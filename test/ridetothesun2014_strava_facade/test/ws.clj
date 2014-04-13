(ns ridetothesun2014-strava-facade.test.ws
  (:use [midje.sweet]
        ridetothesun2014-strava-facade.db
        ridetothesun2014-strava-facade.gpx
        ridetothesun2014-strava-facade.strava
        ridetothesun2014-strava-facade.ws)
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]))

(def example-metrics 
  {:metres-ridden 32424 
   :metres-climbed 1234.455 
   :crank-rotations 200000.42 
   :calories-burnt 264000.42})

(defn request [resource web-app & params]
  (web-app {:request-method :get :uri resource :params (first params)}))

(facts "It converts, rounds down and returns metrics from the database"
  (update-db example-metrics "fake-gpx")
  (let [resp (request "/metrics" app)
        status (:status resp)
        headers (:headers resp)
        json-content (json/read-str (:body resp))]
       (fact "and returns content type"
               (get headers "Content-Type") => "application/json")
       (fact "returns 200 ok" status => 200)
       (fact "metres-ridden to rounded miles" 
             (get json-content "miles-ridden") => 20)
       (fact "metres-climbed rounded" 
             (get json-content "metres-climbed") => 1234)
       (fact "crank-rotations * 4 and rounded" 
             (get json-content "crank-rotations") => 800001)
       (fact "calories-burnt * 4 and rounded down" 
             (get json-content "calories-burnt") => 1056001 )
       (fact "calories_burnt * 4 / 400 and rounded"
             (get json-content "croissants-equivalent") => 2640)))

;;       (fact "The facade returns gpx"
 ;;            (update-db example-metrics "fake-gpx")
  ;;           (let [resp (request "/gpx" app)
   ;;                status (:status resp)
    ;;               headers (:headers resp)
     ;;              gpx-content (:body resp)]
      ;;         status => 200
       ;;        (get headers "Content-Type") => "application/gpx, application/x-gpx+xml, application/xml-gpx"
        ;;       gpx-content => "fake-gpx")))
