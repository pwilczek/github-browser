package name.wilu.a.github;

import feign.Client;
import feign.Response;
import name.wilu.a.github.GitHubExplorer.ClientProvider;
import name.wilu.a.github.RepoBrowser.Repository;
import org.junit.Test;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test data: feign-mock-response.json
 */
public class FeignTest {

    private GitHubExplorer explorer = new GitHubExplorer().url("any").provider(mocked());

    @Test
    public void shouldParseSccRepo() throws Exception {
        Repository actual = explorer.repoDetails("spring-cloud", "spring-cloud-contract");
        //
        assertThat(actual.clone_url, is("https://github.com/spring-cloud/spring-cloud-contract.git"));
        assertThat(actual.description, is("Support for Consumer Driven Contracts in Spring"));
        assertThat(actual.full_name, is("spring-cloud/spring-cloud-contract"));
        assertThat(actual.stargazers_count, is(151));
        assertThat(new SimpleDateFormat("yyyy-MM-dd").format(actual.created_at), is("2016-06-12"));

    }

    private ClientProvider mocked() {
        return () -> {
            try {
                return when(mock(Client.class).execute(any(), any()))
                        .thenReturn(Response.create(200, "", emptyMap(), body()))
                        .getMock();
            } catch (IOException e) {
                throw new RuntimeException("Error while reading feign-mock-response.json");
            }
        };

    }

    private byte[] body() throws IOException {
        return StreamUtils.copyToByteArray(
                this.getClass().getClassLoader().getResourceAsStream("feign-mock-response.json")
        );

    }
}
