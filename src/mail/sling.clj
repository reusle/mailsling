(ns mail.sling
  (:require [clj-http.client :as http]
            [clojure.string :refer (join)]
            [cheshire.core :refer (generate-string parse-string)]))

(defn xor [a b]
  "True if either a or b is True, but not if both are True or False"
  (cond
   (and (= a true) (= b false)) true
   (and (= a false) (= b true)) true
   :else false))

(defonce ^:dynamic *api-key* (System/getenv "MAILGUN_API_KEY"))
(defonce ^:dynamic *domain* (System/getenv "MAILGUN_APP_DOMAIN"))

(defn- comma-list
  [list]
  (if (string? list)
    list
    (join "," list)))

(defn- to-mailgun
  "Send an e-mail"
  [form-data]
  (let [resp (http/post (join ["https://api.mailgun.net/v2/" *domain* "/messages"])
                        {:basic-auth ["api" *api-key*]
                         :form-params form-data})
        status (:status resp)
        body (parse-string (:body resp))]
    (if (= status 200)
      {:sent 'yes, :id (body "id")})))

(defn shootmail
  [{:keys [from to subject body html] :as params}]
  {:pre [(string? subject)
         (xor (string? body) (string? html))
         (or (list? to) (string? to))]}
  (to-mailgun
    (merge
      {:from from, :to (comma-list to), :subject subject}
      (if body {:text body} {:html html}))))

(defmacro with-sling [credentials & body]
  `(binding [*api-key* (or (:api-key ~credentials) *api-key*)
             *domain* (or (:domain ~credentials) *domain*)]
    ~@body))

; TODO change :body to :text
; TODO make subject and body optional