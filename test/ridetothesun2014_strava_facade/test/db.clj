(ns ridetothesun2014-strava-facade.test.db
  (:use [midje.sweet]
        ridetothesun2014-strava-facade.db))

(facts "It persists data from strava and allows retreival"
       (fact "It returns json representation of metrics"
             (update-db {:metres-climbed 123 :metres-ridden 456 :crank-rotations 789 :calories-burnt 123456} "fake-gpx")
             (db-metrics) => 
             {"calories-burnt" 123456 "crank-rotations" 789 "metres-climbed" 123"metres-ridden" 456})
       (fact "It returns unchanged gpx data"
             (update-db {} "fake-gpx-data")
             (db-gpx) => "fake-gpx-data"))

