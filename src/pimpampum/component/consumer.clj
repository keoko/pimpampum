(ns pimpampum.component.consumer
  (:require [com.stuartsierra.component :as component]
            [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.consumers :as lc]
            [langohr.basic     :as lb]))


(def qname "langohr.examples.hello-world")

(defn message-handler
  [ch {:keys [content-type delivery-tag] :as meta} ^bytes payload]
  (println
    (format "[consumer] Received a message: %s, delivery tag: %d, content type: %s"
    (String. payload "UTF-8") delivery-tag content-type)))


(defrecord ConsumerComponent [consumer-rabbitmq]
  component/Lifecycle
  (start [component]
    (let [ch (:ch consumer-rabbitmq)]
      (lc/subscribe ch qname message-handler {:auto-ack true})
      component))
  
  (stop [component]
    component))


(defn make-component []
  (map->ConsumerComponent {}))
