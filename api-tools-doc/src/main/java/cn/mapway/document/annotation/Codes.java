package cn.mapway.document.annotation;

import java.lang.annotation.*;

// TODO: Auto-generated Javadoc

/**
 * 注解.
 *
 * @author zhangjianshe
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Codes {

    /**
     * Code list.
     *
     * @return the code[]
     */
    Code[] value();

    /**
     * scope.
     *
     * @return the string
     */
    String scope() default "";
}
