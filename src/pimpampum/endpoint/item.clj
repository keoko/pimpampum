(ns pimpampum.endpoint.item
  (:require [ring.util.response :refer [response]]
            [compojure.core :refer :all]
            [pimpampum.component.repo :as r]))


(defn resp [body]
  {:headers {"Content-Type" "text/html"}
   :body body})

(defn item-endpoint
  [{repo :repo}]
  (context "/item" []
           (GET "/:id" [id] (resp (r/find-item repo id)))
           (GET "/" [] (resp (r/find-all repo)))
           (POST "/:id" [id] (resp (r/add-item! repo id)))
           (PUT "/:id/:new-id" [id new-id] (resp (r/change-item! repo id new-id)))
           (DELETE "/:id" [id] (resp (r/remove-item! repo id)))))
