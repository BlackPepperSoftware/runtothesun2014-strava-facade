(ns ridetothesun2014-strava-facade.updater
  (:use ridetothesun2014-strava-facade.db
        ridetothesun2014-strava-facade.gpx
        ridetothesun2014-strava-facade.strava))


(defn -main [& args] (update (strava-metrics) (points-to-gpx(strava-points))))
