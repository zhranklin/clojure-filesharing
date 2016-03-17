(ns file-sharing.urlhandlers
  (:use [compojure.core :only [GET POST defroutes]]
    [compojure.route :only [not-found]]
    [ring.util.response :only [response]]
    [file-sharing.db :only [get-filename get-files]]))

(defroutes app-routes
  (GET "/" request (response request))
  (GET "/upload" request (response {:template "upload.html",
                                    :model {}}))
  (POST "/upload" request (response request))
  (GET "/filelist" request 
       (response {:template "files.html",
                  :model {:name "Zhranklin",
                          :files (map #(assoc % :filename
                                         (get-filename (:id %)))
                                      (get-files)) }})))

