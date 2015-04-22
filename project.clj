(defproject mailsling "0.2.0"

  :description "Clojure bindings for http://mailgun.com (API v2)"

  :url "https://github.com/ccfontes/mailsling"

  :scm {:name "git", :url "https://github.com/ccfontes/mailsling"}

  :license {:name "MIT", :url "http://opensource.org/licenses/MIT"}

  :repl-options {:init-ns mail.sling}

  :dependencies [[org.clojure/clojure "1.7.0-alpha1"]
                 [clj-http "0.6.5"]
                 [email-validator "0.1"]])
