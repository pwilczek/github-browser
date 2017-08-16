package name.wilu.a.github;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
public class BrowserAppController {

    private final GitHubBrowser explorer;

    public BrowserAppController(GitHubBrowser explorer) {
        this.explorer = explorer;
    }

    @GetMapping(value = "/repositories/{owner}/{repo}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> repoDetails(@PathVariable String owner, @PathVariable String repo) {
        explorer.details(owner, repo);
        throw new NotImplementedException();
    }

}
