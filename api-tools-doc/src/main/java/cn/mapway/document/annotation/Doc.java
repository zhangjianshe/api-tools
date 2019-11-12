/******************************************************************************
 * <pre>
 *
 *            =============================================================
 *            -   ____ _  _ ____ _  _ ____  _ _ ____ _  _ ____ _  _ ____  -
 *            -    __] |__| |__| |\ | | __  | | |__| |\ | [__  |__| |___  -
 *            -   [___ |  | |  | | \| |__| _| | |  | | \| ___] |  | |___  -
 *            -           http://hi.baidu.com/zhangjianshe                -
 *            =============================================================
 * </pre>
 *******************************************************************************/
package cn.mapway.document.annotation;

import java.lang.annotation.*;


/**
 * 自定义注解类，可以应用在类，方法，参数，字段上.
 *
 * @author zhangjianshe @navinfo.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Doc {

    /**
     * 描述信息,对要描述的类和方法做出解释和说明/title.
     *
     * @return the string
     */
    String value();

    /**
     * 描述信息.
     *
     * @return the string
     */
    String desc() default "";

    /**
     * 作者.
     *
     * @return the string
     */
    String author() default "zhangjianshe@gmail.com";

    /**
     * 接口分组信息 ,以 /为分隔符 进行树形分组，缺省归属为根节点.
     *
     * @return the string
     */
    String group() default "/";

    /**
     * 接口在分组中的排序.
     *
     * @return the int
     */
    int order() default 0;

    /**
     * 开发状态.
     *
     * @return the development state
     */
    DevelopmentState state() default DevelopmentState.UNSTART;

    /**
     * API显示样式.
     *
     * @return API显示样式. api style
     */
    ApiStyle style() default ApiStyle.NORMAL;


    /**
     * 引用的HTML文件.
     *
     * @return 引用的HTML文件. string [ ]
     */
    String[] refs() default {};

    /**
     * 接口的标签列表
     *
     * @return 接口的标签列表 string [ ]
     */
    String[] tags() default {};

    /**
     * 如果函数返回值是一个泛型函数，这里的类型数组第一个表示泛型的具体类型
     * @return
     */
    Class[] retClazz() default {};

}
