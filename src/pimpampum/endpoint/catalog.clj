(ns pimpampum.endpoint.catalog
  (:require [ring.util.response :refer [response]]
            [compojure.core :refer :all]
            [pimpampum.component.repo :as r]))


(defn catalog-endpoint
  [{repo :repo}]
  (context "/catalog" []
           (do
             (GET ["/"] [] (response (r/find-all repo))))))
