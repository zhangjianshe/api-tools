package cn.mapway.document.helper.html;

import cn.mapway.document.module.Entry;

import java.util.List;

/**
 * 输出参数
 */
public class Outputparam {
    ObjTable t;

    public void parse(Entry entry, List<GenInfo> objList) {
        t = new ObjTable();
        t.parse(entry.output, objList);
    }

    public String toString() {
        return t.toString();
    }
}
