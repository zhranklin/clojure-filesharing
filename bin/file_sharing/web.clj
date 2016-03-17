(ns file-sharing.web
  (:require
    [ring.adapter.jetty :as jetty]
    [ring.middleware.cookies :as cookies]
    [ring.middleware.params :as params]
    [ring.middleware.keyword-params :as keyword-params]
    [ring.middleware.file :as file]
    [ring.middleware.resource :as resource]
    [ring.middleware.stacktrace :as stacktrace]
    [ring.middleware.multipart-params :as multi]
    [ring.middleware.multipart-params.byte-array :as byte-arr]
    [file-sharing.urlhandlers :as urlhandlers]
    [file-sharing.files :as files]
    [file-sharing.templating :as templating]
    [file-sharing.db :as db]))
(def app
  (-> urlhandlers/app-routes
      (resource/wrap-resource (clojure.java.io/resource "resources")) ;; static resource
      (files/wrap-content-disposition
        #(second (re-find #".*resources/files/(.{32})" %))
        #(java.lang.String. (-> % db/get-file :filename) "ISO8859-1"))
      templating/wrap-template-response  ;; render template
      stacktrace/wrap-stacktrace-web     ;; wrap-stacktrace-log
      keyword-params/wrap-keyword-params ;; convert parameter name to keyword
      params/wrap-params   ;; query string and url-encoded form
      (multi/wrap-multipart-params {:store (byte-arr/byte-array-store)}) 
      files/wrap-file-save
  ))


;; start web server
(defn start-server []
  (jetty/run-jetty app {:host "172.27.35.7",
                        :port 3000
                        }))

(defn -main [& args]
  (if (= "start" (first args))
    (start-server)))
(defn run-by-host [hostname port]
  (jetty/run-jetty app {:host hostname
                        :port port
                        }))
