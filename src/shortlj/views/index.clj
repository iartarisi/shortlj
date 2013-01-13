(ns shortlj.views.index
  (:require [shortlj.views.common :as common])
  (:use [noir.core :only [defpage defpartial]]
        [noir.response :only [redirect]]
        [hiccup.form]
        [shortlj.backend :only [shorten expand]]))

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

(defpartial wrong-url [url]
  (common/layout
   [:p "Couldn't find a link for the URL you entered: "
    [:a {:id "wrong_url" :href url} url] "."]))

(defpage [:get "/:short"] {:keys [short]}
  (let [url (expand short)]
    (if url
      (redirect url)
      (wrong-url (str base-url short)))))