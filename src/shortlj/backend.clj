;; -*- coding: utf-8 -*-
;; Copyright (c) 2012 Ionuț Arțăriși <ionut@artarisi.eu>
;; This file is part of Shortlj.

;; Shortlj is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; Shortlj is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with Shortlj.  If not, see <http://www.gnu.org/licenses/>.

(ns shortlj.backend
  (:require [taoensso.carmine :as car])
  (:use [clojure.math.numeric-tower :only (expt)]))

(def digits "0123456789abcdefghijklmnopqrstuvwxyz")
(def redis-server )
(defmacro wcar [& body]
  `(car/with-conn (car/make-conn-pool) (car/make-conn-spec :db 1) ~@body))

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

(defn shorten
  "Shorten a given URL, returning the unique id of that URL"
  [url]
  (let [existing-url (wcar (car/get (str "urls|" url)))]
    (if existing-url
      existing-url
      (let [short (int_to_base36 (int (wcar (car/incr "url_counter"))))]
        (wcar (car/set (str "urls|" url) short)
              (car/set (str "shorts|" short) url))
        (str short)))))
