(ns shortlj.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page :only [include-css html5]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "Shortlj!"]
               (include-css "/css/style.css")]
              [:body
               content]))
