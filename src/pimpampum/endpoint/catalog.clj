(ns pimpampum.endpoint.catalog
  (:require [compojure.core :refer :all]))


(defn catalog-endpoint
  [conf]
  (context "/catalog" []
           (GET ["/"] [] {:status 200
                          :headers {"Content-Type" "text/html"}
                          :body "Catalog endpoint!"})))
