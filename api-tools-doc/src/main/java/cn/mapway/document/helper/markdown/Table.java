package cn.mapway.document.helper.markdown;

import java.util.ArrayList;
import java.util.List;

public class Table {

    public Table() {
        headers = new ArrayList<>();
        rows = new ArrayList<>();
    }

    List<String> headers;
    List<List<String>> rows;

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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headers.size(); i++) {
            sb.append("|").append(headers.get(i));
        }
        sb.append("|\r\n");
        for (int i = 0; i < headers.size(); i++) {
            sb.append("|").append("---");
        }
        sb.append("|\r\n");

        for (int i = 0; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            for (int j = 0; j < row.size(); j++) {
                sb.append("|").append(row.get(j));
            }
            sb.append("|\r\n");

        }
        sb.append("\r\n\r\n");

        return sb.toString();
    }
}
