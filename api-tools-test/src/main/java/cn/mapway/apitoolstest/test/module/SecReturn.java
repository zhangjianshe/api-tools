package cn.mapway.apitoolstest.test.module;

import cn.mapway.document.annotation.ApiField;

/**
 *
 */
public class SecReturn {
    @ApiField(value = "名字",example = "摘要")
    public String name;
    @ApiField(value = "类型",example = "SHAL-1")
    public String type;
}
