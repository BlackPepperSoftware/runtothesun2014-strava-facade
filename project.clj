(defproject ridetothesun2014-strava-facade "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [midje "1.6.3"]
                 [ring "1.2.2"]
                 [ring/ring-json "0.3.0"]
                 [compojure "1.1.6"]
                 [org.clojure/data.json "0.2.4"]
                 [com.taoensso/carmine "2.4.5"]
                 [clj-http "0.9.1"]
                 [clj-time "0.6.0"]
                 [com.gfredericks/vcr-clj "0.3.4-alpha1"]]
  :plugins [[lein-midje "3.0.0"]])
