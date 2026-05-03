package git.meet_base.meet_ms.api.annotation;

import git.meet_base.meet_ms.domain.model.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedRoles {
    UserRole[] value();
}