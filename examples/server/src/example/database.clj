(ns example.database
  (:require [clojure.tools.logging :as log]))

(defn start
  [node]
  (log/info "starting database")
  {:name :database})

(def graph
  {::component {:gx/component {:gx/start {:gx/processor start}}}})
