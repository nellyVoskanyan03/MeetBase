package git.meet_base.notification_ms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AuthClientConfig {

    private static final Logger log = LoggerFactory.getLogger(AuthClientConfig.class);

    @Value("${AUTH_MS_URL:http://localhost:8083}")
    private String authMsUrl;

    @Bean
    public RestClient authRestClient() {
        log.info("Configuring Auth RestClient with Base URL: {}", authMsUrl);

        return RestClient.builder()
                .baseUrl(authMsUrl)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                // TODO: Once you implement JWT security later, you will add an interceptor here to automatically attach the "Authorization: Bearer <token>" header!
                .build();
    }
}