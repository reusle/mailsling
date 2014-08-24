(defproject mailsling "0.1.0"

  :description "Clojure bindings for http://mailgun.com (API v2)"

  :url "https://github.com/ccfontes/mailsling"

  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}

  :warn-on-reflection false

  :repl-options {:init-ns mail.sling}

  :dependencies [[org.clojure/clojure "1.4.0"]
                 [clj-http "0.6.5"]
                 [cheshire "5.0.2"]])