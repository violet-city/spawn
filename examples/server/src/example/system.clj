(ns example.system
  (:require [clojure.tools.logging :as log]))

(defn start-server
  [node]
  (log/info "starting server"))

(def server
  {:gx/start {:gx/processor start-server}})
