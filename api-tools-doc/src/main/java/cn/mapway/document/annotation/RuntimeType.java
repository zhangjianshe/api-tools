package cn.mapway.document.annotation;

import java.lang.annotation.*;

/**
 * 注解一个字段标明其类型在运行时动态调整，可以为所列举的类型其一
 *
 * @author zhangjianshe @gmail.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RuntimeType {

    /**
     * dasd
     *
     * @return default class [ ]
     */
    Class<?>[] value() default {};
}
