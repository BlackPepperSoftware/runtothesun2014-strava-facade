(ns ridetothesun2014-strava-facade.gpx
  (:require [clj-time.core :as t]))

(def xmldec "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>")
(def opengpx "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" \n
             creator=\"MapSource 6.15.5\" \n
             version=\"1.1\" \n
             xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n
             xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 \n
             http://www.topografix.com/GPX/1/1/gpx.xsd\">\n")
(def opentrk "<trk>")
(def trackname "<name>Run To The Sun 2014</name>\n")
(def opentrkseg "<trkseg>\n")
(def closetrkseg "</trkseg>\n")
(def closetrk "</trk>")
(def closegpx "</gpx>")


(defn points-to-gpx [points]
  (loop [points-to-process points trkpts "" seconds 0]
    (if (seq points-to-process)
      (let [current-point (first points-to-process)]
        (recur (rest points-to-process) 
               (str trkpts 
                    "<trkpt lat=\"" 
                    (first current-point)  
                    "\" lon=\""
                    (second current-point)  
                    "\"><time>"(t/date-time seconds)"</time></trkpt>\n" )(inc seconds))) (str 
                                xmldec
                                opengpx
                                opentrk
                                trackname 
                                opentrkseg
                                trkpts
                                closetrkseg
                                closetrk
                                closegpx))))
