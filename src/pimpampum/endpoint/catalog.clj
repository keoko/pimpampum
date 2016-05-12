(ns pimpampum.endpoint.catalog
  (:require [ring.util.response :refer [response]]
            [compojure.core :refer :all]
            [pimpampum.component.repo :as r]))


(defn resp [body]
  {:headers {"Content-Type" "text/html"}
   :body body})

(defn catalog-endpoint
  [{repo :repo}]
  (context "/catalog" []
           (GET "/:id" [id] (resp (r/find-item repo id)))
           (GET "/" [] (resp (r/find-all repo)))))
