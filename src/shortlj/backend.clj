(ns shortlj.backend
  (:use [clojure.math.numeric-tower :only (expt)]))

(def digits "0123456789abcdefghijklmnopqrstuvwxyz")

(defn find-factor [i]
  (if (= i 0)
    0
    (last (take-while #(>= i (expt 36 %))
                    (iterate inc 0)))))
                
(defn int_to_base36 [i]
  (if (= i 0)
    "0"
    (reduce str ((fn foo [i factor]
                   (let [j (expt 36 factor)]
                     (if (>= factor 0)
                       (conj (foo (mod i j) (dec factor))
                             (nth digits (quot i j)))
                       '()))) i (find-factor i)))))
