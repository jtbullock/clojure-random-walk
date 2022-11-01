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

(defn get-all-boundary-points
  "Return a list of all points on boundaries of simulation."
  [simulation]
  (let [sim-width (simulation :width)
        sim-height (simulation :height)
        max-x (- sim-height 1)
        max-y (- sim-width 1)]
    (-> (for [x [0 max-x]
              y (range sim-width)]
          [x y])
        (concat (for [x (range 1 max-x)
                      y [0 max-y]]
                  [x y])))))

(defn get-random-boundary-point
  "Get a point in a random location on the simulation boundary."
  [simulation]
  (rand-nth (get-all-boundary-points simulation)))

(def particle-move-directions
  [{:name "down" :shift [1 0] :test (fn [particle sim] (not= (first particle) (dec (sim :height))))}
   {:name "up" :shift [-1 0] :test (fn [particle _] (not= (first particle) 0))}
   {:name "right" :shift [0 1] :test (fn [particle sim] (not= (second particle) (dec (sim :width))))}
   {:name "left" :shift [0 -1] :test (fn [particle _] (not= (second particle) 0))}])

(defn move-particle
  "Move `particle` in a random direction within the `simulation`",
  [particle simulation]
  (let [possible-directions (filter #((:test %) particle simulation) particle-move-directions)
        random-direction-shift (:shift (rand-nth possible-directions))]
    (mapv + particle random-direction-shift)))