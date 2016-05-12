(ns pimpampum.component.repo
  (:require [clojure.java.jdbc :as j]))

(defprotocol ItemRepository
  (find-all [this])
  (find-item [this id])
  (add-item! [this item])
  (change-item! [this item new-item])
  (remove-item! [this item]))

(defrecord ItemRepoComponent [db]
  ItemRepository
  (find-all [this]
    (j/query (:spec db) ["SELECT * FROM item"]))
  (find-item [this id]
    (j/query (:spec db) ["SELECT * FROM item WHERE id = ?" id]))
  (add-item! [this item]
    (j/insert! (:spec db) :item {:id item}))
  (change-item! [this item new-item]
    (j/update! (:spec db) :item {:id new-item} ["id = ?" item]))
  (remove-item! [this item]
    (j/delete! (:spec db) :item ["id = ?" item]))

)

(defn make-component []
  (->ItemRepoComponent {}))


