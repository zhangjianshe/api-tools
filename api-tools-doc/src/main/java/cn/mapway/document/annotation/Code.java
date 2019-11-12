package cn.mapway.document.annotation;

import java.lang.annotation.*;

// TODO: Auto-generated Javadoc

/**
 * 描述代码的特定值及其含义.
 *
 * @author zhangjianshe
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Code {

    /**
     * 字段值.
     *
     * @return the string
     */
    String value();

    /**
     * 字段含义.
     *
     * @return the string
     */
    String desc() default "";

    /**
     * 字段定义的名称.
     *
     * @return the string
     */
    String name() default "";

    /**
     * scope.
     *
     * @return the string
     */
    String scope() default "";
}
