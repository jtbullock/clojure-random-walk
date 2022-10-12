(ns clojure-random-walk.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn create-simulation 
  "Create a new simluation with the specified `width` and `height`"
  [width height]
  {:width width, :height height, :fixed-points #{} }
)

(defn add-fixed-point
  "Add a fixed point to a simulation"
  [simulation x y]
  (update simulation :fixed-points conj [x y])
)