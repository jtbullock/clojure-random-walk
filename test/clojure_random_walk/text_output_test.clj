(ns clojure-random-walk.text-output-test
  (:require [clojure.test :refer :all]
            [clojure-random-walk.text-output :refer :all]
            [clojure-random-walk.simulation :refer [add-fixed-particles create-simulation]]))

(deftest simulation-row->printable-string-test
  (let [simulation (add-fixed-particles (create-simulation 6 3) [1 3] [1 5])]
    (testing "Turns a simulation row into a string"
      (is (= "--O-O-" (simulation-row->printable-string simulation 1))))))

(deftest simulation->printable-strings-test
  (let [simulation (add-fixed-particles (create-simulation 6 3) [1 3] [1 5] [2 2] [3 2] [3 6])]
    (testing "Simulation->printable-strings turns a simulation
             into vector of printable strings."
      (is (= ["--O-O-" "-O----" "-O---O"] (simulation->printable-strings simulation))))))
