(ns clojure-random-walk.core
  (:require [clojure-random-walk.simulation :refer [run-simulation]]
            [clojure-random-walk.text-output :refer [print-simulation-to-console]]))

(defn -main [& args]
  (do
    (println "Running simulation...")
    (print-simulation-to-console (run-simulation))))
