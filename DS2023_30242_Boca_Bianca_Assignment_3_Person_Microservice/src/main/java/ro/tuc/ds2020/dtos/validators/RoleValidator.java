package ro.tuc.ds2020.dtos.validators;

import org.springframework.stereotype.Component;
import ro.tuc.ds2020.dtos.validators.annotation.RoleLimit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class RoleValidator implements ConstraintValidator<RoleLimit, String> {

    private String roleUser;
    private String roleAdmin;

    @Override
    public void initialize(RoleLimit constraintAnnotation) {
        this.roleUser = constraintAnnotation.roleUser();
        this.roleAdmin = constraintAnnotation.roleAdmin();
    }

    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        return role != null && (role.equals(roleUser) || role.equals(roleAdmin));
    }
}
