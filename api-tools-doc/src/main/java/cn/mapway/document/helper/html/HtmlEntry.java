package cn.mapway.document.helper.html;

import cn.mapway.document.module.Entry;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HtmlEntry {


    private static Logger logger = Logger.getLogger(HtmlEntry.class.toGenericString());

    StringBuilder html;

    public HtmlEntry(Entry entry) {

        html = new StringBuilder();
        parse(entry);
    }

    public void parse(Entry entry) {

        html = new StringBuilder();
        html.append("<a name='" + entry.url + "'/>");
        html.append("<h3>" + entry.title + "</h3>");
        html.append("<div>访问地址：" + entry.url + "</div>");
        html.append("<div>调用方法：" + Lang.concat(",", entry.invokeMethods) + "</div>");

        if (!Strings.isBlank(entry.summary)) {
            html.append("<div class='summary'>").append(entry.summary).append("</div>");
        }
        List<GenInfo> objList = new ArrayList<>();

        html.append("<div class='input'> <div > 输入参数</div>");
        ObjTable inputPara = new ObjTable();

        GetPanel panel = new GetPanel();
        panel.parse(entry);
        html.append(panel.toString());

        if (entry.input.size() > 0) {
            inputPara.parse(entry.input.get(0), objList);
            html.append(inputPara.toString());
            JsonPanel jp = new JsonPanel();
            jp.parse(entry.input.get(0).json);
            html.append(jp.toString());
        }

        html.append("</div>");

        html.append("<div class='output'> <div > 输出参数</div>");
        Outputparam op = new Outputparam();
        op.parse(entry, objList);
        html.append(op.toString());
        JsonPanel jp = new JsonPanel();
        jp.parse(entry.output.json);
        html.append(jp.toString());
        html.append("</div>");

        exportAllObject(html, objList);
    }

    private void exportAllObject(StringBuilder html, List<GenInfo> gens) {
        while (needContinue(gens)) {
            List<GenInfo> gens2 = new ArrayList<GenInfo>();
            for (GenInfo info : gens) {
                if (info.gen == false) {

                    ObjTable p = new ObjTable();
                    p.parse(info.obj, gens2);
                    html.append("<div >&nbsp;</div>");
                    html.append("<a name='" + info.type + "' />");
                    html.append(p.toString());
                    info.gen = true;
                }
            }
            merge(gens, gens2);
        }
    }

    /**
     * Merge.
     *
     * @param gens  the gens
     * @param gens2 the gens 2
     */
    private void merge(List<GenInfo> gens, List<GenInfo> gens2) {
        for (GenInfo info : gens2) {
            boolean find = false;
            for (GenInfo gen : gens) {
                if (gen.type.equals(info.type)) {
                    find = true;
                    break;
                }
            }

            if (find == false) {
                gens.add(info);
            }
        }
    }

    /**
     * Need continue.
     *
     * @param gens2 the gens 2
     * @return true, if successful
     */
    private boolean needContinue(List<GenInfo> gens2) {
        for (GenInfo info : gens2) {
            if (info.gen == false) {
                return true;
            }
        }
        return false;
    }

    public String toHTML() {
        return html.toString();
    }
}
