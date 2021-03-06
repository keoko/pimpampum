(ns pimpampum.component.producer
  (:require [com.stuartsierra.component :as component]
            [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.consumers :as lc]
            [langohr.basic     :as lb]))


(def ^{:const true} default-exchange-name "")

(def qname "langohr.examples.hello-world")

(defprotocol Producer
  (publish! [this message]))

(defrecord ProducerComponent [rabbitmq]
  component/Lifecycle
  (start [component]
    (let [ch (:ch rabbitmq)]
      (lq/declare ch qname {:exclusive false :auto-delete true})
      component))
  (stop [component]
    component)
  Producer
  (publish! [component message]
    (let [ch (:ch rabbitmq)]
      (lb/publish ch default-exchange-name qname
                  message {:content-type "text/plain" :type "greetings.hi"}))))


(defn make-component []
  (map->ProducerComponent {}))
