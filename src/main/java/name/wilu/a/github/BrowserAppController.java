package name.wilu.a.github;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Controller
public class BrowserAppController {

    @GetMapping("/repositories/{owner}/{repo}")
    public ResponseEntity<?> repoDetails(@PathVariable String owner, @PathVariable String repo) {
        throw new NotImplementedException();
    }

}
