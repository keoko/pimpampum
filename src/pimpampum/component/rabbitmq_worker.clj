(ns pimpampum.component.rabbitmq-worker
  (:require     
   [com.stuartsierra.component :as component]
    [langohr.channel :as lch]
    [langohr.consumers :as lc]
    [langohr.core :as rmq]
    [langohr.queue :as lq]
    [taoensso.timbre :as log]))

(defrecord RabbitMQWorker [mq-conn
                           channel exchange-name queue-name subscriber]
  component/Lifecycle

  ; @todo avoid use of auto-ack on subscribe
  (start [component]
    (log/debug "Starting RabbitMQ worker...")
    (println "connection... ")
    (let [channel (lch/open (:connection mq-conn))]
      (lq/declare channel queue-name {:durable true :exclusive false :auto-delete false})
      ;;(lq/bind channel queue-name exchange-name {:routing-key "#"})
      (lc/subscribe channel queue-name (partial subscriber component) {:auto-ack true})
      (assoc component :channel channel)))

  (stop [component]
    (log/debug "Stopping RabbitMQ worker...")
    (when channel (rmq/close channel))
    (assoc component :channel nil)))

(defn new-rabbitmq-worker [exchange-name queue-name subscriber]
  (map->RabbitMQWorker {:exchange-name exchange-name
                        :queue-name    queue-name
                        :subscriber    subscriber}))


(defn handler [{{connection :connection} :database-backup} _ _ ^bytes payload]
  (let [domain-event (String. payload "UTF-8")]
    (println "handling event ..." domain-event)))
