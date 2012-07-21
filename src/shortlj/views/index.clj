(ns shortlj.views.index
  (:require [shortlj.views.common :as common])
  (:use [noir.core :only [defpage defpartial]]
        [hiccup.form]
        [shortlj.backend :only [shorten]]))

(def base-url "http://localhost:8080/")

(defpage [:get "/"] []
  (common/layout
   [:h1 "Oh, Hai!"]
   [:p "Enter your URL below to shorten it:"]
   (form-to {:id "shorten"}
            [:post "/"]
            (text-field :url))))

(defpartial shortened [short original]
  (common/layout
     [:p "This URL: " [:a {:id "shortened" :href short} short]
      " will redirect to: " [:a {:id "original" :href original} original] "."]))
     
  
(defpage [:post "/"] {:keys [url]}
  (shortened (str base-url (shorten url))
             url))