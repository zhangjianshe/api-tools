package cn.mapway.document.advance;

import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.lang.util.ClassMetaReader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * GenericTest
 *
 * @author zhangjianshe <zhangjianshe@gmail.com>
 */
public class GenericTest {

    public static void main(String[] args) {
        GenericTest app = new GenericTest();

        try {
            Map<String, List<String>> paramNames = ClassMetaReader.getParamNames(GenericTest.class);
            System.out.println(Json.toJson(paramNames));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Method[] methods = GenericTest.class.getMethods();

        for (Method method : methods) {
            if (method.getName().equals("test")) {
                System.out.println("method " + method.getName());

                Type returnType = method.getGenericReturnType();
                Mirror mirror = Mirror.me(returnType);
                Class<?> typeClass = Lang.getTypeClass(returnType);
                for (Field f : typeClass.getFields()) {
                    System.out.println(f.getName() + ":" +Lang.getGenericsType(mirror,f.getGenericType()));
                }


            }
        }
    }

    public ApiResult<Map<String, Integer>,String> test(List<Double> request) {
        return new ApiResult<>();
    }


}
