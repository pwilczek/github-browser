package name.wilu.a.github;


import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

public abstract class BrowserAppContractBase {

    @Before
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(new BrowserAppController(null));
    }

}
