(ns clojure-random-walk.simulation-test
  (:require [clojure.test :refer :all]
            [clojure-random-walk.simulation :refer :all]))

(deftest create-simulation-test
  (testing "create-simulation returns a new simulation with the provided
            width and height."
    (is (let [width 200 height 200]
          (= {:width width :height height :fixed-particles #{}}
             ( create-simulation width height))))))

(deftest add-fixed-point-test
  (testing "add-fixed-particle adds a new point to simulation."
    (is (let [simulation (create-simulation 200 200)]
          (= {:width 200 :height 200 :fixed-particles #{[50,50]}}
             ( add-fixed-particle simulation [50 50]))))))

(deftest add-fixed-points-test
  (testing "add-fixed-particles adds multiple points to simulation."
    (is (let [simulation (create-simulation 200 200)]
          (= {:width 200 :height 200 :fixed-particles #{[10,10] [20,20] [34,35]}}
             (add-fixed-particles simulation [10,10] [20,20] [34,35]))))))

(deftest points-adjacent?-test
  (let [fixed-point [50 50]]
    (testing "When no point is next to coordinates, returns false."
      (is (= false (particles-adjacent? fixed-point [5 5]))))

    (testing "When fixed point is directly above coordinates, returns true."
      (is (= true (particles-adjacent? fixed-point [50 51]))))

    (testing "When fixed point is directly below coordinates, returns true."
      (is (= true (particles-adjacent? fixed-point [50 49]))))

    (testing "When fixed point is directly to the left, returns true."
      (is (= true (particles-adjacent? fixed-point [51 50]))))

    (testing "When fixed point is directly to the right, returns true."
      (is (= true (particles-adjacent? fixed-point [49 50]))))

    (testing "When fixed point is above and to the left, returns false."
      (is (= false (particles-adjacent? fixed-point [51 51]))))

    (testing "When fixed point is at x+1 but y+5, returns false."
      (is (= false (particles-adjacent? fixed-point [51 55]))))

    (testing "When fixed point is at y+1 but x+5, returns false."
      (is (= false (particles-adjacent? fixed-point [55 51]))))))

(deftest get-adjacent-points-test
  (testing "Returns adjacent points"
    (is (= #{[50 49] [50 51] [49 50] [51 50]} (get-adjacent-points [50 50])))))

(deftest simulation-contains-adjacent-point?-test
  (let [simulation (add-fixed-particle (create-simulation 200 200) [50 50])]
    (testing "When point is not next to fixed point, returns false."
      (is (= false (simulation-contains-adjacent-fixed-particle? simulation [2 2]))))
    (testing "When point is next to fixed point, returns true"
      (is (= true (simulation-contains-adjacent-fixed-particle? simulation [50 51]))))

    (let [multi-point-sim (add-fixed-particle simulation [40 40])]
      (testing "When sim has multiple fixed points, and point
                is next to a fixed point, returns true."
        (is (= false (simulation-contains-adjacent-fixed-particle? multi-point-sim [2 2]))))
      (testing "When sim has multiple fixed points, and point
                is not next to a fixed point, returns false."
        (is (= true (simulation-contains-adjacent-fixed-particle? multi-point-sim [40 41])))))))

(deftest get-all-boundary-points-test
  (testing "Returns all points along border of 3x3 simulation."
    ; 3x3 simulation grid:
    ; [0 0] [0 1] [0 2]
    ; [1 0] [1 1] [1 2]
    ; [2 0] [2 1] [2 2]
    (let [simulation (create-simulation 3 3)
          expected-points #{[0 0] [0 1] [0 2]
                            [1 0]       [1 2]
                            [2 0] [2 1] [2 2]}]
      (is (= expected-points (set (get-all-boundary-points simulation))))))

  (testing "Returns all points along border of 5x4 simulation."
    ; 5x4 simulation grid:
    ; [0 0] [0 1] [0 2] [0 3] [0 4]
    ; [1 0] [1 1] [1 2] [1 3] [1 4]
    ; [2 0] [2 1] [2 2] [2 3] [2 4]
    ; [3 0] [3 1] [3 2] [3 3] [3 4]
    (let [simulation (create-simulation 5 4)
          expected-points #{[0 0] [0 1] [0 2] [0 3] [0 4]
                            [1 0]                   [1 4]
                            [2 0]                   [2 4]
                            [3 0] [3 1] [3 2] [3 3] [3 4]}]
      (is (= expected-points (set (get-all-boundary-points simulation)))))))

; To test the random results, running the function many times
; to get a good sample set, and checking all the results.
(deftest get-random-boundary-point-test
  (testing "Always returns a random point along boundary of 3x3 simulation.")
  (let [simulation (create-simulation 3 3)
        expected-points (set (get-all-boundary-points simulation))
        random-points (repeatedly 81 (partial get-random-boundary-point simulation))]
    (is (every? (partial contains? expected-points) random-points)))
  (testing "Always returns a random point along boundary of 5x4 simulation.")
  (let [simulation (create-simulation 5 4)
        expected-points (set (get-all-boundary-points simulation))
        random-points (repeatedly 400 (partial get-random-boundary-point simulation))]
    (is (every? (partial contains? expected-points) random-points))))

(deftest move-particle-test
  (let [simulation (create-simulation 3 3)]
    (testing "Particle is moved."
      (is (not= [0 0] (move-particle simulation [0 0]))))
    (testing "Particle does not move past top of simulation."
      (let [moved-particles (repeatedly 10 #(move-particle simulation [0 1]))]
        (is (every? #(>= (first %) 0) moved-particles))))
    (testing "Particle does not move past left of simulation."
      (let [moved-particles (repeatedly 10 #(move-particle simulation [1 0]))]
        (is (every? #(>= (second %) 0) moved-particles))))
    (testing "Particle does not move past right of simulation."
      (let [moved-particles (repeatedly 10 #(move-particle simulation [1 2]))]
        (is (every? #(<= (second %) 2) moved-particles))))
    (testing "Particle does not move past bottom of simulation."
      (let [moved-particles (repeatedly 10 #(move-particle simulation [2 1]))]
        (is (every? #(<= (first %) 2) moved-particles))))))

(deftest walk-particle-test
  (testing "Particle's final location is next to a fixed particle"
    (let [fixed-particle [4 4]
          simulation (add-fixed-particles (create-simulation 10 10) fixed-particle)
          walked-particle (walk-particle (get-random-boundary-point simulation) simulation)]
      (is (particles-adjacent? fixed-particle walked-particle)))))
