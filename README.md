### Running
./deploy-locally builds and runs service locally on port 8080.

./deploy-docker builds and deploys service to docker container, also port 8080.

### Performance testing with Vegeta
Installation procedure: https://github.com/tsenart/vegeta

When the service is running:
- ./vegeta-single runs for 2 seconds sending 20rps against single URI.
- ./vegeta-multiple runs for 2 seconds, 20rps, against URI defined in vegeta-targets

### API documentation with Swagger
Hit the following URL: localhost:8080/swagger-ui.html

### Hystrix status
Hit the following URL: http://localhost:8080/hystrix and provide hystrix stream URL http://localhost:8080/hystrix.stream

### API contract definition
A single happy scenario was documented in shouldProvideRepoDetails.groovy. It's executed against GitHub endpoint during build process.
Generally the test should pass consistently. The failure might mean GitHub API change or changes in exemplary project selected for testing.

### Known problems
After service startup the first call might fail due to infrastructure initialization (hystrix Timeout; https://stackoverflow.com/questions/36071841/spring-cloud-hystrix-fails-at-first-command-call).
Since spring cloud contract server side test is executed during build process and the test hits real GitHub endpoint, hystrix timeout has been increased to 15 seconds (test/resource/config.properties).

### Further considerations
Failover and load balancing - it might be reasonable to increase number of instances to 2, register instances in any kind of discovery service (Eureka or Consul),
then rely on client side load balancing (Ribbon) or put instances behind reverse proxy like Zuul.

If the service fails check your GitHub rate_limit: https://api.github.com/rate_limit







