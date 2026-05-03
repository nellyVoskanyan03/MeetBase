package git.meet_base.meet_ms.api.interceptor;

import git.meet_base.meet_ms.api.annotation.AllowedRoles;
import git.meet_base.meet_ms.domain.exception.ForbiddenAccessException;
import git.meet_base.meet_ms.domain.model.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        if (handler instanceof HandlerMethod handlerMethod) {
            AllowedRoles allowedRoles = handlerMethod.getMethodAnnotation(AllowedRoles.class);

            if (allowedRoles != null) {
                String userRoleHeader = request.getHeader("X-User-Role");

                List<String> allowedRoleStrings = Arrays.stream(allowedRoles.value())
                        .map(UserRole::name)
                        .toList();

                System.out.println("Gateway Header Role: " + userRoleHeader);
                System.out.println("Allowed Roles for endpoint: " + allowedRoleStrings);

                if (userRoleHeader == null || !allowedRoleStrings.contains(userRoleHeader)) {
                    throw new ForbiddenAccessException("You do not have the required role to perform this action.");
                }
            }
        }

        return true;
    }
}