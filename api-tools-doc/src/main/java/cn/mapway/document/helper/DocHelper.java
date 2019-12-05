package cn.mapway.document.helper;

import java.io.IOException;
import java.util.List;

import cn.mapway.document.module.ApiDoc;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

import cn.mapway.document.parser.GenContext;
import cn.mapway.document.parser.SpringParser;
import org.nutz.lang.Strings;



/**
 * 辅助类.
 *
 * @author zhangjianshe
 */
public class DocHelper {

    /**
     * The ant home.
     */
    private String antHome = "";

    /**
     * Sets the ant home.
     *
     * @param home the new ant home
     */
    public void setAntHome(String home) {
        antHome = home;
    }

    /**
     * 生成WORD文档.
     *
     * @param api      the api
     * @param fileName the file name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void toWord(ApiDoc api, String fileName) throws IOException {

    }

    /**
     * 生成接口文档JSON格式.
     *  data = data.replaceAll("\\$\\{PACKAGE\\}", pacakgeName);
     * @param pt       the pt
     * @param context  the context
     * @param packages the packages
     * @return the api doc
     */
    public ApiDoc toDoc(ParseType pt, GenContext context, String... packages) {
        if (pt == ParseType.PT_SPRING) {
            SpringParser parser = new SpringParser();
            ApiDoc doc = new ApiDoc();
            try {
                doc = parser.parse(context, packages);
            } catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            return doc;
        }

        return new ApiDoc();
    }

    /**
     * 生成HTML文档.
     *
     * @param pt       the pt
     * @param context  the context
     * @param packages the packages
     * @return the string
     */
    public String genHTML(ParseType pt, GenContext context, String... packages) {
        ApiDoc doc = toDoc(pt, context, packages);
        return genHTML(doc);
    }

    private final String RESOURCE_PACKAGE="cn.mapway.document.resource";
    /**
     * 生成文档.
     * template
     *
     * @param doc the doc
     * @return the string
     */
    public String genHTML(ApiDoc doc) {
        String template="";

        template = Scans.readResource(RESOURCE_PACKAGE, "index.html");
        String ui = Scans.readResource(RESOURCE_PACKAGE, "docui.nocache.js");
        String json = Json.toJson(doc, JsonFormat.tidy());
        String customStyle = "";

        if (!Strings.isBlank(doc.cssStyle)) {
            //用户自定义了样式.
            if (doc.cssStyle.length() > 4) {
                String start = doc.cssStyle.substring(0, 4);
                if (start.compareToIgnoreCase("http") == 0) {
                    customStyle = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + doc.cssStyle + "\">";
                } else {
                    customStyle = "<style>";
                    customStyle += doc.cssStyle;
                    customStyle += "</style>";
                }
            }
        }

        template = template.replace("API_CSS_STYLE", customStyle);
        template = template.replace("API_DOC_DATA", json);
        template = template.replace("API_DOC_UI", ui);
        return template;

    }

    /**
     * The gifdata.
     */
    private static byte[] clearGifData = {0x47, 0x49, 0x46, 0x38, 0x39, 0x61, 0x01, 0x00, 0x01, 0x00, (byte) 0x80, 0x00,
            0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x21, (byte) 0xf9, 0x04,
            0x01, 0x0a, 0x00, 0x01, 0x00, 0x2c, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x02, 0x02, 0x4c,
            0x01, 0x00, 0x3b};

    /**
     * 获取透明 1X1 像素的GIF图片数据.
     *
     * @return the clear gif data
     */
    public static byte[] getClearGifData() {
        return clearGifData;
    }

    /**
     * 生成Word File.
     *
     * @param pt       the pt
     * @param context  the context
     * @param packages the packages
     * @return the string
     */
    public String genDoc(ParseType pt, GenContext context, String... packages) {
        ApiDoc doc = toDoc(pt, context, packages);
        String wordFile = "";

        return wordFile;
    }

    /**
     * Jar.
     *
     * @param api         the api
     * @param path        the path
     * @param packageName the package name
     * @param className   the class name
     * @param libPath     the lib path
     * @return the list
     */
    public List<JarInfo> jar(ApiDoc api, String path, String packageName, String className, String libPath) {
        JarHelper helper = new JarHelper();
        helper.setAntHome(antHome);
        return helper.jar(api, path, packageName, className, libPath);

    }

    /**
     * 输出JAvascript 脚本
     *
     * @param ptSpring     the pt spring
     * @param context      the context
     * @param packageNames the package names
     * @param exportName   the export name
     * @return string
     */
    public String toJavascript(ParseType ptSpring, GenContext context, String packageNames, String exportName) {
        ApiDoc doc = toDoc(ptSpring, context, packageNames);
        JavascriptExport export = new JavascriptExport();
        String data = export.toJavascript(doc, context.getBasepath(), exportName);
        return data;
    }
}
