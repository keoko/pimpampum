(ns pimpampum.component.repo
  (:require [clojure.java.jdbc :as j]))

(defprotocol CatalogRepository
  (find-all [this])
  (find-item [this id])
  (add-item! [this item])
  (change-item! [this item new-item])
  (remove-item [this item]))

(defrecord CatalogRepoComponent [db]
  CatalogRepository
  (find-all [this]
    (j/query (:spec db) ["SELECT * FROM catalog"]))
  (find-item [this id]
    (j/query (:spec db) ["SELECT * FROM catalog WHERE id = ?" id]))
  (add-item! [this item]
    (j/insert! (:spec db) :catalog {:id item}))
  (change-item! [this item new-item]
    (j/update! (:spec db) :catalog {:id new-item} ["id = ?" item]))
  (remove-item [this item]
    (j/delete! (:spec db) :catalog ["id = ?" 13]))

)

(defn make-component []
  (->CatalogRepoComponent {}))


