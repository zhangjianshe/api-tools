package cn.mapway.document.helper.html;


import cn.mapway.document.module.Entry;
import cn.mapway.document.module.Group;
import org.nutz.lang.Lang;

public class HtmlCatalog extends HtmlTable {


    private int catalogLevel = 0;
    private int totalCount = 0;

    public void parse(Group group) {


        catalogLevel = catalogLevel(group);
        totalCount = total(group);

        initTable(totalCount, catalogLevel + 6);
        fillHeader();
        index = 1;
        fillData(1, 0, group);

    }

    private void fillHeader() {
        setCellAttr(0, 0, 1, catalogLevel, "分类", "b");
        setCellAttr(0, catalogLevel, 1, 1, "序号", "b");
        setCellAttr(0, catalogLevel + 1, 1, 1, "名称", "b");
        setCellAttr(0, catalogLevel + 2, 1, 1, "URI", "b");
        setCellAttr(0, catalogLevel + 3, 1, 1, "作者", "b");
        setCellAttr(0, catalogLevel + 4, 1, 1, "状态", "b");
        setCellAttr(0, catalogLevel + 5, 1, 1, "标签", "b");

    }


    int index = 0;

    /**
     * 填充目录
     *
     * @param group
     */
    private int fillData(int row, int col, Group group) {

        int count = total(group);
        if (count > 1) {
            setCellAttr(row, col, count, 1, group.name, "");
        } else {
            setCellAttr(row, col, 1, catalogLevel - col, group.name, "");
        }

        int entryCol = catalogLevel;
        int entryRow = row;
        for (Entry entry : group.entries) {
            entryCol = catalogLevel;
            setCellAttr(entryRow, entryCol++, 1, 1, (index++) + "", "");
            setCellAttr(entryRow, entryCol++, 1, 1, "<a href='#" + entry.url + "'>" + entry.title + "</a>", "");
            setCellAttr(entryRow, entryCol++, 1, 1, entry.url, "");
            setCellAttr(entryRow, entryCol++, 1, 1, entry.author, "");
            setCellAttr(entryRow, entryCol++, 1, 1, entry.state, "");
            setCellAttr(entryRow, entryCol++, 1, 1, entry.tags.size() > 0 ? Lang.concat(",", entry.tags).toString() : "&nbsp;", "");
            entryRow++;
        }
        if (count > 1) {
            if (group.entries.size() > 0 && col < catalogLevel - 1) {
                setCellAttr(row, col + 1, group.entries.size(), catalogLevel - col - 1, "--", "");
            }
        }

        int row1 = row + group.entries.size();
        for (Group subGroup : group.getChildGroups()) {
            row1 = row1 + fillData(row1, col + 1, subGroup);
        }
        return count;
    }


    /**
     * 目录的层级
     *
     * @param g
     * @return
     */
    private int catalogLevel(Group g) {
        int maxLevel = 1;
        if (g.getChildGroups().size() == 0)
            return maxLevel;


        int temp = 0;
        for (Group subGroup : g.getChildGroups()) {
            int level = catalogLevel(subGroup);
            if (level > temp) {
                temp = level;
            }
        }
        return maxLevel + temp;
    }

    /**
     * 总共的接口数量
     *
     * @param g
     * @return
     */
    private int total(Group g) {
        int count = g.entries.size();

        for (Group subGroup : g.getChildGroups()) {
            count = count + total(subGroup);
        }
        return count;
    }


    @Override
    public int getLevel() {
        return catalogLevel;
    }
}
