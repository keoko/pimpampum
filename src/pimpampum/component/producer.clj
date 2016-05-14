(ns pimpampum.component.producer
  (:require [com.stuartsierra.component :as component]
            [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.consumers :as lc]
            [langohr.basic     :as lb]))


;; todo - experimenting with rabbitmq
(def ^{:const true} default-exchange-name "")

(def qname "langohr.examples.hello-world")

(def amqp-url  "amqp://guest:guest@localhost:5672")

(def default-exchange-name "")

(defn message-handler
  [ch {:keys [content-type delivery-tag] :as meta} ^bytes payload]
  (println
    (format "[consumer] Received a message: %s, delivery tag: %d, content type: %s"
    (String. payload "UTF-8") delivery-tag content-type)))


(defn publish-message [message]
  (let [conn  (rmq/connect)
        ch    (lch/open conn)]
    (lq/declare ch qname {:exclusive false :auto-delete true})
    #_(lc/subscribe ch qname message-handler {:auto-ack true})
    (lb/publish ch default-exchange-name qname
                message {:content-type "text/plain" :type "greetings.hi"})
    #_(rmq/close ch)
    #_(rmq/close conn)))


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
