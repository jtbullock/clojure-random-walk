(ns clojure-random-walk.simulation
  (:require [clojure.set :refer [intersection]]))

(defn create-simulation
  "Create a new simulation with the specified `width` and `height`."
  [width height]
  {:width width, :height height, :fixed-particles #{}})

(defn add-fixed-particle
  "Add a fixed point to a `simulation` at coordinates `x` and `y`."
  [simulation [x y]]
  (update simulation :fixed-particles conj [x y]))

(defn add-fixed-particles
  "Add fixed `points` to `simulation`"
  [simulation & points]
  (reduce add-fixed-particle simulation points))

(defn get-adjacent-points
  "Return a set of points adjacent to the provided `[x y]` coordinates."
  [[x y]]
  (->> #{[1 0] [-1 0] [0 1] [0 -1]}
       (map (fn [[x2 y2]] [(+ x x2) (+ y y2)])) set))

(defn points-adjacent?
  "Determine if coordinates `[x y]` are adjacent to a fixed point
   with coordinates `[fixed-x fixed-y]`."
  [fixed-particle particle]
  (contains? (get-adjacent-points particle) fixed-particle))

(defn simulation-contains-adjacent-point?
  "Determine if simulation contains a fixed point adjacent to
   the `[x y]` coordinates."
  [simulation point]
  (->> (get-adjacent-points point)
       (intersection (simulation :fixed-particles)) seq boolean))

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
  [simulation particle]
  (let [possible-directions (filter #((:test %) particle simulation) particle-move-directions)
        random-direction-shift (:shift (rand-nth possible-directions))]
    (mapv + particle random-direction-shift)))

(defn walk-particle
  "Randomly walk `particle` around `simulation` until it wanders next to
  a fixed particle."
  [particle simulation]
  (->> (iterate (partial move-particle simulation) particle)
       (drop-while #(not (simulation-contains-adjacent-point? simulation %)))
       (first)))

(defn run-simulation
  "Run a simulation!"
  []
  (let [simulation (add-fixed-particle (create-simulation 100 100) [49 49])]
    (reduce (fn [acc, _] (add-fixed-particle acc (walk-particle (get-random-boundary-point acc) acc)))
            simulation
            (range 600))))
