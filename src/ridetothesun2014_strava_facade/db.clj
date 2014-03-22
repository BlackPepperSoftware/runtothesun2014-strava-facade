(ns ridetothesun2014-strava-facade.db
  (:use [taoensso.carmine :as car :refer (wcar)]
        [clojure.data.json :as json :only [read-str write-str]]))

(def server1-conn {:pool {} 
                   :spec {:uri (get (System/getenv) "REDISCLOUD_URL")}})
(def calories-per-croissant 400) 

(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn metrics []
  (let [db-metrics (json/read-str(wcar* (car/get "metrics")))]
    (assoc db-metrics 
           "croissants-equivalent" 
           (int(/ (get db-metrics "calories-burnt") calories-per-croissant)))))

(defn gpx [](wcar* (car/get "gpx")))

(defn round-metrics [m & args]
   (reduce (fn [r [k v]] (assoc r k (apply int v args))) {} m))

(defn update [metrics gpx]
  (wcar* 
    (car/set "metrics" (write-str (round-metrics metrics)))
    (car/set "gpx" gpx)
    (car/set "last-updated" (quot (System/currentTimeMillis) 1000))))

(defn last-updated []
  (read-string(wcar* (car/get "last-updated"))))

