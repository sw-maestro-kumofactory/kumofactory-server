package com.kumofactory.cloud.global.annotation.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidatorImpl.class)
public @interface EnumValidator {
		Class<? extends Enum<?>> enumClass();

		String message() default "must be any of enum {enumClass}";

		Class<?>[] groups() default {};

		Class<?>[] payload() default {};

}
