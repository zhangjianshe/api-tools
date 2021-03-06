package cn.mapway.document.ui.client.module;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;

// TODO: Auto-generated Javadoc

/**
 * 接口信息.
 *
 * @author zhangjianshe
 */
public class Entry extends JavaScriptObject {

    /**
     * Instantiates a new entry.
     */
    protected Entry() {
    }

    /**
     * 入口的函数名称.
     *
     * @return the string
     */
    public final native String methodName()/*-{
        return this.methodName;
    }-*/;

    /**
     * 接口名称.
     *
     * @return the string
     */
    public final native String title()/*-{
        return this.title;
    }-*/;

    /**
     * 接口说明.
     *
     * @return the string
     */
    public final native String summary()/*-{
        return this.summary;
    }-*/;

    /**
     * 接口相对路径.
     *
     * @return the string
     */
    public final native String relativePath()/*-{
        return this.relativePath;
    }-*/;

    /**
     * 调用类型.
     *
     * @return the string
     */
    public final native JsArrayString invokeMethods()/*-{
        return this.invokeMethods;
    }-*/;

    /**
     * 输入参数.
     *
     * @return the js array
     */
    public final native JsArray<ObjectInfo> input()/*-{
        return this.input;
    }-*/;

    /**
     * 输入参数.
     *
     * @return the js array
     */
    public final native JsArray<ObjectInfo> pathParas()/*-{
        return this.pathParas;
    }-*/;

    /**
     * 输入参数.
     *
     * @return the js array
     */
    public final native JsArray<ObjectInfo> queryParas()/*-{
        return this.queryParas;
    }-*/;

    /**
     * 输出参数.
     *
     * @return the object info
     */
    public final native ObjectInfo output()/*-{
        return this.output;
    }-*/;

    /**
     * 接口的排序值.
     *
     * @return the int
     */
    public final native int order()/*-{
        return this.order;
    }-*/;

    /**
     * 接口创建者.
     *
     * @return the string
     */
    public final native String author()/*-{
        return this.author;
    }-*/;

    /**
     * 方法所在的类.
     *
     * @return the string
     */
    public final native String parentClassName()/*-{
        return this.parentClassName;
    }-*/;

    /**
     * 开发状态.
     *
     * @return the string
     */
    public final native String state()/*-{
        return this.state;
    }-*/;

    /**
     * API样式.
     *
     * @return the string
     */
    public final native String style()/*-{
        return this.apiStyle;
    }-*/;

    /**
     * 全路径.
     *
     * @return the string
     */
    public final native String url()/*-{
        return this.url;
    }-*/;

    /**
     * 标签
     *
     * @return string [ ]
     */
    public final native String[] tags()/*-{
        return this.tags;
    }-*/;


    /**
     * set servlet Path
     *
     * @param servletPath ss
     */
    public final native void servletPath(String servletPath)/*-{
        this.servletPath = servletPath;
    }-*/;

    /**
     * servlet path
     *
     * @return strng
     */
    public final native String servletPath()/*-{
        return this.servletPath;
    }-*/;
}
