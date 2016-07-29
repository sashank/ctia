(ns ctia.test-helpers.benchmark
  (:require [ctia.init :refer [start-ctia!]]
            [ctia.flows.hooks :as hooks]
            [ctia.http.server :as http-server]
            [ctia.shutdown :as shutdown]
            [ctia.test-helpers
             [atom :as at-helpers]
             [core :as helpers]
             [es :as esh]]))

(defn setup-ctia! [fixture]
  (let [http-port (helpers/available-port)]
    (println "Default: Launch CTIA on port" http-port)
    (fixture (fn [] (helpers/fixture-properties:clean
                    #(helpers/with-properties ["ctia.store.es.default.refresh" false
                                               "ctia.http.enabled" true
                                               "ctia.http.port" http-port
                                               "ctia.http.show.port" http-port]
                       (start-ctia! :join? false)))))
    http-port))


(defn setup-ctia-atom-store! []
  (setup-ctia! at-helpers/fixture-properties:atom-memory-store))

(defn setup-ctia-es-store! []
  (setup-ctia! esh/fixture-properties:es-store))

(defn setup-ctia-es-store-native! []
  (setup-ctia! esh/fixture-properties:es-store-native))

(defn cleanup-ctia! [_]
  (shutdown/shutdown-ctia!))
