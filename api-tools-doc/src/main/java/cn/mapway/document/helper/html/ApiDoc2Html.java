package cn.mapway.document.helper.html;

import cn.mapway.document.helper.markdown.Table;
import cn.mapway.document.module.ApiDoc;
import cn.mapway.document.module.Entry;
import cn.mapway.document.module.Group;
import cn.mapway.document.module.ObjectInfo;
import org.nutz.lang.Lang;
import org.nutz.lang.Xmls;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;


/**
 * ApiDoc 转 Html
 */
public class ApiDoc2Html {

    Document htmldoc;
    Element head;
    Element body;
    Element html;

    public ApiDoc2Html() {
        try {
            htmldoc = Xmls.xmls().newDocument();
            html = this.htmldoc.createElement("html");
            body = htmldoc.createElement("body");
            head = htmldoc.createElement("head");
            htmldoc.appendChild(html);
            html.appendChild(head);
            html.appendChild(body);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


    Table table(ObjectInfo obj) {
        Table table = new Table();
        table.appendHeader("名称", "类型", "长度", "选项", "解释");
        for (ObjectInfo f : obj.fields) {
            table.addRow(f.name, f.type, f.maxLength + "", f.manditary ? "必填" : "选填", f.title + f.summary);
        }
        return table;
    }

    /**
     * 转换文档
     *
     * @param doc
     * @return
     */
    public String convert(ApiDoc doc) {

        StringBuilder sb = new StringBuilder(3000);
        sb.append(head(1, doc.title)).append(line());
        sb.append(line(doc.author + " " + link("参考系统", doc.homeUrl)));
        sb.append(line(""));

        StringBuilder catalog = new StringBuilder();
        StringBuilder body = new StringBuilder();
        Group root = doc.root;

        int count = exportGroup(root, catalog, body);

        sb.append(catalog.toString()).append(body.toString());
        return sb.toString();
    }

    private int exportGroup(Group group, StringBuilder catalog, StringBuilder body) {

        for (Entry entry : group.entries) {
            exportEntry(entry, catalog, body);
        }

        for (Group subGroup : group.getChildGroups()) {
            exportGroup(subGroup, catalog, body);
        }
        return 0;
    }

    private void exportEntry(Entry entry, StringBuilder catalog, StringBuilder body) {
        catalog.append(line(entry.title));

        body.append(head(3, entry.title)).append(line());
        body.append(cite(entry.summary));
        body.append(line());
        body.append("请求URL: " + entry.url).append(line());
        body.append("请求方法: " + Lang.concat(",", entry.invokeMethods)).append(line("\r\n"));

        body.append(bold("请求参数")).append(line());
        if (entry.input.size() == 0) {
            body.append(bold("无需传递参数")).append(line());
        } else {
            for (ObjectInfo obj : entry.input) {
                body.append(table(obj).toString());
            }
            body.append(code("json\r\n", entry.input.get(0).json + "\r\n"));
        }


        body.append(bold("输出参数")).append(line());
        body.append(table(entry.output).toString()).append(line());
        body.append(code("json\r\n", entry.output.json + "\r\n"));

        body.append(line());
        return;
    }
}
