(ns ridetothesun2014-strava-facade.test.acceptance
  (:use [midje.sweet]
        ridetothesun2014-strava-facade.core)
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]))

(facts "Ride to the Sun 2014 Strava Facade acceptance tests"
  (fact "The facade returns metrics"
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
    (get json-content "croissants-equivalent") => 660)))
