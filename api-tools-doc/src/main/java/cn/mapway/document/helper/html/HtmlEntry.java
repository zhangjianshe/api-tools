package cn.mapway.document.helper.html;

import cn.mapway.document.module.Entry;
import org.nutz.lang.Lang;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HtmlEntry {
    Document doc;
    Element title;
    Element method;
    Element url;
    Element author;
    Element example;
    Element root;
    Element summary;


    public HtmlEntry(Document doc) {
        this.doc = doc;
        root = div("");
        title = div("");
        method = div("");
        url = div("");
        author = div("");
        example = div("");
        summary = div("");

        root.appendChild(title);
        root.appendChild(summary);
        root.appendChild(url);
        root.appendChild(method);
        root.appendChild(author);

    }

    Element toElement() {
        return root;
    }

    Element div(String value) {
        Element div = doc.createElement("div");
        div.setTextContent(value);
        return div;
    }

    Element head(int level, String value) {
        Element div = doc.createElement("h" + level);
        div.setTextContent(value);
        return div;
    }

    public HtmlEntry setEntryName(String name) {
        this.title.setTextContent(name);
        return this;
    }

    public HtmlEntry setUrl(String name) {
        this.url.setTextContent(name);
        return this;
    }

    public HtmlEntry setAuthor(String name) {
        this.author.setTextContent(name);
        return this;
    }

    public HtmlEntry setSummary(String name) {
        this.summary.setTextContent(name);
        return this;
    }

    public void parse(Entry entry) {
        setEntryName(entry.title);
        if (entry.summary != null) {
            setSummary(entry.summary);
        }
        setUrl(entry.url);
        setMethod(Lang.concat(',', entry.invokeMethods).toString());
        setAuthor(entry.author);

        HtmlTable response = new HtmlTable();

        root.appendChild(response.toElement(doc));

    }

    private void setMethod(String concat) {
        method.setTextContent(concat);
    }
}
