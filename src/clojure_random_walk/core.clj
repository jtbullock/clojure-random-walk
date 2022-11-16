(ns clojure-random-walk.core
  (:require [clojure-random-walk.simulation :refer [run-simulation]]
            [clojure-random-walk.text-output :refer [print-simulation-to-console]]
            [clojure-random-walk.image-output :refer [write-particles-to-image create-image write-image]]))

(def valid-args { "-output" { :valid-values #{ "console" "image" } } } )

(defn read-args
  "Parse command line arguments into a map."
  [args]
  (let [first-flag (first args)]
    (if (contains? valid-args first-flag)
      { :successful? true :config { :output "" } }
      { :successful? false :message (str "Unknown config flag '" first-flag "' provided.")})))

(defn output-image
  "Output simulation results as an image file."
  [simulation-results]
  (let [image-scale 50
        output-image-width (* image-scale (simulation-results :width))
        output-image-height (* image-scale (simulation-results :height))
        output-image (create-image output-image-width output-image-height)
        output-image-with-results (write-particles-to-image output-image image-scale (simulation-results :fixed-particles))]
    (write-image output-image-with-results "./result.jpg")))

(defn -main [& args]
  (do
    (println "Running simulation...")
    (let [sim-results (run-simulation)
          config (read-args args)]
      (cond (= (config :output) :console) (print-simulation-to-console sim-results)
            (= (config :output) :image) (output-image sim-results)))))