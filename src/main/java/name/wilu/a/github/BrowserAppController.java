package name.wilu.a.github;

import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
public class BrowserAppController {

    private final GitHubExplorer explorer;

    public BrowserAppController(GitHubExplorer explorer) {
        this.explorer = explorer;
    }

    @GetMapping(value = "/repositories/{owner}/{repo}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> repoDetails(@PathVariable String owner, @PathVariable String repo) {
        return ResponseEntity.ok(explorer.repoDetails(owner, repo));
    }

    @ControllerAdvice
    private static class Handler {
        @ExceptionHandler(FeignException.class)
        protected ResponseEntity<?> feignFailures(FeignException e) {
            return ResponseEntity.status(e.status()).build(); // some extra msg might be useful
        }
    }
}
