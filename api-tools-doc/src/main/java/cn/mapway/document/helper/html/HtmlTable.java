package cn.mapway.document.helper.html;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class HtmlTable {

    private String title;
    private String summary;

    public HtmlTable() {
        headers = new ArrayList<>();
        rows = new ArrayList<>();
    }

    List<String> headers;
    List<List<String>> rows;

    public HtmlTable setTitle(String title) {
        this.title = title;
        return this;
    }

    public HtmlTable setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public void appendHeader(String... header) {
        for (int i = 0; i < header.length; i++) {
            headers.add(header[i]);
        }
    }

    public void addRow(String... text) {
        ArrayList<String> row = new ArrayList<>();
        for (int i = 0; i < text.length; i++) {
            row.add(text[i]);
        }
        rows.add(row);
    }

    Element div(Document doc, String value) {
        Element div = doc.createElement("div");
        div.setTextContent(value);
        return div;
    }

    public Element toElement(Document doc) {

        Element table = doc.createElement("table");
        Element th = doc.createElement("tr");
        Element title = doc.createElement("td");
        th.appendChild(div(doc, this.title));
        th.appendChild(div(doc, this.summary));
        title.setAttribute("colspan", "5");

        table.appendChild(th);
        th = doc.createElement("th");


        for (int i = 0; i < headers.size(); i++) {
            Element td = doc.createElement("td");
            td.setNodeValue(headers.get(i));
            th.appendChild(td);
        }
        table.appendChild(th);


        for (int i = 0; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            Element tr = doc.createElement("tr");
            for (int j = 0; j < row.size(); j++) {
                Element td = doc.createElement("td");
                td.setNodeValue(row.get(j));
                tr.appendChild(td);
            }
            table.appendChild(tr);
        }
        return table;
    }
}
