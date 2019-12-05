package cn.mapway.apitoolstest.test;

import org.springframework.stereotype.Controller;

import cn.mapway.document.annotation.Doc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The Class TestController2.
 */
@Doc(value = "ABCD", group = "/泛能", desc = "ABCD Specail")
@RestController
@RequestMapping(value = "hello/456/")
public class TestController2 {

    /**
     * Gets the name 3.
     *
     * @param req the req
     * @return the name 3
     */
    @Doc(value = "touch3", group = "/Style", tags = {"测试", "文明"},refs = {"ref/hello.md"})
    @RequestMapping("/touch3")
    public Ret getname3(Req req) {
        return new Ret();
    }

    /**
     * Gets the name 456.
     *
     * @param req the req
     * @return the name 456
     */
    @Doc(value = "touch4", group = "/Css")
    @RequestMapping("/touch4")
    public Ret getname456(Req req) {
        return new Ret();
    }
}
