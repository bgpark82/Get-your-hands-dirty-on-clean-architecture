package com.bgpark.app.common;

import javax.validation.*;
import java.util.Set;

public abstract class SelfValidating<T> {

    private Validator validator;

    public SelfValidating() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * 상속받은 객체의 모든 validation을 체크
     */
    protected void validateSelf() {
        Set<ConstraintViolation<T>> violations = validator.validate((T) this);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
