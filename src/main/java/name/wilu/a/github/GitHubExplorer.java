package name.wilu.a.github;

import feign.Client;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.hystrix.HystrixFeign;
import feign.slf4j.Slf4jLogger;
import name.wilu.a.github.RepoBrowser.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class GitHubExplorer {

    private ClientProvider provider = () -> new Client.Default(null, null);
    private @Value("${github.api}") String url;

    Repository repoDetails(String owner, String repository) {
        return HystrixFeign.builder()
                .client(provider.get())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.BASIC)
                .target(RepoBrowser.class, url)
                .details(owner, repository);
    }

    /**
     * Testing purposes
     */
    interface ClientProvider {
        Client get();
    }

    GitHubExplorer url(String url) {
        this.url = url;
        return this;
    }


    GitHubExplorer provider(ClientProvider provider) {
        this.provider = provider;
        return this;
    }
}