(ns pimpampum.component.repo
  (:require [clojure.java.jdbc :as j]))

(defprotocol CatalogRepository
  (find-all [this]))

(defrecord CatalogRepoComponent [db]
  CatalogRepository
  (find-all [this]
    (j/query (:spec db) ["SELECT * FROM catalog"])))

(defn make-component []
  (->CatalogRepoComponent {}))


