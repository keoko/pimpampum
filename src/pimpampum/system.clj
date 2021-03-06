(ns pimpampum.system
  (:require [com.stuartsierra.component :as component]
            [duct.component.endpoint :refer [endpoint-component]]
            [duct.component.handler :refer [handler-component]]
            [duct.component.hikaricp :refer [hikaricp]]
            [duct.component.ragtime :refer [ragtime]]
            [duct.middleware.not-found :refer [wrap-not-found]]
            [meta-merge.core :refer [meta-merge]]
            [ring.component.jetty :refer [jetty-server]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [system.components.rabbitmq :refer [new-rabbit-mq]]
            [pimpampum.endpoint.item :refer [item-endpoint]]
            [pimpampum.component.logger :as logger]
            [pimpampum.component.repo :as repo]
            [pimpampum.component.producer :as producer]
            [pimpampum.component.consumer :as consumer]
            [pimpampum.component.rabbitmq-connection :as mq-conn]
            [pimpampum.component.rabbitmq-producer :as mq-producer]
            [pimpampum.component.rabbitmq-worker :as mq-worker]))

(def base-config
  {:app {:middleware [[wrap-restful-format :formats [:json :edn :yaml :msgpack :msgpack-kw :yaml-in-html :transit-json :transit-msgpack]]
                      [wrap-not-found :not-found]
                      [wrap-defaults :defaults]]
         :not-found  "Resource Not Found"
         :defaults   (meta-merge api-defaults {})}
   :ragtime {:resource-path "pimpampum/migrations"}})

(defn new-system [config]
  (let [config (meta-merge base-config config)]
    (-> (component/system-map
         :app  (handler-component (:app config))
         :http (jetty-server (:http config))
         :db   (hikaricp (:db config))
         :ragtime (ragtime (:ragtime config))
         :item-endpoint (endpoint-component item-endpoint)
         :logger (logger/make-component)
         :repo (repo/make-component)
;         :consumer (consumer/make-component)
         :mq-conn (mq-conn/new-rabbitmq-connection (:rabbitmq-connection config))
         :mq-producer         (mq-producer/new-rabbitmq-producer          "events-worker")
         :mq-worker         (mq-worker/new-rabbitmq-worker           (get-in config [:rabbitmq-worker :exchange-name])          "events-worker"                                     mq-worker/handler)
         )
        (component/system-using
         {
          :http [:app]
          :app  [:item-endpoint]
          :ragtime [:db]
          :repo [:db]
          :mq-producer [:mq-conn]
          :mq-worker [:mq-conn]
          :item-endpoint [:repo :mq-producer]
          }))))
