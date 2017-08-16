package name.wilu.a.github;

import feign.Param;
import feign.RequestLine;
import org.springframework.stereotype.Service;

import java.util.List;

interface GitHubBrowser {

    @RequestLine("GET /repos/{owner}/{repo}/contributors")
    List<RepoDetails> details(@Param("owner") String owner, @Param("repo") String repo);

    static class RepoDetails {
        private String name;
    }

    @Service
    static class RepoBrowser() {
        List<RepoDetails> details
        //to low battery :/
    }


}
