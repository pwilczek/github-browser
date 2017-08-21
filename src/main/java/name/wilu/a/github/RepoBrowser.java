package name.wilu.a.github;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import feign.Param;
import feign.RequestLine;

import java.util.Date;

interface RepoBrowser {

    @HystrixCommand
    @RequestLine("GET /repos/{owner}/{repo}")
    Repository details(@Param("owner") String owner, @Param("repo") String repo);

    @JsonPropertyOrder({"fullName", "description", "cloneUrl", "stars", "createdAt"})
    class Repository {
        public String description;
        public @JsonProperty("fullName") String full_name;
        public @JsonProperty("cloneUrl") String clone_url;
        public @JsonProperty("stars") int stargazers_count;
        public @JsonProperty("createdAt") @JsonFormat(pattern = "yyyy-MM-dd") Date created_at;
    }
}