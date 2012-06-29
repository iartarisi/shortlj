(ns shortlj.backend
  (:use [clojure.math.numeric-tower :only (expt)]))

(def digits "0123456789abcdefghijklmnopqrstuvwxyz")

(defn find-factor [i]
  (if (= i 0)
    0
    (last (take-while #(>= i (expt 36 %))
                    (iterate inc 0)))))
                
(defn int_to_base36 [i]
  ((fn foo [i factor]
     (let [j (expt 36 factor)]
       (if (>= factor 0)
         (str (nth digits (quot i j))
              (foo (mod i j) (dec factor))))))
   i (find-factor i)))
