package cn.mapway.document.helper.html;

import org.nutz.lang.Strings;

import java.util.ArrayList;

public abstract class HtmlTable {

    ArrayList<ArrayList<Cell>> table;

    public HtmlTable() {
        table = new ArrayList<>();
    }

    protected void setCellAttr(int row, int col, int rowspan, int colspan, String text, String css) {
        Cell cell = table.get(row).get(col);
        cell.colspan = colspan;
        cell.rowspan = rowspan;
        cell.text = text;
        if (!Strings.isBlank(css)) {
            cell.css = css;
        }
    }

    protected void initTable(int row, int col) {
        table = new ArrayList<>();
        for (int i = 0; i < row + 1; i++) {
            ArrayList<Cell> rows = new ArrayList<>(col);
            for (int j = 0; j < col; j++) {
                rows.add(new Cell());
            }
            table.add(rows);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1' cellpadding='5px' cellspacing='1px' style='border-color:#f0f0f0;border-collapse:collapse;' >");
        for (int i = 0; i < table.size(); i++) {
            ArrayList<Cell> row = table.get(i);

            sb.append("<tr>");

            for (int j = 0; j < row.size(); j++) {
                Cell cell = row.get(j);
                if (j < getLevel()) {
                    if (cell.text != null) {
                        String style = findStyle(cell.css);

                        sb.append("<td " + style + " colspan='" + cell.colspan + "' rowspan='" + cell.rowspan + "'>").append(cell.text).append("</td>");
                    }
                } else {
                    if (cell.text != null) {
                        String style = findStyle(cell.css);
                        sb.append("<td " + style + "colspan='" + cell.colspan + "' rowspan='" + cell.rowspan + "'>").append(cell.text).append("</td>");
                    }
                }
            }

            sb.append("</tr>");

        }
        sb.append("</table>");
        return sb.toString();
    }

    /**
     * 获取目录的列数
     *
     * @return
     */
    public abstract int getLevel();

    private String findStyle(String css) {
        if (Strings.isBlank(css)) {
            return "";
        } else {
            return "class=\"" + css + "\"";
        }
    }
}
