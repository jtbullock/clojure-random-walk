(ns clojure-random-walk.text-output)

(defn simulation-row->printable-string
  "Converts a `simulation` `row` into a printable string."
  [simulation row-number]
  (reduce
   (fn [row-str col-number]
     (let [coordinates [row-number (inc col-number)]
           fixed-particle-at-coordinates? (contains? (simulation :fixed-particles) coordinates)]
       (str row-str (if fixed-particle-at-coordinates? "O" "-"))))
   ""
   (range (simulation :width))))

(defn simulation->printable-strings
  "Converts a `simulation`'s point data to strings that can be printed to console."
  [simulation]
  (map (fn [row] (simulation-row->printable-string simulation (inc row)))
       (range (simulation :height))
))

(defn print-simulation-to-console
  "Print a simulation to the console."
  [simulation]
  (doseq [row (simulation->printable-strings simulation)]
    (println row)))