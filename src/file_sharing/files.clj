(ns file-sharing.files
  (:require [clojure.java.io :as io]
    [ring.util.response :as rsp]
    [file-sharing.db :as db]))

(defn wrap-file-save [handler]
  (fn [request]
    (let [response (handler request)]
      (println "############")
      (println response)
      (println "############")
      (if-let [file (get-in response [:body :params :fileToUpload])]
        (with-open [is (io/input-stream (:bytes file))
                    os (io/output-stream (io/file (str "resources/files/"
                                                    (db/new-file! {:filename (:filename file)} ))))]
          (io/copy (:bytes file) os)
          (rsp/response "OK"))
        response))))

;This is a middleware that can change the filename for downloading
;get-name is a function to match File.getPath() and return the matched filename
;if the the file need to be changed its filename, otherwise, it'll return nil
;and the function change-name change the source filename to target filename
(defn wrap-content-disposition [handler get-name change-name]
  (fn [request]
    (let [response (handler request)]
      (if-let [id (and (->> response :body (instance? java.io.File))
                       (->> response :body .getPath get-name))] 
        (assoc response :headers (merge (:headers response)
					                         {"Content-Disposition" (str "attachment; filename=\"" (change-name id) \")
					                          "Content-Type" "application/octet-stream"}))
        response))))