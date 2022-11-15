(ns clojure-random-walk.image-output
  (:import (java.awt.image BufferedImage))
  (:import (java.awt Color))
  (:import (javax.imageio ImageIO)
           (java.io FileOutputStream)))

(def color-rgb-fg (.getRGB Color/WHITE))

(defn create-image
  "Create an empty Java BufferedImage with the provided width and height."
  [width height] (BufferedImage. width height BufferedImage/TYPE_INT_RGB))

(defn get-image-pixels
  "Return a vector of the color values of all pixels in the `image`."
  [image]
  (vec (.getRGB image 0 0 (.getWidth image) (.getHeight image) nil 0 (.getWidth image))))

(defn write-image
  "Write an image to file at the uri."
  [image uri]
  (with-open [file (FileOutputStream. uri)]
    (ImageIO/write image "JPG" file)))

(defn write-particles-to-image
  "Write the set of `particles` to the `image` at the given `scale`.
   At scale 1, 1 particle = 1 pixel. At scale 2, 1 particle = a 2x2 pixel square, and so on."
  [image scale particles]
  (reduce (fn [acc particle]
            (let [start-x (* (first particle) scale)
                  start-y (* (second particle) scale)
                  scaled-pixel-colors (int-array (repeat (* scale scale) color-rgb-fg))]
              (doto acc (.setRGB start-x start-y scale scale scaled-pixel-colors 0 scale))))
          image
          particles))