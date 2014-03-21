(ns ridetothesun2014-strava-facade.ws
  (:use [ring.adapter.jetty]
        [ring.middleware.json]
        [ring.util.response]
        ridetothesun2014-strava-facade.db))


(defn handler [req]
  (ring.util.response/content-type (response  (metrics))
                                   "application/json; charset=utf-8"))

(def app
  (wrap-json-response handler))

(defn -main [port] (run-jetty app  {:port (Integer. port) :join? false}))
