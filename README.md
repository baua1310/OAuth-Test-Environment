# OAuth-Test-Environment
University project: Conception and prototypical implementation of an OAuth 2.0 test environment

## Packages
### Authorization Server
see: authorization-server
start by running AuthorizationServerApplication class
An OAuth 2.0 Authorization Server implemented with Spring Security.

### Resource Server
see: resource-server
start by running ResourceServerApplication class
An OAuth 2.0 Resource Server implemented with Spring Security.

### Selenium Tests
see: testing
start by running tests inside TestSeleniumTest class
Test OAuth 2.0 environment with selenium.

### Web Client
see: web-client
start by running WebClientApplication class
An OAuth 2.0 Web Client implemented with Spring Security.
Browse to http://web-client:8080/demo to try the OAuth 2.0 environment interactively.

### OAuth Client
see: oauth-client
A simple OAuth 2.0 Client using no OAuth 2.0 libraries.

## Installation
Get [VS Code](https://code.visualstudio.com/), [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/)

Add additional hosts to your local hosts file:

auth-server       127.0.0.1

resource-server   127.0.0.1

web-client        127.0.0.1
