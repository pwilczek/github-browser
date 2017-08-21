package name.wilu.a.github;

import com.fasterxml.jackson.annotation.JsonInclude;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeoutException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
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
    static class Handler {
        //
        static String FEIGN_ERROR = "Error calling external resource!";
        static String TIMEOUT_ERROR = "Timeout calling external resource!";
        static String UNEXPECTED_ERROR = "Unexpected error";

        @ExceptionHandler(FeignException.class)
        ResponseEntity<?> feignFailures(FeignException e) {
            return ResponseEntity.status(e.status()).body(
                    new Reason(FEIGN_ERROR, e.getMessage()));
        }

        @ExceptionHandler(TimeoutException.class)
        ResponseEntity<?> timeouts(TimeoutException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new Reason(TIMEOUT_ERROR, e.getMessage()));
        }

        @ExceptionHandler(Exception.class)
        ResponseEntity<?> other(Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                    new Reason(UNEXPECTED_ERROR, e.getMessage()));
        }
    }

    @JsonInclude(NON_NULL)
    static class Reason {
        public final String hystrix = "http://localhost:8080/hystrix";
        public final String msg;
        public final String details;

        Reason(String msg, String details) {
            this.msg = msg;
            this.details = details;
        }
    }
}
