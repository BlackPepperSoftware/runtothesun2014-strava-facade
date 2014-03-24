(ns ridetothesun2014-strava-facade.test.strava
  (:use [midje.sweet]
        ridetothesun2014-strava-facade.db
        ridetothesun2014-strava-facade.strava)
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]))

(facts "Correctly retrieves and persists strava data"
       (fact "Facade retreives strava metrics correctly"
             (let [from-strava (strava-metrics)]
               (get from-strava :metres-climbed) => (roughly 1752) 
               (get from-strava :metres-ridden) => (roughly 93800)
               (get from-strava :crank-rotations) => (roughly 45618) 
               (get from-strava :calories-burnt) => (roughly 3006))))

