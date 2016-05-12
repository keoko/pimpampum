(ns pimpampum.component.logger
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre.appenders.core :as appenders]
            [taoensso.timbre :as timbre
             :refer (log  trace  debug  info  warn  error  fatal  report
                          logf tracef debugf infof warnf errorf fatalf
                          reportf spy get-env log-env)]))


(def log-filename "/tmp/my-file.log")

(defn log-output-format
  ([data] 
   (log-output-format nil data))
  ([_ {:keys [timestamp_ msg_]}]
   (str @timestamp_ ": " (force  msg_))))


(defrecord LoggerComponent [config]
  component/Lifecycle
  (start [component]
    (timbre/merge-config!
     {:level :trace 
      :appenders 
      {:println {:enabled? true}
       :spit (appenders/spit-appender 
              {:fname log-filename
               :min-level :info
               :output-format log-output-format
               :enabled? true})}}))

  (stop [component]
    (timbre/merge-config!
     {:level :fatal
      :appenders 
      {:println {:enabled? false}
       :spit {:enabled? false}}})))

(defn make-component
  []
  (map->LoggerComponent {}))
