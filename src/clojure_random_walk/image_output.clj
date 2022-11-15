(ns clojure-random-walk.image-output
  (:import (java.awt.image BufferedImage))
  (:import (java.awt Color))
  (:import (javax.imageio ImageIO)
           (java.io FileOutputStream)))

(defn create-image [w h] (BufferedImage. w h BufferedImage/TYPE_INT_RGB))

(def color-rgb-fg (.getRGB Color/WHITE))

(defn write-image
  [img uri]
  (with-open [file (FileOutputStream. uri)]
    (ImageIO/write img "JPG" file)))

(defn get-image-pixels
  [img]
  (vec (.getRGB img 0 0 (.getWidth img) (.getHeight img) nil 0 (.getWidth img))))

(defn write-particles-to-image
  "Write the set of `particles` to the `image` at the given `scale`"
  [image scale particles]
  (reduce (fn [acc particle]
            (let [start-x (* (first particle) scale)
                  start-y (* (second particle) scale)
                  scaled-pixel-colors (int-array (repeat (* scale scale) color-rgb-fg))]
              (doto acc (.setRGB start-x start-y scale scale scaled-pixel-colors 0 scale))))
          image
          particles))