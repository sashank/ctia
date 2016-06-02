(ns ctia.http.routes.incident
  (:require
    [compojure.api.sweet :refer :all]
    [ctia.domain.entities :refer [realize-incident]]
    [ctia.flows.crud :as flows]
    [ctia.store :refer :all]
    [ctim.schemas.incident :refer [NewIncident StoredIncident]]
    [ring.util.http-response :refer :all]
    [schema.core :as s]))

(defroutes incident-routes

  (context "/incident" []
    :tags ["Incident"]
    (POST "/" []
      :return StoredIncident
      :body [incident NewIncident {:description "a new incident"}]
      :summary "Adds a new Incident"
      :header-params [api_key :- (s/maybe s/Str)]
      :capabilities :create-incident
      :identity identity
      (ok (flows/create-flow :realize-fn realize-incident
                             :store-fn #(write-store :incident
                                                     (fn [s] (create-incident s %)))
                             :entity-type :incident
                             :identity identity
                             :entity incident)))
    (PUT "/:id" []
      :return StoredIncident
      :body [incident NewIncident {:description "an updated incident"}]
      :summary "Updates an Incident"
      :path-params [id :- s/Str]
      :header-params [api_key :- (s/maybe s/Str)]
      :capabilities :create-incident
      :identity identity
      (ok (flows/update-flow :get-fn #(read-store :incident
                                                  (fn [s] (read-incident s %)))
                             :realize-fn realize-incident
                             :update-fn #(write-store :incident
                                                      (fn [s] (update-incident s (:id %) %)))
                             :entity-type :incident
                             :entity-id id
                             :identity identity
                             :entity incident)))
    (GET "/:id" []
      :return (s/maybe StoredIncident)
      :summary "Gets an Incident by ID"
      :path-params [id :- s/Str]
      :header-params [api_key :- (s/maybe s/Str)]
      :capabilities :read-incident
      (if-let [d (read-store :incident (fn [s] (read-incident s id)))]
        (ok d)
        (not-found)))
    (DELETE "/:id" []
      :no-doc true
      :path-params [id :- s/Str]
      :summary "Deletes an Incident"
      :header-params [api_key :- (s/maybe s/Str)]
      :capabilities :delete-incident
      :identity identity
      (if (flows/delete-flow :get-fn #(read-store :incident
                                                  (fn [s] (read-incident s %)))
                             :delete-fn #(write-store :incident
                                                      (fn [s] (delete-incident s %)))
                             :entity-type :incident
                             :entity-id id
                             :identity identity)
        (no-content)
        (not-found)))))
