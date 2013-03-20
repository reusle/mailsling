(ns cljmailgun.core
  (:require [clj-http.client :as http])
  (:use [clojure.string :only (join)])
  (:use [cheshire.core :only (generate-string parse-string)]))

(declare comma-list)
(declare to-mailgun)

(defn send-plaintext
  [api-key domain from {:keys [to subject body]}]
  {:pre [(string? subject) (string? body)
         (or (list? to) (string? to))]}
  (to-mailgun api-key domain {:to (comma-list to)
                              :from from
                              :subject subject
                              :text body}))

(defn- comma-list
  [list]
  (if (string? list)
    list
    (join "," list)))

(defn- to-mailgun
  "Send an e-mail"
  [api-key domain form-data]
  (let [resp (http/post (join ["https://api.mailgun.net/v2/" domain "/messages"])
                        {:basic-auth ["api" api-key]
                         :form-params form-data})
        status (:status resp)
        body (parse-string (:body resp))]
    (if (= status 200)
      {:sent 'yes
       :id (body "id")}
      )))