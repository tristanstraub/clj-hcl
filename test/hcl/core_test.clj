(ns hcl.core-test
  (:require [hcl.core :as hcl]
            [clojure.test :refer [deftest is]]))

(deftest example
  (is (= "job \"build\" {\n  datacenters = [\n      \"ap-southeast-2\"\n  ]\n  update {\n      stagger = \"30s\"\n      max-parallel = 1\n  }\n  group \"load-balancers\" {\n      count = 1\n      restart {\n            attempts = 10\n      }\n  }\n}\n"

         (hcl/emit '{[:job "build"]
                     {:datacenters ["ap-southeast-2"]
                      :update {:stagger      "30s"
                               :max-parallel 1}
                      [:group "load-balancers"]
                      {:count 1
                       :restart {:attempts 10}}}}))))
