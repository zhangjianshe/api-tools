package cn.mapway.apitoolstest.test;

import cn.mapway.document.annotation.Doc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The Class CopyOfTestController3.
 */
@Doc(value = "ABCD", group = "/泛能", desc = "今天我们做对了吗?")
@Controller
@RequestMapping(value = "hello/478")
public class CopyOfTestController3 {


    @Doc(value = "测试retClazz注解", retClazz = GetOperInfoByProdTypeIdResp.class)
    @RequestMapping(value = "/testretval")
    @ResponseBody
    public String testRetValue(@RequestBody Req req) {
        return "{'hello':'ssas'}";
    }

}
