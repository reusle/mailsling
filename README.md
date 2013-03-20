# cljmailgun

A simple wrapper around the [Mailgun](http://mailgun.org) REST API.

Still a work in process.

Clojure API
-----------

### Sending a plaintext e-mail

``` clojure
(cljmailgun.core/send-plaintext YourApiKey YourDomain FromAddress {:subject "subject"
                                                                   :body    "The body"
																   :to      "one email or a list of emails"})
```
