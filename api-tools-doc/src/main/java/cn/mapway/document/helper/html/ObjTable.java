package cn.mapway.document.helper.html;

import cn.mapway.document.module.FieldCode;
import cn.mapway.document.module.ObjectInfo;
import org.nutz.lang.Strings;

import java.util.List;

/**
 * 输出参数
 */
public class ObjTable extends Para {


    String title;
    String summary;

    public ObjTable() {
    }

    public void parse(ObjectInfo obj, List<GenInfo> objList) {
        title = (obj.title == null ? "" : obj.title + "(" + Strings.escapeHtml(obj.type) + ")");
        summary = (obj.summary == null ? "" : obj.summary);

        initTable(obj.fields.size() + 2, 7);
        fillHeader();
        fillData(obj, objList);
    }

    private void fillData(ObjectInfo obj, List<GenInfo> objList) {
        int row = 3;
        for (int i = 0; i < obj.fields.size(); i++) {

            ObjectInfo o = obj.fields.get(i);
            // 名称 类型 长度 选项 解释

            int col = 0;
            setCellAttr(row, col++, 1, 1, o.name, "");


            if (isPrimitive(o.type)) {
                setCellAttr(row, col++, 1, 1, o.type, "");
            } else {
                boolean hasFind = findObj(o.type, objList);
                if (!hasFind) {
                    GenInfo info = new GenInfo();
                    info.type = o.type;
                    info.obj = o;
                    info.gen = false;
                    objList.add(info);
                }
                setCellAttr(row, col++, 1, 1, "<a href='#" + o.type + "'>" + Strings.escapeHtml(o.type) + "</a>", "");
            }
            setCellAttr(row, col++, 1, 1, o.example, "");

            setCellAttr(row, col++, 1, 1, handleLengthConstrain(o), "");


            String m = (o.manditary == true ? "必须" : "可选");
            setCellAttr(row, col++, 1, 1, m, "");


            // 处理返回值\String msg="代码-------说明<br/>";
            String msg = o.summary + "<br/>";
            if (o.codes.size() > 0) {
                msg += "代码-------说明<br/>";
                for (FieldCode fc : o.codes) {
                    msg += fc.value + "------" + fc.desc;
                }
            }

            String text = "";
            // 处理RuntimeType类型
            if (o.refs.size() > 0) {

                text = "可能的参考对象如下<br/>";

                for (int k = 0; k < o.refs.size(); k++) {
                    ObjectInfo o1 = o.refs.get(k);
                    text += "<a href='#" + o1.type + "'>" + o1.type + "</a><br/>";
                    boolean hasFind = findObj(o1.type, objList);
                    if (!hasFind) {
                        GenInfo info = new GenInfo();
                        info.type = o1.type;
                        info.obj = o1;
                        info.gen = false;
                        objList.add(info);
                    }
                }
            }

            setCellAttr(row, col++, 1, 1, msg + text, "");
            row++;
        }
    }

    private void fillHeader() {

        setCellAttr(0, 0, 1, 6, title, "b");
        setCellAttr(1, 0, 1, 6, summary, "");
        setCellAttr(2, 0, 1, 1, "名称", "b");
        setCellAttr(2, 1, 1, 1, "类型", "b");
        setCellAttr(2, 2, 1, 1, "例子", "b");
        setCellAttr(2, 3, 1, 1, "限制", "b");
        setCellAttr(2, 4, 1, 1, "必填", "b");
        setCellAttr(2, 5, 1, 1, "说明", "b");
    }


}
