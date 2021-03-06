(ns ctia.http.routes.event
  (:require [schema.core :as s]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [ctia.events :refer [recent-events]]
            [ctim.events.schemas :refer [ModelEventBase]]))

(defroutes event-routes
  (context "/events" []
    :tags ["Events"]
    (GET "/log" []
      :return [ModelEventBase]
      :summary "Recent Event log"
      :capabilities :developer
      (ok (recent-events)))))
