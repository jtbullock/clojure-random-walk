(ns clojure-random-walk.core-test
  (:require [clojure.test :refer :all]
            [clojure-random-walk.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest create-simulation-test
  (testing "create-simulation returns a new simulation with the provided
            width and height"
    (is
     (let [width 200 height 200]
      (= {:width width :height height :points [] } 
         ( create-simulation width height ))))))

