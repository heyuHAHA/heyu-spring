package org.springframework.stereotype;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface Value {
    String value();
}
