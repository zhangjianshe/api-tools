package cn.mapway.apitoolstest.test;

import cn.mapway.apitoolstest.test.module.SecReturn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mapway.document.annotation.ApiStyle;
import cn.mapway.document.annotation.Doc;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Class TestController.
 */
@Doc(value = "ABCD", group = "/泛能云/APP接口/Tst", desc = "然后循环提取某个键值对的key和value，"
        + "对于value我们可以直接使用之前的策略进行反序列化，因为这部分的结构也是固定的。", refs = {"ref/hello.md","ref/test.html"})
@RestController
@RequestMapping(value = "hello/123/")
public class TestController {

    /**
     * Gets the name.
     *
     * @param req the req
     * @return the name
     */
    @Doc(
            value = "這裏是重要的央視",
            style = ApiStyle.IMPORTANT,
            desc = "我们的意图是明确的，将返回的多个Person实体对象放到一个Map结构中。但是问题就在于返回的JSON中的keys是不固定的(比如上述JSON中的keys是人名)，这导致反序列化失败。毕竟默认配置下的ObjectMapper也没有聪明到这种程度，能够猜测你是想要将多个实体放到Map中。")
    @RequestMapping("/touch")
    public Ret getname(@RequestBody Req req) {
        return new Ret();
    }



    @Doc("加密信息")
    @RequestMapping("/security")
    public SecReturn secAlg() throws NoSuchAlgorithmException {
        String ALGORITHM="md-5";
        MessageDigest md=MessageDigest.getInstance(ALGORITHM);
        SecReturn r=new SecReturn();
        r.name=MessageDigest.class.getName();

        r.type=ALGORITHM;
        return r;
    }
}
