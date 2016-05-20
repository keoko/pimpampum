(ns pimpampum.component.rabbitmq-producer
  (:require [com.stuartsierra.component :as component]
            [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.consumers :as lc]
            [langohr.basic     :as lb]))


(def ^{:const true} default-exchange-name "")

(def qname "events-worker")

(defprotocol Producer
  (publish! [this message]))

(defrecord RabbitMQProducer [mq-conn exchange-name]
  component/Lifecycle
  (start [component]
    (let [ch (lch/open (:connection mq-conn))]
      (lq/declare ch qname {:exclusive false :auto-delete false :durable true})
      component))
  (stop [component]
    component)

  Producer
  (publish! [component message]
    (let [ch (lch/open (:connection mq-conn))]
      (lb/publish ch default-exchange-name qname
                  message {:content-type "text/plain" :type "greetings.hi"}))))


(defn new-rabbitmq-producer [exchange-name]
  (map->RabbitMQProducer {:exchange-name exchange-name}))
