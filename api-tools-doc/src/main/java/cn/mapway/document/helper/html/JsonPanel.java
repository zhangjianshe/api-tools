package cn.mapway.document.helper.html;

public class JsonPanel {

    StringBuilder sb = new StringBuilder();

    public void parse(String json) {
        sb.append("<div style='margin:5px;padding:10px;'>");
        sb.append("<pre>");
        sb.append(json);
        sb.append("</pre>");
        sb.append("</div>");

    }

    public String toString() {
        return sb.toString();
    }
}
