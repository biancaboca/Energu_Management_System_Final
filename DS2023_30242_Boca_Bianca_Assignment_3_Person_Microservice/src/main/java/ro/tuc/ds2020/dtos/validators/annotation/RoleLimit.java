package ro.tuc.ds2020.dtos.validators.annotation;

import ro.tuc.ds2020.dtos.validators.RoleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RoleValidator.class})
public @interface RoleLimit {

    String roleUser() default "user";
    String roleAdmin() default  "admin";

    String message() default "The role can be just user and admin";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
