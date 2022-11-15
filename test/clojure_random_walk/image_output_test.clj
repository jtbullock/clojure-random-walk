(ns clojure-random-walk.image-output-test
  (:require [clojure.test :refer :all]
            [clojure-random-walk.image-output :refer :all]))

(deftest writeParticlesToImage-test
  (let [particles #{[1 1] [0 1] [1 0]}
        black -16777216 white -1]
    (testing "Writes particles to an image at scale of 1."
      (let [pixels (-> (create-image 3 3) (writeParticlesToImage 1 particles) getPixels)]
        (is (= [black white black
                white white black
                black black black] pixels))))
    (testing "Write particles to an image at scale of 2."
      (let [pixels (-> (create-image 6 6) (writeParticlesToImage 2 particles) getPixels)]
        (is (= [black black white white black black
                black black white white black black
                white white white white black black
                white white white white black black
                black black black black black black
                black black black black black black] pixels))))))