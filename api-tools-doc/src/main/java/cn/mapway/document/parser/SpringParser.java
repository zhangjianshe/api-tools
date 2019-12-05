package cn.mapway.document.parser;

import cn.mapway.document.annotation.*;
import cn.mapway.document.helper.Markdowns;
import cn.mapway.document.helper.Scans;
import cn.mapway.document.module.*;
import org.nutz.castor.Castors;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.log.Logs;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * 解析Spring 注解.
 *
 * @author zhangjianshe @gmail.com
 */
public class SpringParser {

    /**
     * The Constant log.
     */
    private final static org.nutz.log.Log log = Logs.getLog(SpringParser.class);

    /**
     * The m context.
     */
    GenContext mContext;
    /**
     * 类深度信息.
     */
    Deeps deeps;

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalAccessException   the illegal access exception
     * @throws InstantiationException   the instantiation exception
     */
    public static void main(String[] args)
            throws IllegalArgumentException, IllegalAccessException, InstantiationException {

        SpringParser p = new SpringParser();

        ApiDoc doc = p.parse(new GenContext(), "cn.mapway.doc2.test");
        System.out.println(Json.toJson(doc));
    }

    /**
     * 解析包中的类.
     *
     * @param context      the context
     * @param packageNames 包名
     * @return the api doc
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalAccessException   the illegal access exception
     * @throws InstantiationException   the instantiation exception
     */
    public ApiDoc parse(GenContext context, String... packageNames)
            throws IllegalArgumentException, IllegalAccessException, InstantiationException {

        mContext = context;
        if (packageNames.length == 0) {
            return new ApiDoc();
        }
        ArrayList<Class<?>> clzs = new ArrayList<Class<?>>();

        for (String pk : packageNames) {
            Set<Class<?>> clz = scanPackage(pk);
            clzs.addAll(clz);
        }

        ApiDoc doc = new ApiDoc();
        doc.author = context.getAuthor();
        doc.basePath = context.getBasepath();
        doc.title = context.getDocTitle();
        doc.wordUrl = context.getWordURL();
        doc.summary = context.getSubtitle();
        doc.domain = context.getDomain();
        doc.copyright = context.getCopyright();
        doc.apiVersion = context.getApiVersion();
        doc.logo = context.getLogobase64();
        doc.homeUrl = context.getHomeUrl();
        doc.cssStyle = context.getCssStyle();

        if (Strings.isEmpty(doc.apiVersion)) {
            doc.apiVersion = "1.0";
        }
        doc.copyright = doc.copyright + "-" + CompileVersion.VERSION;

        for (Class<?> clz : clzs) {
            if (clz.getAnnotation(Controller.class) != null || clz.getAnnotation(RestController.class) != null) {
                parseClass(doc, clz);
            }

        }
        doc.root.name = doc.title;
        doc.root.fullName = "/" + doc.title;
        doc.root.summary = "";
        doc.sort();
        return doc;
    }

    private Set<Class<?>> scanPackage(String pk) {

        Set<Class<?>> clzs = Scans.getClasses(pk);

        return clzs;
    }

    /**
     * 解析某个类.
     *
     * @param document the document
     * @param clz      the clz
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalAccessException   the illegal access exception
     * @throws InstantiationException   the instantiation exception
     */
    private void parseClass(ApiDoc document, Class<?> clz)
            throws IllegalArgumentException, IllegalAccessException, InstantiationException {

        Doc doc = clz.getAnnotation(Doc.class);

        if (doc == null) {
            return;
        }

        populateGroup(document, clz);
    }

