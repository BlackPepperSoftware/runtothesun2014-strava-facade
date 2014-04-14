(ns ridetothesun2014-strava-facade.ws
  (:use [ring.adapter.jetty]
        [ring.util.response]
        [clojure.data.json :as json :only [write-str]]
        compojure.core
        ridetothesun2014-strava-facade.db)
  (:require 
    [compojure.route :as route]
    [clojure.math.numeric-tower :as math]
    [clojure.java.io :as io]))



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
(def team-size 4)


(defn present-metrics [raw-metrics]
  {"miles-ridden" (int (math/floor (/ (get raw-metrics "metres-ridden") metres-per-mile)))
   "metres-climbed" (int (math/floor(get raw-metrics "metres-climbed")))
   "crank-rotations" (int (math/floor(* team-size (get raw-metrics "crank-rotations"))))
   "calories-burnt" (int (math/floor (* team-size (get raw-metrics "calories-burnt"))))
   "croissants-equivalent" (int (math/floor 
                             (* team-size 
                               (/ (get raw-metrics "calories-burnt") 
                                 calories-per-croissant))))})


(defroutes app
  (GET "/metrics" [] (json-response (present-metrics (db-metrics))))
  (OPTIONS "/gpx" [] {:status 200
                      :headers {"Access-Control-Allow-Origin" "*"
                                "Access-Control-Allow-Methods" "GET, OPTIONS"
                                "Access-Control-Allow-Headers" "X-Requested-With"
                                "Access-Control-Max-Age" "1800"}})
  (GET "/planned-gpx" [] (gpx-response (slurp (clojure.java.io/resource "france.gpx"))))
  (GET "/gpx" [] (gpx-response(db-gpx))))

(defn -main [port] (run-jetty app  {:port (Integer. port) :join? false}))
