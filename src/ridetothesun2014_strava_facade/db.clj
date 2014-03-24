(ns ridetothesun2014-strava-facade.db
  (:use [taoensso.carmine :as car :refer (wcar)]
        [clojure.data.json :as json :only [read-str write-str]]))

(def server1-conn {:pool {} 
                   :spec {:uri (get (System/getenv) "REDISCLOUD_URL")}})

(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn db-metrics []
  (json/read-str(wcar* (car/get "metrics"))))

(defn db-gpx [](wcar* (car/get "gpx")))

(defn update-db [metrics gpx]
  (wcar* 
    (car/set "metrics" (write-str metrics))
    (car/set "gpx" gpx)
    (car/set "last-updated" (quot (System/currentTimeMillis) 1000))))

(defn last-updated []
  (read-string(wcar* (car/get "last-updated"))))

