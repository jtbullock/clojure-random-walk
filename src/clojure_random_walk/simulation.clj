(ns clojure-random-walk.simulation
  (:require [clojure.set :refer [intersection]]))

; Possible directions a particle can move, and test functions
; to determine if the direction is valid given a particle's position in the simulation.
(def particle-move-directions
  [{:name "down"  :shift [ 1  0] :test (fn [particle sim] (not= (first particle) (dec (sim :height))))}
   {:name "up"    :shift [-1  0] :test (fn [particle _] (not= (first particle) 0))}
   {:name "right" :shift [ 0  1] :test (fn [particle sim] (not= (second particle) (dec (sim :width))))}
   {:name "left"  :shift [ 0 -1] :test (fn [particle _] (not= (second particle) 0))}])

(defn add-fixed-particle
  "Add fixed particle to `simulation` at coordinates `[x y]`."
  [simulation [x y]]
  (update simulation :fixed-particles conj [x y]))

(defn add-fixed-particles
  "Add fixed `particles` ([x y] coordinates) to `simulation`."
  [simulation & particles]
  (reduce add-fixed-particle simulation particles))

(defn create-simulation
  "Create a new simulation with the specified `width` and `height`."
  [width height]
  {:width width, :height height, :fixed-particles #{}})

(defn get-adjacent-points
  "Return a lazy sequence of points adjacent to the provided `[x y]` coordinates."
  [[x y]]
  (->> #{[1 0] [-1 0] [0 1] [0 -1]}
       (map (fn [[x2 y2]] [(+ x x2) (+ y y2)])) set))

(defn get-all-boundary-points
  "Return a lazy seq of all points on boundary of simulation."
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

(defn move-particle
  "Move `particle` in a random direction within the `simulation`.",
  [simulation particle]
  (let [possible-directions (filter #((:test %) particle simulation) particle-move-directions)
        random-direction-shift (:shift (rand-nth possible-directions))]
    (mapv + particle random-direction-shift)))

(defn particles-adjacent?
  "Determine if two particles (in [x y] format) are adjacent to each other."
  [particle-1 particle-2]
  (contains? (get-adjacent-points particle-1) particle-2))

(defn simulation-contains-adjacent-fixed-particle?
  "Determine if `simulation` contains a fixed particle adjacent to the `[x y]` coordinates."
  [simulation point]
  (->> (get-adjacent-points point)
       (intersection (simulation :fixed-particles)) seq boolean))

(defn walk-particle
  "Randomly walk `particle` around `simulation` until it wanders next to a fixed particle."
  [particle simulation]
  (->> (iterate (partial move-particle simulation) particle)
       (drop-while #(not (simulation-contains-adjacent-fixed-particle? simulation %)))
       (first)))

(defn run-simulation
  "Run a preset simulation! Temporary until command line args are set up."
  []
  (let [simulation (add-fixed-particle (create-simulation 100 100) [49 49])]
    (reduce (fn [acc, _] (add-fixed-particle acc (walk-particle (get-random-boundary-point acc) acc)))
            simulation
            (range 600))))