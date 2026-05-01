package git.meet_base.notification_ms.client;

import git.meet_base.notification_ms.dto.AuthUserDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthClient {

    private final RestClient authRestClient;

    public AuthClient(RestClient authRestClient) {
        this.authRestClient = authRestClient;
    }

    public List<String> getActiveManagerEmails(UUID companyId) {
        List<AuthUserDto> users = authRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/users")
                        .queryParam("role", "MANAGER")
                        .queryParam("companyId", companyId)
                        .queryParam("isActive", true)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (users == null) return List.of();

        return users.stream()
                .map(AuthUserDto::getEmail)
                .collect(Collectors.toList());
    }

    public List<String> getUserEmailsBatch(List<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        System.out.println("jj");
        userIds.stream().map(UUID::toString).toList().forEach(System.out::println);
        Map<UUID, String> userEmailMap = authRestClient.post()
                .uri("/users/emails/batch")
                .body(userIds.stream().map(UUID::toString).toList())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        System.out.println("gg");
        if (userEmailMap == null || userEmailMap.isEmpty()) {
            return List.of();
        }

        return userEmailMap.values().stream().toList();
    }
}