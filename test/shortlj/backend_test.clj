(ns shortlj.backend_test
  (:use clojure.test)
  (:use shortlj.backend))

(deftest test_find_factor
  (is (= (find-factor 0) 0))
  (is (= (find-factor 35) 0))
  (is (= (find-factor 36) 1))
  (is (= (find-factor 1295) 1))
  (is (= (find-factor 1296) 2)))

(deftest test_int_to_base36
  (is (= (int_to_base36 0) "0"))
  (is (= (int_to_base36 36) "10"))
  (is (= (int_to_base36 3600) "2s0")))