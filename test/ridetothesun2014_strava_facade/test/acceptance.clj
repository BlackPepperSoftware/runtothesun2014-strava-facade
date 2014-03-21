(ns ridetothesun2014-strava-facade.test.acceptance
  (:use [midje.sweet]
        ridetothesun2014-strava-facade.db
        ridetothesun2014-strava-facade.strava
        ridetothesun2014-strava-facade.ws)
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]))

(facts "Ride to the Sun 2014 Strava Facade acceptance tests"
       (fact "The facade returns metrics"
             (update-metrics {:miles-done 970 
                              :metres-climbed 8848 
                              :crank-rotations 2000000 
                              :calories-burnt 264000})    
             (let [resp (app {})
                   status (:status resp)
                   headers (:headers resp)
                   json-content (json/read-str (:body resp))]
               status => 200
               (get headers "Content-Type") => "application/json; charset=utf-8"
               (get json-content "miles-done") => 970 
               (get json-content "metres-climbed") => 8848 
               (get json-content "crank-rotations") => 2000000 
               (get json-content "calories-burnt") => 264000 
               (get json-content "croissants-equivalent") => 660))

       (fact "The facade updates itself from Strava"
             (update-metrics (strava-metrics))    
             (let [db-metrics (metrics)]
               (last-updated) => 
               (roughly (quot (System/currentTimeMillis) 1000) 600)
               (get db-metrics "miles-done") => 93757.4 
               (get db-metrics "metres-climbed") => (roughly 1752.9)
               (get db-metrics "crank-rotations") => 45618 
               (get db-metrics "calories-burnt") => 3006.1)))


