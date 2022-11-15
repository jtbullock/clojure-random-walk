(ns clojure-random-walk.core
  (:require [clojure-random-walk.simulation :refer [run-simulation]]
            [clojure-random-walk.text-output :refer [print-simulation-to-console]]
            [clojure-random-walk.image-output :refer [write-particles-to-image create-image write-image]]))

(defn -main [& args]
  (do
    (println "Running simulation...")
    (let [sim-results (run-simulation)
          output-image (create-image (* 50 (sim-results :width)) (* 50 (sim-results :height)))]
      (print-simulation-to-console sim-results)
      (write-image (write-particles-to-image output-image 50 (sim-results :fixed-particles)) "./result.jpg"))))