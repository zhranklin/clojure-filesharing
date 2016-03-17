(defproject file-sharing "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [mysql/mysql-connector-java "5.1.25"]
                 [korma "0.4.0"]
                 [clj-http "2.0.0"]
                 [ring "1.4.0"]
                 [compojure "1.4.0"]
                 [selmer "0.9.3"]]
  :main  file-sharing.web
  :aot [file-sharing.web]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler cljweb.web/app
         :auto-reload? true
         :auto-refresh? true
         }
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
