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
   :headers {"Content-Type" "application/gpx, application/x-gpx+xml, application/xml-gpx"
             "Access-Control-Allow-Origin" "*"}
   :body data})

(def calories-per-croissant 400)
(def metres-per-mile 1609)


(defn present-metrics [raw-metrics]
  {"miles-ridden" (/ (get raw-metrics "metres-ridden") metres-per-mile)
   "metres-climbed" (get raw-metrics "metres-climbed")
   "crank-rotations" (get raw-metrics "crank-rotations")
   "calories-burnt" (get raw-metrics "calories-burnt")
   "croissants-equivalent" (/ (get raw-metrics "calories-burnt") 
                              calories-per-croissant)})


(defroutes app
  (GET "/metrics" [] (json-response (present-metrics (db-metrics))))
  (GET "/gpx" [] (gpx-response(db-gpx))))

(defn -main [port] (run-jetty app  {:port (Integer. port) :join? false}))
