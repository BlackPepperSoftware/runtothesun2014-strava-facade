(ns ridetothesun2014-strava-facade.strava
  (:require [clj-http.client :as client]
            [clojure.data.json :as json :only [read-str]]))

(def base-url "https://www.strava.com/api/v3/")

(def my-key (get (System/getenv) "STRAVA_ACCESS_TOKEN"))

(def empty-metrics
  {:miles-done 0
   :metres-climbed 0
   :crank-rotations 0
   :calories-burnt 0})

(defn strava-json-resource [url]
  (json/read-str 
    (:body 
      (client/get url {:headers 
                       {:Authorization (str "Bearer " my-key)} 
                       :query-params {:resolution "low"}}))))

(defn activity-ids [] 
  (map #(get % "id") (strava-json-resource (str base-url "athlete/activities"))))

(defn calculate-crank-rotations [elapsed-seconds rpm]
  (int (float(* (if (nil? rpm) 90 rpm)(/ elapsed-seconds 60)))))

(defn activity-details [id] 
  (let [activity-map (strava-json-resource (str base-url "activities/" id))]
    {:miles-done (get activity-map "distance")
     :metres-climbed (get activity-map "total_elevation_gain")
     :calories-burnt (get activity-map "calories")
     :crank-rotations 
     (calculate-crank-rotations 
       (get activity-map "moving_time") 
       (get activity-map "average_cadence"))})) 

(defn activity-stream [id]
  (get (first (strava-json-resource (str base-url "activities/" id "/streams/latlng"))) "data"))

(defn strava-metrics []
  (loop [ids-to-process (activity-ids)  metrics empty-metrics]
    (if (seq ids-to-process)
      (let [id-to-process    (first ids-to-process)
            current-activity (activity-details id-to-process)]
        (recur 
          (rest ids-to-process) 
          (merge-with + metrics current-activity))) metrics )))

(defn strava-points []  
  (loop [ids-to-process (reverse(activity-ids)) points []]
    (if (seq ids-to-process)
      (let [id-to-process    (first ids-to-process)
            current-points (activity-stream id-to-process)]
        (recur 
          (rest ids-to-process) 
          (concat points current-points))) points )))
