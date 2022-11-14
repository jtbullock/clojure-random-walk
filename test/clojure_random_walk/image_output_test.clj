(ns clojure-random-walk.image-output-test
  (:require [clojure.test :refer :all]
            [clojure-random-walk.image-output :refer :all]))

(deftest getPixelScalingMatrix-test
  (testing "Creates a matrix for scale 1"
    (is (= #{[0 0]} (getPixelScalingMatrix 1))))
  (testing "Creates a matrix for scale 3"
    (is (= #{[0 0] [0 1] [0 2]
             [1 0] [1 1] [1 2]
             [2 0] [2 1] [2 2]} (getPixelScalingMatrix 3)))))

(deftest particle->scaled_pixels-test
  (testing "Converts a particle to pixels with scale of 1."
    (is (= #{[5 5]} (particle->scaled_pixels [5 5] 1))))
  (testing "Converts a particle to pixels with scale of 2."
    (is (= #{[5 5] [5 6]
             [6 5] [6 6]} (particle->scaled_pixels [5 5] 2)))))