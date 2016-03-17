(ns file-sharing.db
  (:use
    [korma.db]
    [korma.core]
    [clojure.string :only [replace]]))

(defdb korma-db (if (nil? (System/getenv "SERVER_SOFTWARE"))
                  (mysql {:db "test",
                        :host "localhost",
                        :port 3306,
                        :user "root",
                        :password ""})
                  (mysql {:db "dCloHgbmCYQJZjYdJijb",
                        :host "sqld.duapp.com",
                        :port 4050,
                        :user "183360e5b43745c1b7f1b43a193b636d",
                        :password "d3e9e854ccff423bbd184d39f3bff186"})))

(declare files)
(defentity files)

(defn get-file [id]
  (let [fs (select files (where {:id id}))]
    (if (empty? fs)
      nil
      (first fs))))

(defn get-files []
  (select files (order :id :asc)))

(defn new-file! [file]
  (println "new file:" (:filename file))
  (let [uuid (replace (->> "uuid" sqlfn fields
                            (select "courses")
                            first vals first)
                 #"-" "")]
    (insert files
      (values {:filename (.getBytes (:filename file)) :id uuid}))
    uuid))

(defn get-filename [id]
  (-> id get-file :filename (java.lang.String. "utf-8")))

(defn delete-file! [id]
  (println "delete file" id)
  (delete files (where {:id id}))
  ;TODO delete the file
  )