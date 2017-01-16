(set-env! :dependencies '[[boot-bundle "0.1.0-SNAPSHOT" :scope "test"]])
(require '[boot-bundle :refer [expand-keywords]])
(reset! boot-bundle/bundle-file-path "./boot.bundle.edn")

(set-env! :resource-paths #{"src"}
          :dependencies (expand-keywords [:bootlaces]))

(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.1.0-SNAPSHOT")

(bootlaces! +version+)

(task-options!
  pom {:project     'com.tristanstraub/clj-hcl
       :version     +version+
       :description "Formatted maps as hcl for use with hashicorp tools"
       :url         "https://github.com/tristanstraub/clj-hcl"
       :scm         {:url "https://github.com/tristanstraub/clj-hcl"}
       :license     {"MIT" "https://opensource.org/licenses/MIT"}})

(deftask build []
  (comp (pom)
        (jar)
        (install)))

(deftask release []
  (comp (build-jar)
        (push-snapshot)))
