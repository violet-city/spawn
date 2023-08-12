(ns city.violet.spawn
  (:gen-class)
  (:require [k16.gx.beta.system :as gx.system]
            [k16.gx.beta.core :as gx]
            [clojure.tools.logging :as log]
            [clojure.edn :as edn]))

(defn load-layer!
  [layer-sym]
  @(requiring-resolve layer-sym))

(defn merge-layers
  [graph layer]
  (merge graph layer))

(def read-config!
  (comp
   (partial edn/read-string {:readers {}})
   (partial slurp "spawn.edn")))

(defn load-system!
  []
  (gx.system/register!
   ::system
   {:context gx/default-context
    :graph   (let [config (read-config!)]
               (reduce merge-layers
                       (:graph config)
                       (map load-layer! (:layers config))))}))

(defn start!
  []
  (gx.system/signal! ::system :gx/start))

(defn stop!
  []
  (gx.system/signal! ::system :gx/stop))

(defn failures
  []
  (gx.system/failures-humanized ::system))

(defn reload!
  []
  (stop!)
  (load-system!)
  (start!))

(defn -main
  [& _]
  (load-system!)
  (doto (Runtime/getRuntime)
    (.addShutdownHook (Thread. #(stop!))))
  (start!)
  (when-let [failures (seq (failures))]
    (doseq [failure failures]
      (log/error failure))
    (System/exit 1))
  @(promise))
