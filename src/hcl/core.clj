(ns hcl.core
  (:use [clojure.core.match :refer [match]])
  (:require [clojure.string :as str]))

(def default-indent "  ")
(def ^:dynamic *indent* "")
(def ^:dynamic *level* 0)

(defn- quote-string [name]
  (format "\"%s\"" (str/replace name "\"" "\\\"")))

(defmacro indented-lines [block]
  `(binding [*level*  (inc *level*)
             *indent* (str/join "" (repeat *level* default-indent))]
     (let [block#   ~block]
       (if (not (empty? block#))
         (let [lines# (str/split block# #"\n")]
           (str (->> lines#
                     (map #(str *indent* %))
                     (str/join "\n"))
                "\n"))
         ""))))

(defn kv [f]
  #(apply f %))

(defn emit [value]
  (cond (nil? value)
        ""
        (map? value)
        (format (case *level* 0 "%s" "{\n%s}")
                (->> value
                     (map (kv #(format "%s %s\n"
                                       (cond (vector? %1)
                                             (case (count %1)
                                               1 (apply format "%s" (name %1))
                                               2 (apply format "%s \"%s\"" (map name %1)))
                                             :else
                                             (name %1))
                                       (cond (map? %2)
                                             (emit %2)
                                             :else
                                             (format "= %s" (emit %2))))))
                     (str/join "")
                     indented-lines))

        (vector? value)
        (format "[\n%s]" (indented-lines (str/join ",\n" (map emit value))))

        :else
        (cond (string? value)
              (quote-string value)
              :else
              (str value))))
