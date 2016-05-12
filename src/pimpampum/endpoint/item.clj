(ns pimpampum.endpoint.item
  (:require [ring.util.response :refer [response]]
            [compojure.core :refer :all]
            [pimpampum.component.repo :as r]))


(defn resp [body]
  {:body body})

(defn item-endpoint
  [{repo :repo}]
  (context "/item" []
           (GET "/:id" [id] (resp (r/find-item repo id)))
           (GET "/" [] (resp (r/find-all repo)))
           (POST "/" req (let [id (get (:form-params req) "id")
                               sku (get (:form-params req) "sku")
                               name (get (:form-params req) "name")
                               item {:id id :sku sku :name name}]
                           (resp (r/add-item! repo item))))
           (PUT "/:id" req (let [id (get (:form-params req) "id")
                               sku (get (:form-params req) "sku")
                               name (get (:form-params req) "name")
                               item {:id id :sku sku :name name}]
                           (resp (r/change-item! repo id item))))
           (DELETE "/:id" [id] (resp (r/remove-item! repo id)))))
