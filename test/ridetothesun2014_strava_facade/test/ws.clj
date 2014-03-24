(ns ridetothesun2014-strava-facade.test.ws
  (:use [midje.sweet]
        ridetothesun2014-strava-facade.db
        ridetothesun2014-strava-facade.gpx
        ridetothesun2014-strava-facade.strava
        ridetothesun2014-strava-facade.ws)
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]))

(def example-metrics 
  {:metres-ridden 160900 
   :metres-climbed 1234 
   :crank-rotations 200000 
   :calories-burnt 264000})

(defn request [resource web-app & params]
  (web-app {:request-method :get :uri resource :params (first params)}))

(facts "It converts and returns metrics from the database"
       (fact "The facade converts and returns metrics"
             (update-db example-metrics "fake-gpx")
             (let [resp (request "/metrics" app)
                   status (:status resp)
                   headers (:headers resp)
                   json-content (json/read-str (:body resp))]
               status => 200
               (get headers "Content-Type") => "application/json"
               (get json-content "miles-ridden") => 100 
               (get json-content "metres-climbed") => 1234 
               (get json-content "crank-rotations") => 200000 
               (get json-content "calories-burnt") => 264000 
               (get json-content "croissants-equivalent") => 660)))

;;       (fact "The facade returns gpx"
 ;;            (update-db example-metrics "fake-gpx")
  ;;           (let [resp (request "/gpx" app)
   ;;                status (:status resp)
    ;;               headers (:headers resp)
     ;;              gpx-content (:body resp)]
      ;;         status => 200
       ;;        (get headers "Content-Type") => "application/gpx, application/x-gpx+xml, application/xml-gpx"
        ;;       gpx-content => "fake-gpx")))
