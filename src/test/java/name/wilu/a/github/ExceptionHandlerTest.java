package name.wilu.a.github;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import feign.Response;
import name.wilu.a.github.BrowserAppController.Handler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

import static com.netflix.hystrix.exception.HystrixRuntimeException.FailureType.BAD_REQUEST_EXCEPTION;
import static com.netflix.hystrix.exception.HystrixRuntimeException.FailureType.TIMEOUT;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExceptionHandlerTest {

    private ApiController controller;
    private MockMvc mockMvc;

    @RestController
    @RequestMapping("/api")
    class ApiController {
        @GetMapping(produces = APPLICATION_JSON_VALUE)
        public ResponseEntity<?> call() throws Exception {
            return ResponseEntity.ok("I'm ok!");
        }
    }

    @Before
    public void setUp() {
        controller = mock(ApiController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new Handler()).build();
    }

    @Test
    public void shouldHandleFeignFailures() throws Exception {
        FeignException fex = FeignException.errorStatus("api", response(509));
        when(controller.call()).thenThrow(fex);
        mockMvc.perform(get("/api"))
                .andExpect(status().isBandwidthLimitExceeded())
                .andExpect(jsonPath("$.msg").value(Handler.FEIGN_ERROR));
    }

    @Test
    public void shouldHandleTimeouts() throws Exception {
        String details = "Timeout happened!";
        TimeoutException exc = new TimeoutException(details);
        when(controller.call()).thenThrow(exc);
        mockMvc.perform(get("/api"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.msg").value(Handler.TIMEOUT_ERROR))
                .andExpect(jsonPath("$.details").value(details));
    }

    @Test
    public void shouldHandleHystrixTimeout() throws Exception {
        String msg = "cause-msg";
        HystrixRuntimeException hex = new HystrixRuntimeException(TIMEOUT, null, EMPTY, new TimeoutException(msg), null);
        when(controller.call()).thenThrow(hex);
        mockMvc.perform(get("/api"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.msg").value(Handler.TIMEOUT_ERROR))
                .andExpect(jsonPath("$.details").value(msg));

    }

    @Test
    public void shouldHandleHystrixFeignFailure() throws Exception {
        FeignException fex = FeignException.errorStatus("api", response(400));
        HystrixRuntimeException hex = new HystrixRuntimeException(BAD_REQUEST_EXCEPTION, null, EMPTY, fex, null);
        when(controller.call()).thenThrow(hex);
        mockMvc.perform(get("/api"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value(Handler.FEIGN_ERROR));
    }


    @Test
    public void shouldHandleOthers() throws Exception {
        String details = "NPE!";
        NullPointerException exc = new NullPointerException(details);
        when(controller.call()).thenThrow(exc);
        mockMvc.perform(get("/api"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.msg").value(Handler.UNEXPECTED_ERROR))
                .andExpect(jsonPath("$.details").value(details));
    }

    private Response response(int status) {
        return Response.
                create(status, EMPTY, emptyMap(), EMPTY, defaultCharset());
    }

}
