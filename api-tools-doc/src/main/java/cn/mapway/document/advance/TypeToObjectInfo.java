package cn.mapway.document.advance;

import cn.mapway.document.module.ObjectInfo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * TypeToObjectInfo
 * Type 转化为类型对象
 * @author zhangjianshe <zhangjianshe@gmail.com>
 */
public class TypeToObjectInfo {
    public ObjectInfo toObjectInfo(Type type){

        if(type instanceof ParameterizedType)
            {

            }
        return  null;
    }
}
