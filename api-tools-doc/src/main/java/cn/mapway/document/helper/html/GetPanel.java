package cn.mapway.document.helper.html;

import cn.mapway.document.module.Entry;
import cn.mapway.document.module.ObjectInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Get Panel
 */
public class GetPanel {
    StringBuilder html = new StringBuilder();

    public void parse(Entry entry) {
        List<GenInfo> temp = new ArrayList<>();
        if (entry.pathParas.size() == 0) {

        } else {


            String url = entry.url;
            ObjTable table = new ObjTable();
            List<ObjectInfo> ps = new ArrayList<>();
            ps.addAll(entry.pathParas);
            ps.addAll(entry.queryParas);
            table.parse("路径参数", "", ps, temp);

            for (int i = 0; i < entry.pathParas.size(); i++) {
                ObjectInfo oi = entry.pathParas.get(i);
                url = url.replaceAll("\\{" + oi.name + "\\}", oi.example == null ? "" : oi.example);
            }
            if (entry.queryParas.size() > 0) {
                url = url + "?";
                for (int i = 0; i < entry.queryParas.size(); i++) {
                    ObjectInfo oi = entry.queryParas.get(i);
                    if (i > 0) {
                        url += "&";
                    }
                    url = url + oi.name + "=" + (oi.example == null ? "*" : oi.example);
                }
            }

            html.append("<div> 访问例子:" + url + "</div>");
            html.append(table.toString());
        }
    }

    public String toString() {
        return html.toString();
    }
}
