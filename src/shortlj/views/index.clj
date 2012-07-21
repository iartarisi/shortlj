(ns shortlj.views.index
  (:require [shortlj.views.common :as common])
  (:use [noir.core :only [defpage]]
        [hiccup.form]))

(defpage "/" []
  (common/layout
   [:h1 "Oh, Hai!"]
   [:p "Enter your URL below to shorten it:"]
   (form-to {:id "shorten"}
            [:post "/"]
            (text-field :url))))