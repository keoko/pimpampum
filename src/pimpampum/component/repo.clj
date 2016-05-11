(ns pimpampum.component.repo
  (:require [clojure.java.jdbc :as j]))

(defprotocol CatalogRepository
  (find-all [this])
  (add-item [this item])
  (change-item [this item new-item])
  (remove-item [this item]))

(defrecord CatalogRepoComponent [db]
  CatalogRepository
  (find-all [this]
    (j/query (:spec db) ["SELECT * FROM catalog"]))
  (add-item [this item]
    (j/insert! (:spec db) :catalog {:id 11}))
  (change-item [this item new-item]
    (j/update! (:spec db) :catalog {:id 13} ["id = ?" 11]))
  (remove-item [this item]
    (j/delete! (:spec db) :catalog ["id = ?" 13]))

)

(defn make-component []
  (->CatalogRepoComponent {}))


