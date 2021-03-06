(ns ctia.stores.atom.indicator
  (:require [ctia.lib.pagination :refer [list-response-schema]]
            [ctia.lib.schema :as ls]
            [ctia.properties :refer [properties]]
            [ctim.domain.id :as id]
            [ctim.schemas
             [indicator :refer [StoredIndicator]]
             [judgement :refer [StoredJudgement]]]
            [ctia.stores.atom.common :as mc]
            [schema.core :as s]))

(def handle-create-indicator (mc/create-handler-from-realized StoredIndicator))
(def handle-read-indicator (mc/read-handler StoredIndicator))
(def handle-update-indicator (mc/update-handler-from-realized StoredIndicator))
(def handle-delete-indicator (mc/delete-handler StoredIndicator))
(def handle-list-indicators (mc/list-handler StoredIndicator))

(s/defn handle-list-indicators-by-judgements :- (list-response-schema StoredIndicator)
  [indicator-state :- (ls/atom {s/Str StoredIndicator})
   judgements :- [StoredJudgement]
   params]
  (let [indicator-ids (some->> (map :indicators judgements)
                               (mapcat #(map :indicator_id %))
                               (map id/str->short-id)
                               set)]
    (handle-list-indicators indicator-state {:id indicator-ids} params)))