    /**
     * 填充Group信息.
     *
     * @param document the document
     * @param c        the c
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalAccessException   the illegal access exception
     * @throws InstantiationException   the instantiation exception
     */
    private void populateGroup(ApiDoc document, Class<?> c)
            throws IllegalArgumentException, IllegalAccessException, InstantiationException {

        // 类Doc
        Doc doc = c.getAnnotation(Doc.class);
        String group_base_path = doc.group();

        Group apiGroup = document.findGroup(group_base_path);

        apiGroup.summary += doc.desc().length() > 0 ? doc.desc() : "";
        apiGroup.summary += parseRef(c, doc.refs());
        apiGroup.order = doc.order();

        String basepath = "";

        RequestMapping rm = c.getAnnotation(RequestMapping.class);
        if (rm != null) {
            String[] paths = rm.value();
            if (paths == null || paths.length == 0) {

            } else {
                basepath = paths[0];
            }
        }


        Method[] methods = c.getDeclaredMethods();
        List<Method> list = new ArrayList<Method>();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            RequestMapping rm1 = m.getAnnotation(RequestMapping.class);
            if (rm1 != null) {
                list.add(m);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            Method m = list.get(i);

            Entry entry = handleMethod(c, document, group_base_path, m);

            if (entry != null) {
                entry.parentClassName = c.getName();
                entry.relativePath = basepath + entry.relativePath;
                entry.url = mContext.getBasepath() + entry.relativePath;
            }
        }
    }

    /**
     * 解析方法，生成APIentry.
     *
     * @param document        the document
     * @param group_base_path the group base path
     * @param m               the m
     * @return the entry
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalAccessException   the illegal access exception
     * @throws InstantiationException   the instantiation exception
     */
    private Entry handleMethod(Class<?> clsType, ApiDoc document, String group_base_path, Method m)
            throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        // 如果没有添加注解 就不输出这个接口的文档
        Doc summary = m.getAnnotation(Doc.class);
        if (null == summary) {
            return null;
        }
        Entry e = new Entry();


        RequestMapping rm = m.getAnnotation(RequestMapping.class);
        if (rm != null) {
            // TODO 增加对多路径的支持
            String[] paths = rm.value();
            if (paths == null || paths.length == 0) {

            } else {
                e.relativePath = paths[0];
            }

            // 处理请求方法 支持对多种调用方式 POST GET PUT DELETE 等
            RequestMethod[] ms = rm.method();
            if (ms != null) {
                for (int i = 0; i < ms.length; i++) {
                    RequestMethod rm0 = ms[i];
                    e.invokeMethods.add(rm0.name());
                }
            }
        }


        if (e.invokeMethods.size() == 0) {
            e.invokeMethods.add("GET");
        }

        if (e.relativePath.length() == 0) {
            return null;
        }

        e.methodName = m.getName();

        Class retClz = null;


        if (summary != null) {
            e.title = summary.value();
            e.summary = summary.desc() == null ? "" : summary.desc();
            e.summary += parseRef(clsType, summary.refs());
            e.order = summary.order();
            e.author = summary.author();
            e.state = transState(summary.state());
            e.apiStyle = transApiStyle(summary.style());
            // 处理方法的标签.
            if (summary.tags() != null) {
                for (String tag : summary.tags()) {
                    e.tags.add(tag);
                }
            }
            if (summary.retClazz() != null && summary.retClazz().length > 0) {
                retClz = summary.retClazz()[0];
            }
        }

        Class<?>[] ps = m.getParameterTypes();

        Class<?> out = (Class<?>) m.getReturnType();

        if (retClz != null) {
            out = retClz;
        }

