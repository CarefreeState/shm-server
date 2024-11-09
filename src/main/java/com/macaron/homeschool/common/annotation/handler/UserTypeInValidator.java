package com.macaron.homeschool.common.annotation.handler;

import com.macaron.homeschool.common.annotation.UserTypeIn;
import com.macaron.homeschool.common.enums.UserType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;
import java.util.Set;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 0:23
 */
public class UserTypeInValidator implements ConstraintValidator<UserTypeIn, Integer> {

    private Set<UserType> userTypes;

    @Override
    public void initialize(UserTypeIn userTypeIn) {
        this.userTypes = Set.of(userTypeIn.role());
    }

    @Override
    public boolean isValid(Integer role, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(role)
                .map(UserType::get)
                .map(userType -> userTypes.contains(userType))
                .orElse(Boolean.TRUE);
    }

}
