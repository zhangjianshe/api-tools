package cn.mapway.document.helper.html;

import cn.mapway.document.module.ApiDoc;
import cn.mapway.document.module.Entry;
import cn.mapway.document.module.Group;


/**
 * ApiDoc 转 Html
 */
public class ApiDoc2Html {


    public ApiDoc2Html() {

    }


    /**
     * 转换文档
     *
     * @param doc
     * @return
     */
    public String convert(ApiDoc doc) {

        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");
        html.append("<style>");
        html.append(" .b{font-weight:bold;}");
        html.append(" .summary{color:green;padding:10px;border:solid 1px #f0f0f0;}");
        html.append(" .input{padding:10px;border-left:solid 3px red;}");
        html.append(" .output{padding:10px;border-left:solid 3px skyblue;}");

        html.append("</style>");
        html.append("</head>");
        html.append("<body>");

        HtmlCatalog catalog = new HtmlCatalog();
        catalog.parse(doc.root);
        html.append(catalog.toString());

        exportEntry(html, doc.root);

        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }

    private void exportEntry(StringBuilder html, Group root) {
        for (Entry entry : root.entries) {
            HtmlEntry htmlEntry = new HtmlEntry(entry);
            html.append(htmlEntry.toHTML());
        }

        for (Group subGroup : root.getChildGroups()) {
            exportEntry(html, subGroup);
        }
    }


}
