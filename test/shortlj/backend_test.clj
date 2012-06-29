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

(ns shortlj.backend_test
  (:require [redis.core :as redis])
  (:use clojure.test)
  (:use shortlj.backend))

(def test-server {:host "127.0.0.1" :db 1})
(def test-url "http://doesnt.exist")

(deftest test_find_factor
  (is (= (find-factor 0) 0))
  (is (= (find-factor 35) 0))
  (is (= (find-factor 36) 1))
  (is (= (find-factor 1295) 1))
  (is (= (find-factor 1296) 2)))

(deftest test_int_to_base36
  (is (= (int_to_base36 0) "0"))
  (is (= (int_to_base36 2) "2"))
  (is (= (int_to_base36 36) "10"))
  (is (= (int_to_base36 3600) "2s0")))

(use-fixtures :each (fn [f]
                      ;; setUp
                      (redis/with-server test-server
                        (redis/flushdb))
                      ;; run test
                      (f)))

(deftest test_shorten_already_exists
  (redis/with-server test-server
    (redis/set (str "urls|" test-url) "a1f")
    (redis/get "urls|http://doesnt.exist")
    (is (= "a1f" (shorten test-url)))))

(deftest test_shorten_doesnt_exist_returns_valid
  (redis/with-server test-server
    (is (nil? (redis/get (str "urls|" test-url))))
    (is (= "1" (shorten test-url)))))

(deftest test_shorten_doesnt_exist_returns_next
  (redis/with-server test-server
    (redis/set "url_counter" 51)
    (is (= "1g" (shorten test-url)))))

(deftest test_shorten_doesnt_exist_creates_new
  (redis/with-server test-server
    (is (nil? (redis/get (str "urls|" test-url))))
    (shorten test-url)
    (is (= "1" (redis/get (str "urls|" test-url))))
    (is (= test-url (redis/get "shorts|1")))))

(deftest test_shorten_doesnt_exist_create_new_next
  (redis/with-server test-server
    (redis/set "url_counter" 51)
    (shorten test-url)
    (is (= "1g" (redis/get (str "urls|" test-url))))
    (is (= test-url (redis/get "shorts|1g")))))