(ns clojure-random-walk.image-output
  (:import (java.awt.image BufferedImage))
  (:import (java.awt Color))
  (:import (javax.imageio ImageIO)
           (java.io FileOutputStream)))

(defn create-image [w h] (BufferedImage. w h BufferedImage/TYPE_INT_RGB))

(def fg-color-rgb (.getRGB Color/WHITE))

(defn write-image
  [img uri]
  (with-open [file (FileOutputStream. uri)]
    (ImageIO/write img "JPG" file)))

(defn getPixels
  [img]
  (vec (.getRGB img 0 0 (.getWidth img) (.getHeight img) nil 0 (.getWidth img))))

(defn writeParticlesToImage
  "Write the set of `particles` to the `image` at the given `scale`"
  [image scale particles]
  (reduce (fn [acc particle]
            (let [start_x (* (first particle) scale)
                  start_y (* (second particle) scale)
                  pixel_color_arr (int-array (repeat (* scale scale) fg-color-rgb))]
              (doto acc (.setRGB start_x start_y scale scale pixel_color_arr 0 scale))))
          image
          particles))