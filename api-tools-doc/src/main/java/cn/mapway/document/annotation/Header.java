package cn.mapway.document.annotation;

import java.lang.annotation.*;

/**
 * The interface Header.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Header {
    /**
     * 用于Header中的 KEY值 例如 ENN-TOKEN
     *
     * @return the string
     */
    String key() default "";

    /**
     * HEader中的VALUE值
     *
     * @return the int
     */
    String value() default "";
}
