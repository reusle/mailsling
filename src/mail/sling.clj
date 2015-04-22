(ns mail.sling
  (:require [clj-http.client :as http]
            [clojure.string :as str]
            [email-validator.core :refer [is-email?]]))

(defn- update-multi
  "Updates multiple keys of a map with multiple fns using a map of key/fn pairs."
  [m fn-m]
  (merge
    m
    (some->> m (#(map (fn [[k f]] [k (f (k %))]) fn-m)) (into {}))))

(defonce ^:dynamic *api-key* (System/getenv "MAILGUN_API_KEY"))
(defonce ^:dynamic *domain* (System/getenv "MAILGUN_APP_DOMAIN"))

(defn- comma-list
  [l]
  (if (string? l)
    l
    (str/join "," l)))

(defn- mailgun
  "Send an e-mail"
  [form-params]
  (http/post (str/join ["https://api.mailgun.net/v2/" *domain* "/messages"])
             {:basic-auth ["api" *api-key*], :form-params form-params}))

(defn shootmail
  [{:keys [from to subject text html] :as params}]
  {:pre [(string? from)
         (or ((every-pred string? is-email?) to)
             (and ((some-fn seq? vector? set?) to)
                  (seq to)
                  (every? is-email? to)))
         (not (and text html))]}
  (let [params (update-multi params {:to comma-list
                                     :subject #(or % " ")
                                     :text #(if (or % html) % " ")})]
    (mailgun
      (select-keys params [:from :to :subject :text :html]))))

(defmacro with-sling [credentials & body]
  `(binding [*api-key* (or (:api-key ~credentials) *api-key*)
             *domain* (or (:domain ~credentials) *domain*)]
    ~@body))