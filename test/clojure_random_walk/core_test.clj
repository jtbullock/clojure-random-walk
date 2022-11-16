(ns clojure-random-walk.core-test
  (:require [clojure.test :refer :all]
            [clojure-random-walk.core :refer :all]))

(deftest read-args-test
  (testing "When unknown flag provided, config parse fails with a message."
    (is (= {:successful? false :message "Unknown config flag '-test' provided."} (read-args ["-test"]))))
  (testing "When multiple flags provided, if the first is unknown, config parse fails with message."
    (is (= {:successful? false :message "Unknown config flag '-test' provided."}
           (read-args ["-test" "-another-test" "-output"]))))

  (testing "When valid flag provided, return config map with flag."
    (let [config-parse-results (read-args ["-output"])
          config (config-parse-results :config)]
      (is (contains? config :output ))))

  (testing "When flag expects a value to be provided, return config map with flag and value."
    (let [config-parse-results (read-args ["-output" "console"])
          config (config-parse-results :config)
          output-config (config :output)]
      (is (= output-config "console")))))

; Test cases to write: When value provided doesn't fit into valid values