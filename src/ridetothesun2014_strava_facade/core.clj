(ns ridetothesun2014-strava-facade.core
  (:use [ring.adapter.jetty]
        [ring.middleware.json]
        [ring.util.response]
        [taoensso.carmine :as car :refer (wcar)]
        [clojure.data.json :as json :only [read-str]]))

(def server1-conn {:pool {} 
                   :spec {:uri (get (System/getenv) "REDISCLOUD_URL")}})
(def calories-per-croissant 400) 

(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn metrics []
  (let [db-metrics (json/read-str(wcar* (car/get "metrics")))]
    (assoc db-metrics 
           "croissants-equivalent" 
           (/ (get db-metrics "calories-burnt") calories-per-croissant))))

(defn handler [req]
  (ring.util.response/content-type (response  (metrics))
                                   "application/json; charset=utf-8"))

(def app
  (wrap-json-response handler))

(defn -main [port] (run-jetty app  {:port (Integer. port) :join? false}))
