(ns clojure-random-walk.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn create-simulation 
  "Create a new simluation with the specified `width` and `height`"
  [width height]
  {:width width, :height height, :points [] }
)