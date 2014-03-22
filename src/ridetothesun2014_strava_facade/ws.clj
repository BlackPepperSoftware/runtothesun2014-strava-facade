(ns ridetothesun2014-strava-facade.ws
  (:use [ring.adapter.jetty]
        [ring.util.response]
        [clojure.data.json :as json :only [write-str]]
        compojure.core
        ridetothesun2014-strava-facade.db)
  (:require 
        [compojure.route :as route]))



(defn json-response [data & [status]]
    {:status (or status 200)
        :headers {"Content-Type" "application/json"}
        :body (json/write-str data)})

(defn gpx-response [data & [status]]
    {:status (or status 200)
        :headers {"Content-Type" "application/gpx, application/x-gpx+xml, application/xml-gpx"}
        :body data})

(defroutes app
    (GET "/metrics" [] (json-response (metrics)))
    (GET "/gpx" [] (gpx-response(gpx))))

(defn -main [port] (run-jetty app  {:port (Integer. port) :join? false}))
