(ns pimpampum.config
  (:require [environ.core :refer [env]]))

(def defaults
  ^:displace {:http {:port 3000}})

(def environ
  {:http {:port (some-> env :port Integer.)}
   :db   {:uri  (env :database-url)}
   :rabbitmq-connection {:host     "localhost"
                         :port     5672
                         :vhost    "/"
                         :username "guest"
                         :password "guest"}
   :rabbitmq-worker {:exchange-name "events-worker"}})
