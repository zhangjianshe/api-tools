package cn.mapway.document.helper.html;

import cn.mapway.document.module.ObjectInfo;

import java.util.List;

public class Para extends HtmlTable {
    @Override
    public int getLevel() {
        return 0;
    }

    /**
     * Simple string.
     *
     * @param type the type
     * @return the string
     */
    public static String simple(String type) {
        if (type == null) {
            return "";
        }
        int index = type.lastIndexOf('.');
        if (index >= 0) {
            // 处理<>
            int ltIndex = type.lastIndexOf('<');
            if (ltIndex >= 0) {
                String start = type.substring(0, ltIndex + 1);
                return start + type.substring(index + 1);
            } else {
                return type.substring(index + 1);
            }
        } else
            return type;
    }

    /**
     * 处理长度约束.
     *
     * @param o the o
     * @return label
     */
    static String handleLengthConstrain(ObjectInfo o) {
        String l = "";
        if (isString(o)) {
            if (o.minLength == 0 && o.maxLength == 0) {
                l = "";
            } else {
                l = o.minLength + "-" + o.maxLength + "个字符";
            }
        } else if (isNumber(o)) {
            if (o.min != null && o.max != null) {
                l = o.min + "至" + o.max;
            } else if (o.min != null) {
                l = "最小为:" + o.min;
            } else if (o.max != null) {
                l = "最大为:" + o.min;
            } else {
                l = "";
            }
        } else {
            l = "";
        }
        return l;
    }

    private static boolean isNumber(ObjectInfo o) {
        for (String s : numbers) {
            if (o.type.contains(s)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isString(ObjectInfo o) {
        for (String s : strings) {
            if (o.type.contains(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find obj.
     *
     * @param type    the type
     * @param objList the obj list
     * @return true, if successful
     */
    protected boolean findObj(String type, List<GenInfo> objList) {
        for (GenInfo info : objList) {
            if (info.type.equals(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The ps.
     */
    private static String[] numbers = {"int", "Integer", "float", "Float", "Double", "double", "long", "Long", "short",
            "byte"};
    /**
     * The ps.
     */
    private static String[] strings = {"String"};
    /**
     * The ps.
     */
    private static String[] ps = {"int", "Integer", "float", "Float", "Double", "double", "long", "Long", "Date",
            "DateTime", "String", "boolean", "Boolean", "char", "short", "byte", "Timestamp"};

    /**
     * Checks if is primitive.
     *
     * @param type the type
     * @return true, if is primitive
     */
    public boolean isPrimitive(String type) {

        if (type.startsWith("List<") || type.startsWith("Map<")) {
            return false;
        }

        for (String s : ps) {
            if (type.equals(s)) {
                return true;
            }
        }
        return false;

    }
}
