package name.wilu.a.github;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import feign.Feign;
import feign.Param;
import feign.Request.Options;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import name.wilu.a.github.GitHubExplorer.RepoBrowser.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
class GitHubExplorer {

    private @Value("${github.api}") String url;
    private @Value("${github.connTimeout}") int connTimeout = 3000;
    private @Value("${github.readTimeout}") int readTimeout = 6000;


    Repository repoDetails(String owner, String repository) {
//        return HystrixFeign.builder()
        return Feign.builder()
                .options(new Options(connTimeout, readTimeout))
                .decoder(new GsonDecoder())
                .target(RepoBrowser.class, url)
                .details(owner, repository);
    }

    interface RepoBrowser {
        //
        @RequestLine("GET /repos/{owner}/{repo}")
        Repository details(@Param("owner") String owner, @Param("repo") String repo);
        //
        @JsonPropertyOrder({"fullName", "description", "cloneUrl", "stars", "createdAt"})
        class Repository {
            public String description;
            public @JsonProperty("fullName") String full_name;
            public @JsonProperty("cloneUrl") String clone_url;
            public @JsonProperty("stars") int stargazers_count;
            public @JsonProperty("createdAt") @JsonFormat(pattern = "yyyy-MM-dd") Date created_at;
        }
    }

    /**
     * Testing purposes
     */
    GitHubExplorer url(String url) {
        this.url = url;
        return this;
    }
}