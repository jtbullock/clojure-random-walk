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
  "Add a fixed point to a `simulation` at coordinates `x` and `y`"
  [simulation x y]
  (update simulation :fixed-points conj [x y])
)

(defn are-points-adjacent?
  "Determine if coordinates `x` and `y` are adjacent to a fixed point
   with coordinates `fixed-x fixed-y`"
  [[fixed-x fixed-y] [x y]]
  (let [x-delta (abs (- x fixed-x))
        y-delta (abs (- y fixed-y))]
    (or (and (= 0 x-delta) (= 1 y-delta))
        (and (= 1 x-delta) (= 0 y-delta)))
  )
)