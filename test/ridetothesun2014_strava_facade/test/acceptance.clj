(ns ridetothesun2014-strava-facade.test.acceptance
  (:use [midje.sweet]
        ridetothesun2014-strava-facade.db
        ridetothesun2014-strava-facade.gpx
        ridetothesun2014-strava-facade.strava
        ridetothesun2014-strava-facade.ws)
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]))

(def example-metrics 
  {:miles-done 970.1
   :metres-climbed 8848.1 
   :crank-rotations 2000000.1
   :calories-burnt 264000.1})

(defn request [resource web-app & params]
  (web-app {:request-method :get :uri resource :params (first params)}))

(facts "Ride to the Sun 2014 Strava Facade acceptance tests"
       (fact "The facade returns metrics"
             (update example-metrics "fake-gpx")
             (let [resp (request "/metrics" app)
                   status (:status resp)
                   headers (:headers resp)
                   json-content (json/read-str (:body resp))]
               status => 200
               (get headers "Content-Type") => "application/json"
               (get json-content "miles-done") => 970 
               (get json-content "metres-climbed") => 8848 
               (get json-content "crank-rotations") => 2000000 
               (get json-content "calories-burnt") => 264000 
               (get json-content "croissants-equivalent") => 660))

       (fact "The facade returns gpx"
             (update example-metrics "fake-gpx")
             (let [resp (request "/gpx" app)
                   status (:status resp)
                   headers (:headers resp)
                   gpx-content (:body resp)]
               status => 200
               (get headers "Content-Type") => "application/gpx, application/x-gpx+xml, application/xml-gpx"
               gpx-content => "fake-gpx"))

       (fact "The facade updates metrics from Strava"
             (update (strava-metrics) "fake-gpx")    
             (let [db-metrics (metrics)]
               (last-updated) => 
               (roughly (quot (System/currentTimeMillis) 1000) 600)
               (get db-metrics "miles-done") => 93757 
               (get db-metrics "metres-climbed") => (roughly 1752)
               (get db-metrics "crank-rotations") => 45618 
               (get db-metrics "calories-burnt") => 3006))

       (fact "The facade updates gpx data from strava"
             (update example-metrics "fake-gpx" )
             (update example-metrics (points-to-gpx (strava-points)))
             (let [db-gpx (gpx)]
               db-gpx => (contains "http://www.topografix.com/GPX/1/1"))))
