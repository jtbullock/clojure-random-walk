(ns clojure-random-walk.core-test
  (:require [clojure.test :refer :all]
            [clojure-random-walk.core :refer :all]))

(deftest create-simulation-test
  (testing "create-simulation returns a new simulation with the provided
            width and height."
    (is (let [width 200 height 200]
          (= {:width width :height height :fixed-points #{}} 
             ( create-simulation width height))))))

(deftest add-fixed-point-test
  (testing "add-fixed-point adds a new point to simulation."
    (is (let [simulation (create-simulation 200 200)]
          (= {:width 200 :height 200 :fixed-points #{[50,50]}}
             ( add-fixed-point simulation 50 50))))))

(deftest are-points-adjacent?-test
  (let [fixed-point [50 50]]
    (testing "When no point is next to coordinates, returns false."
      (is (= false (are-points-adjacent? fixed-point [5 5]))))
    
    (testing "When fixed point is directly above coordinates, returns true."
      (is (= true (are-points-adjacent? fixed-point [50 51])))) 
  
    (testing "When fixed point is directly below coordinates, returns true."
      (is (= true (are-points-adjacent? fixed-point [50 49]))))
    
    (testing "When fixed point is directly to the left, returns true."
      (is (= true (are-points-adjacent? fixed-point [51 50]))))
    
    (testing "When fixed point is directly to the right, returns true."
      (is (= true (are-points-adjacent? fixed-point [49 50]))))
    
    (testing "When fixed point is above and to the left, returns false."
      (is (= false (are-points-adjacent? fixed-point [51 51]))))
    
    (testing "When fixed point is at x+1 but y+5, returns false."
      (is (= false (are-points-adjacent? fixed-point [51 55]))))
    
    (testing "When fixed point is at y+1 but x+5, returns false."
      (is (= false (are-points-adjacent? fixed-point [55 51]))))
  )
)