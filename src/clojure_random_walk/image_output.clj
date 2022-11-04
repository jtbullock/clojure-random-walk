(ns clojure-random-walk.image-output
  (:import (java.awt.image BufferedImage))
  (:import (java.awt Color))
  (:import (javax.imageio ImageIO)
           (java.io FileOutputStream)))

(defn create-image [w h] (BufferedImage. w h BufferedImage/TYPE_INT_RGB))

(def fg-color (.getRGB Color/WHITE))

(defn draw-square [img color]
  (doto img
    (.setRGB 20 20 color)
    (.setRGB 20 21 color)
    (.setRGB 21 20 color)
    (.setRGB 21 21 color)))

(defn write-image
  [img uri]
  (with-open [file (FileOutputStream. uri)]
    (ImageIO/write img "JPG" file)))

(defn getPixels
  [img]
  (vec (.getRGB img 0 0 (.getWidth img) (.getHeight img) nil 0 (.getWidth img))))

(defn getRGBTest
  []
  (-> (create-image 3 3)
      (doto
        (.setRGB 1 1 fg-color))
      (getPixels)))