        int i = 0;
        for (Class<?> clz : ps) {

            String name = clz.getSimpleName();
            String pname = "";
            if (clz.getPackage() != null) {
                pname = clz.getPackage().getName();
            }

            if (name.startsWith("Http") || pname.startsWith("org.")) {
                continue;
            } else {

                Annotation[][] parameterAnnotations = m.getParameterAnnotations();
                Annotation[] ass = parameterAnnotations[i];

                // 处理每一个参数
                PathVariable pathVariable = null;
                RequestParam queryVariable = null;
                RequestBody isRequestBody = null;
                ApiField paraDoc = null;
                Size stringConstrain = null;
                NotNull nullConstrain = null;
                Min minConstrain = null;
                Max maxConstrain = null;

                for (Annotation a : ass) {
                    if (a instanceof PathVariable) {
                        pathVariable = (PathVariable) a;
                    } else if (a instanceof RequestParam) {
                        queryVariable = (RequestParam) a;
                    } else if (a instanceof ApiField) {
                        paraDoc = (ApiField) a;
                    } else if (a instanceof RequestBody) {
                        isRequestBody = (RequestBody) a;
                    } else if (a instanceof Size) {
                        stringConstrain = (Size) a;
                    } else if (a instanceof NotNull) {
                        nullConstrain = (NotNull) a;
                    } else if (a instanceof Min) {
                        minConstrain = (Min) a;
                    } else if (a instanceof Max) {
                        maxConstrain = (Max) a;
                    }
                }

                ObjectInfo p = handleParameter(clz, name);

                if (p == null) {
                    continue;
                }

                if (paraDoc != null) {
                    p.example = paraDoc.example();
                    p.manditary = paraDoc.mandidate();
                    p.title = paraDoc.value();
                    // 处理注释,从ref中获取
                    p.summary = parseRef(clsType, paraDoc.refs());
                }

                // 长度约束
                if (stringConstrain != null) {
                    p.minLength = stringConstrain.min();
                    p.maxLength = stringConstrain.max();
                }

                if (minConstrain != null) {
                    p.min = minConstrain.value();
                }
                if (maxConstrain != null) {
                    p.max = maxConstrain.value();
                }
                if (nullConstrain != null) {
                    p.manditary = true;
                }

                if (pathVariable != null) {
                    // 路径参数
                    if (Strings.isBlank(pathVariable.value())) {
                        p.name = "未知";
                    } else {
                        p.name = pathVariable.value();
                    }
                    e.pathParas.add(p);
                } else if (queryVariable != null) {
                    p.name = queryVariable.value();
                    e.queryParas.add(p);
                } else if (isRequestBody != null) {

                    e.input.add(p);
                }
                i++;
            }

        }

        // 处理返回值注解
        ApiField returnDoc = m.getAnnotation(ApiField.class);

        e.output = handleParameter(out, "out");
        if (returnDoc != null) {
            // 类型 解释 例子
            if (returnDoc.example() != null && returnDoc.example().length() > 0) {
                e.output.example = returnDoc.example();
            }
        }

        String group_path = group_base_path + summary.group();
        Group apiGroup = document.findGroup(group_path);
        apiGroup.entries.add(e);

