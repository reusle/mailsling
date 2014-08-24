(ns mail.sling
  (:require [clj-http.client :as http]
            [clojure.string :refer (join)]
            [cheshire.core :refer (generate-string parse-string)]))

(defonce ^:dynamic *api-key* (System/getenv "MAILGUN_API_KEY"))
(defonce ^:dynamic *domain* (System/getenv "MAILGUN_APP_DOMAIN"))

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
      {:sent 'yes, :id (body "id")})))

(defmacro with-sling [credentials & body]
  `(binding [*api-key* (or (:api-key ~credentials) *api-key*)
             *domain* (or (:domain ~credentials) *domain*)]
    ~@body))

(defn shootmail [{:keys [from] :as params}]
  (send-plaintext *api-key* *domain* from
    (dissoc params :from)))