mailsling
==========
Forked from [cljmailgun](https://github.com/omarkj/cljmailgun), mailsling is a simple wrapper around the [Mailgun](http://mailgun.org) REST API.

### Holding mailsling with your hands!
----------
<img src="http://images5.fanpop.com/image/photos/30500000/Sogeking-sogeking-30524470-1280-1024.jpg"
 alt="Sogeking powerfully holding his slingshot" title="Sogeking powerfully holding his slingshot" align="right" height=210 />
Add the following to the `:dependencies` vector of your `project.clj` file:
[![clojars version](https://clojars.org/mailsling/latest-version.svg?raw=true)]
(https://clojars.org/mailsling)

### Aiming targets with correspondence
----------
.. and shooting them to selected targets:
```clj
(use 'mail.sling)

(def credentials {:domain "app81369673.mailgun.org"}) ; :api-key can also be set
```

When `credentials` are provided via environment variables `MAILGUN_API_KEY` and
`MAILGUN_APP_DOMAIN`, `with-sling` can be skipped, and `shootmail` can be used
standalone. In this case, `credentials` provided by environment variables will
be used instead:
```clj
(defn shoot-noreply-mail [params]
  (shootmail (assoc params :from "noreply@fortune500.com")))
```
Because sending emails is side-effecty, `with-sling` can be quite convenient
with multiple email tasks:
```clj
(let [params {:subject "work" :text "do this work now!" :to "minion@fortune500.com"}]
  (with-sling credentials
    (shootmail (assoc params :from "me@fortune500.com"))
    (shoot-noreply-mail params)))
```

### Gentle contributions
----------
- [Omar Yasin](https://github.com/omarkj) Author of cljmailgun
- [Carlos Cunha](https://github.com/ccfontes) Author of the fork

### License
----------
Copyright (C) 2015 Carlos C. Fontes.

Licensed under the [MIT License]("http://opensource.org/licenses/MIT").