        return e;
    }

    /**
     * 解析ref html.
     *
     * @param refs
     * @return
     */
    private String parseRef(Class<?> type, String[] refs) {

        if (refs == null || refs.length == 0) {
            // 如果没有引用描述页面，则不处理
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String ref : refs) {
            if (Strings.isBlank(ref)) {
                //移除空字符串
                continue;
            }

            boolean isMarkdown = Files.getSuffixName(ref).equalsIgnoreCase("md");

            String desc = Scans.readResource(type.getPackage().getName(), ref);
            desc = desc.replaceAll("\\$\\{PACKAGE\\}", type.getPackage().getName());
            if (isMarkdown) {
                //转换markdown to html
                desc = translateMarkdown(desc);
                sb.append("<div class='md'>").append(desc).append("</div>");
            } else {
                sb.append(desc);
            }
        }
        return sb.toString();
    }

    /**
     * 转换Markdown文件格式.
     *
     * @param desc
     * @return
     */
    private String translateMarkdown(String desc) {
        if (Strings.isBlank(desc)) {
            return "";
        }
        return Markdowns.get().translate(desc);
    }

    /**
     * 转换API样式
     *
     * @param style
     * @return
     */
    private String transApiStyle(ApiStyle style) {
        return style.name();
    }

    /**
     * Trans state.
     *
     * @param state the state
     * @return the string
     */
    private String transState(DevelopmentState state) {
        if (state == DevelopmentState.FINISH) {
            return "已完成";
        } else if (state == DevelopmentState.OBSOLETED) {
            return "已废弃";
        } else if (state == DevelopmentState.PROCESS) {
            return "开发中";
        } else if (state == DevelopmentState.UNSTART) {
            return "未开发";
        }
        return "";
    }

    /**
     * 处理参数.
     *
     * @param clz  the clz
     * @param name the name
     * @return the object info
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalAccessException   the illegal access exception
     * @throws InstantiationException   the instantiation exception
     */
    private ObjectInfo handleParameter(Class<?> clz, String name)
            throws IllegalArgumentException, IllegalAccessException, InstantiationException {

        ObjectInfo p = new ObjectInfo();
        Doc doc = clz.getAnnotation(Doc.class);

        p.name = name == null ? clz.getSimpleName() : name;

        p.title = doc == null ? "" : doc.value();

        if (isPrimitive(clz)) {
            p.type = clz.getSimpleName();
        } else {
            p.type = clz.getName();
        }
        String sum = doc == null ? "" : (Strings.isBlank(doc.desc()) ? doc.value() : doc.desc());

        // 循环处理父类中的解释
        Class<?> superclazz = clz.getSuperclass();
        while (superclazz != null) {
            Doc summary1 = superclazz.getAnnotation(Doc.class);
            if (sum.length() > 0) {
                sum += "<br/>";
            }
            sum += summary1 == null ? "" : summary1.desc();
            superclazz = superclazz.getSuperclass();
        }
        if (doc != null) {
            p.summary = sum + parseRef(clz, doc.refs());
        }

        deeps = new Deeps();
        deeps.push(clz.getName(), deeps.getLevel());
        Object instance = null;

        instance = newInstance(clz);

        for (Field f : clz.getFields()) {
            ObjectInfo fld = handleField(instance, f);
            if (fld != null) {
                p.fields.add(fld);
            }
        }

        if (instance != null)
            p.json = Json.toJson(instance, JsonFormat.full());
        else {
            p.json = "{}";
        }
        return p;
    }

    /**
     * 处理字段.
     *
     * @param instance the instance
     * @param f        the f
     * @return the object info
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalAccessException   the illegal access exception
     * @throws InstantiationException   the instantiation exception
     */
    private ObjectInfo handleField(Object instance, Field f)
            throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        deeps.incLevel();

        ApiField wf = f.getAnnotation(ApiField.class);
        Annotation[] ass = f.getAnnotations();
        if (wf != null) {

            ObjectInfo fi = new ObjectInfo();
            fi.manditary = wf.mandidate();
            fi.title = wf.value();
            fi.example = wf.example();
            fi.name = f.getName();
            fi.type = f.getType().getName();

            // 处理返回代码
            Codes codes = f.getAnnotation(Codes.class);
            if (codes != null) {
                for (Code code : codes.value()) {
                    FieldCode fc = new FieldCode(code.value(), code.desc());
                    fi.codes.add(fc);
                }
            }

            // 处理字段约束
            Size stringConstrain = null;
            NotNull nullConstrain = null;
            Min minConstrain = null;
            Max maxConstrain = null;

            for (Annotation a : ass) {
                if (a instanceof Size) {
                    stringConstrain = (Size) a;
                } else if (a instanceof NotNull) {
                    nullConstrain = (NotNull) a;
                } else if (a instanceof Min) {
                    minConstrain = (Min) a;
                } else if (a instanceof Max) {
                    maxConstrain = (Max) a;
                }
            }

            // 长度约束
            if (stringConstrain != null) {
                fi.minLength = stringConstrain.min();
                fi.maxLength = stringConstrain.max();
            }

            if (minConstrain != null) {
                fi.min = minConstrain.value();
            }
            if (maxConstrain != null) {
                fi.max = maxConstrain.value();
            }
            if (nullConstrain != null) {
                fi.manditary = true;
            }

            // 记录类型的循环次数

            deeps.push(f.getType().getName(), deeps.getLevel());

            // 处理字段
            if (isPrimitive(f.getType())) {
                // String Integer Boolean Double
                tacklePrimitive(instance, f, wf, fi);
            } else if (isList(f)) {
                // List<Object> List<String>
                ArrayList list = new ArrayList();
                Type type = getGenericType(f);

                Logs.get().debug("handle list field " + f.getName() + " with type " + type.getTypeName());

                Class<?> c = null;
                if (type instanceof ParameterizedType) {
                    ParameterizedType t = (ParameterizedType) type;
                    c = (Class<?>) t.getRawType();

                } else if (type instanceof TypeVariable) {

                    c = Object.class;
                } else {
                    c = (Class<?>) type;
                }

                f.set(instance, list);
                fi.type = new StringBuilder().append("List<").append(type.getTypeName()).append(">").toString();

                if (isMap(type.getClass())) {
                    tackleMap(fi, list);

                } else if (isGeneric(c)) {
                    tackleListGenericType(list, f, fi);

                } else {

                    tackleListObject(fi, list, c);
                }
            } else if (isGeneric(f.getType())) {
                tackleGenericType(instance, f, fi);

            } else {
                // 该字段是一个对象类，循环处理此类
                int count = deeps.getPreLevelCount(f.getType().getName(), deeps.getLevel());
                if (count > 2) {
                    // 不处理了，油循环引用
                    deeps.decLevel();
                    return null;
                }

                tackleObject(instance, f, fi);
            }

            deeps.decLevel();
            return fi;
        }
        deeps.decLevel();
        return null;
    }

    /**
     * @param instance
     * @param f
     * @param wf
     * @param fi
     */
    private void tacklePrimitive(Object instance, Field f, ApiField wf, ObjectInfo fi) {
        // 原始数据类型 无需解析子类
        fi.type = f.getType().getSimpleName();

        if (instance != null) {
            // 处理例子
            if (!Strings.isBlank(wf.example())) {
                try {
                    Object obj = Castors.me().castTo(wf.example(), f.getType());
                    Mirror.me(instance).setValue(instance, f, obj);
                } catch (Exception e) {
                    Logs.get().error(f.getName() + "  " + e.getMessage());
                }
            }
        }
    }

    /**
     * @param instance
     * @param f
     * @param fi
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void tackleObject(Object instance, Field f, ObjectInfo fi)
            throws IllegalAccessException, InstantiationException {
        Object cinstance = null;

        cinstance = newInstance(f.getType());
        f.set(instance, cinstance);

        // 读取List数组中对象的Doc注解
        Doc fdoc = cinstance.getClass().getAnnotation(Doc.class);
        if (fdoc != null) {
            fi.summary = fdoc.desc();
        }

        for (Field f1 : f.getType().getFields()) {
            ObjectInfo o = handleField(cinstance, f1);
            if (o != null) {
                fi.fields.add(o);
            }
        }
    }

    /**
     * @param instance
     * @param f
     * @param fi
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void tackleGenericType(Object instance, Field f, ObjectInfo fi)
            throws IllegalAccessException, InstantiationException {
        // 是一个泛型类 处理特殊的注解
        fi.type = "Object";
        fillObjectInfoWithField(f, fi);
        f.set(instance, new Object());
    }

    private void fillObjectInfoWithField(Field f, ObjectInfo fi) throws IllegalAccessException, InstantiationException {
        RuntimeType rt = f.getAnnotation(RuntimeType.class);
        if (rt == null) {
            // 程序员没有添加注解，需要补充完整
            fi.summary = "请程序员添加注解 RuntimeType";
        } else {
            // 处理程序员添加的注解
            for (int i = 0; i < rt.value().length; i++) {
                Class<?> cls = rt.value()[i];
                fi.refs.add(handleParameter(cls, ""));
            }
        }
    }

    /**
     * @param instance
     * @param f
     * @param fi
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void tackleListGenericType(List instance, Field f, ObjectInfo fi)
            throws IllegalAccessException, InstantiationException {
        // 是一个泛型类 处理特殊的注解
        fi.type = "List<Object>";
        fillObjectInfoWithField(f, fi);
        instance.add(new Object());
    }

    /**
     * @param fi
     * @param list
     */
    private void tackleMap(ObjectInfo fi, ArrayList list) {
        if (fi.example == null || fi.example.length() == 0) {
            fi.example = "key:345";
        }
        // List<Map> 对象
        String[] items = fi.example.split(",");
        Map<String, String> mdata = new HashMap<String, String>();

        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            item = Strings.trim(item);
            String[] kv = item.split(":");
            if (kv.length == 1) {
                mdata.put(kv[0], "例子");
            } else {
                mdata.put(kv[0], kv[1]);
            }
        }
        list.add(mdata);
    }

    /**
     * @param fi
     * @param list
     * @param c
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void tackleListObject(ObjectInfo fi, ArrayList list, Class<?> c)
            throws IllegalAccessException, InstantiationException {

        Object cinstance = newInstance(c);
        Logs.get().debug("create instance " + c.getName() + " is " + Json.toJson(cinstance));
        // 处理 DOc fi.summary;

        // 读取List数组中对象的Doc注解
        Doc fdoc = c.getAnnotation(Doc.class);
        if (fdoc != null) {
            fi.summary = fdoc.desc();
        }

        if (isPrimitive(c)) {
            List<Object> listObj = Lang.list4(fi.example);
            if (listObj != null)
                list.addAll(listObj);
        } else {
            // 列表添加2个例子
            list.add(cinstance);
        }

        for (Field f1 : c.getFields()) {
            // 检查是否是循环引用
            ObjectInfo o = handleField(cinstance, f1);
            if (o != null) {
                fi.fields.add(o);
            }
        }
    }

    private Doc getClassDoc(Class<?> cls) {
        Doc doc = cls.getAnnotation(Doc.class);
        return doc;
    }

    /**
     * 是泛型
     *
     * @param c
     * @return
     */
    private boolean isGeneric(Class<?> c) {
        if (c.getSimpleName().equals("Object")) {
            return true;
        }
        return false;
    }

    /**
     * 是Map
     *
     * @param c
     * @return
     */
    private boolean isMap(Class<?> c) {
        if (c.isAssignableFrom(Map.class)) {
            return true;
        }
        return false;
    }

    /**
     * New instance.
     *
     * @param c the c
     * @return the object
     */
    private Object newInstance(Class<?> c) {

        Mirror<?> m = Mirror.me(c);
        if (m.isArray()) {
            return new ArrayList();
        }
        if (m.isInt()) {
            return 0;
        } else if (m.isMap()) {
            return new HashMap<Object, Object>();
        } else if (m.isDouble()) {
            return 0.0;
        } else if (m.isFloat()) {
            return 0.0f;
        } else if (m.isBoolean()) {
            return true;
        } else if (m.isByte()) {
            Byte b = new Byte("0");
            return b;
        } else if (m.isLong()) {
            return new Long(0l);
        } else
            return m.born();
    }

    /**
     * Checks if is list.
     *
     * @param f the f
     * @return true, if is list
     */
    private boolean isList(Field f) {
        if (f.getType().getSimpleName().equals("Object")) {
            return false;
        }
        if (f.getType().isAssignableFrom(List.class)) {
            return true;
        }
        return false;
    }

    /**
     * Gets the generic type.
     *
     * @param f the f
     * @return the generic type
     */
    private Type getGenericType(Field f) {
        if (f.getGenericType() instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) f.getGenericType();
            return pt.getActualTypeArguments()[0];
        }
        return f.getType();
    }

    /**
     * Checks if is primitive.
     *
     * @param c the c
     * @return true, if is primitive
     */
    private boolean isPrimitive(Class<?> c) {
        String name = c.getName();
        int pos = name.lastIndexOf('.');
        if (pos >= 0) {
            name = name.substring(pos + 1);
        }
        String[] ps = {"int", "Integer", "float", "Float", "Double", "double", "long", "Long", "Date", "DateTime",
                "String", "boolean", "Boolean", "char", "short", "byte", "Timestamp"};
        for (String s : ps) {
            if (name.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
