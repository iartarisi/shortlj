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

(ns shortlj.views_test
  (:use clojure.test)
  (:use noir.util.test)
  (:use shortlj.views.index))

(deftest test_expand_url_not_found
  (has-status (send-request "/notfound") 404))

(deftest test_index_get_ok
  (let [response (send-request "/")]
    (has-status response 200)
    (is (.contains (get response :body)
                   "Oh, Hai!"))
    (is (.contains (get response :body)
                   "<form action=\"/\" id=\"shorten\" method=\"POST\""))))
