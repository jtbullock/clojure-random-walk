(ns clojure-random-walk.core 
  (:require [clojure.set :refer [intersection]]))

(defn create-simulation 
  "Create a new simluation with the specified `width` and `height`."
  [width height]
  {:width width, :height height, :fixed-points #{}})

(defn add-fixed-point
  "Add a fixed point to a `simulation` at coordinates `x` and `y`."
  [simulation [x y]]
  (update simulation :fixed-points conj [x y]))

(defn add-fixed-points
  "Add fixed `points` to `simulation`"
  [simulation & points]
  (reduce add-fixed-point simulation points))

(defn get-adjacent-points
  "Return a set of points adjacent to the provided `[x y]` coordinates."
  [[x y]]
  (->> #{[1 0] [-1 0] [0 1] [0 -1]}
       (map (fn [[x2 y2]] [(+ x x2) (+ y y2)])) set))

(defn are-points-adjacent?
  "Determine if coordinates `[x y]` are adjacent to a fixed point
   with coordinates `[fixed-x fixed-y]`."
  [fixed-point point]
  (contains? (get-adjacent-points point) fixed-point))

(defn simulation-contains-adjacent-point?
  "Determine if simulation contains a fixed point adjacent to 
   the `[x y]` coordinates."
  [simulation point]
  (->> (get-adjacent-points point) 
       (intersection (simulation :fixed-points)) seq boolean))

(defn simulation-row->printable-string
  "Converts a `simulation` `row` into a printable string."
  [simulation row-number]
  (reduce
   (fn [row-str col-number]
     (let [coordinates [row-number (inc col-number)]
           fixed-point-at-coordinates? (contains? (simulation :fixed-points) coordinates)]
       (str row-str (if fixed-point-at-coordinates? "O" "-"))))
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