# Task List

This is Task list back-end service to demonstrate how authentication works with SPA applications as part of my book [Understanding React](https://leanpub.com/understandingreact). This book is completely free for my students (if you want to read it, just write to me).

# Install and Start

- Install maven
- git clone https://github.com/enriquemolinari/tasklist.git tasklist
- mvn install
- mvn exec:java -Dsecret=bfhAp4qdm92bD0FIOZLanC66KgCS8cYVxq/KlSVdjhI=
  - This will start Javalin/Jetty with the services running on Port: 1235. And set the shared secret. You can create any cryptographically secure secret, as long as it is the same used by the [User Auth](https://github.com/enriquemolinari/userauth) microservice. The secret is used to create and validate paseto tokens.

## Using LocalTunnel

To start the service using LocalTunnel URLs, use:

- mvn exec:java -Dsecret=bfhAp4qdm92bD0FIOZLanC66KgCS8cYVxq/KlSVdjhI= -Dtest-with-lt=true
  - This will enable CORS and change cookie parameters to enable testing using [LocalTunnel](https://github.com/localtunnel/localtunnel).