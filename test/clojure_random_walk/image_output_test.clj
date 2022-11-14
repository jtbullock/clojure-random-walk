(ns clojure-random-walk.image-output-test
  (:require [clojure.test :refer :all]
            [clojure-random-walk.image-output :refer :all]))

(deftest particle->scaled_pixels-test
  (testing "Converts a particle to pixels with scale of 1."
    (is (= #{[5 5]} (particle->scaled_pixels [5 5] 1))))
  (testing "Converts a particle to pixels with scale of 2."
    (is (= #{[5 5] [5 6] [6 5] [6 6]} (particle->scaled_pixels [5 5] 2)))))