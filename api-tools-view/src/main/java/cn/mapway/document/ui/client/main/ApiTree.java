package cn.mapway.document.ui.client.main;

import cn.mapway.document.ui.client.main.storage.LocalStorage;
import cn.mapway.document.ui.client.module.ApiDoc;
import cn.mapway.document.ui.client.module.Entry;
import cn.mapway.document.ui.client.module.Group;
import cn.mapway.document.ui.client.resource.CssStyle;
import cn.mapway.document.ui.client.resource.SysResource;
import cn.mapway.document.ui.client.resource.TreeResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;


/**
 * API接口树 提供一个本地存储，存储各个节点的打开情况.
 *
 * @author zhangjianshe
 */
public class ApiTree extends Tree {

    /**
     * The css.
     */
    CssStyle css;
    ApiDoc apiDoc;
    /**
     * The open handler.
     */
    private final OpenHandler<TreeItem> openHandler = new OpenHandler<TreeItem>() {

        @Override
        public void onOpen(OpenEvent<TreeItem> event) {
            Group g = (Group) event.getTarget().getUserObject();
            setOpen(g.fullName(), true);
        }
    };
    /**
     * The close handler.
     */
    private final CloseHandler<TreeItem> closeHandler = new CloseHandler<TreeItem>() {

        @Override
        public void onClose(CloseEvent<TreeItem> event) {
            Group g = (Group) event.getTarget().getUserObject();
            setOpen(g.fullName(), false);
        }
    };

    /**
     * Instantiates a new api tree.
     */
    public ApiTree() {
        super(new TreeResource(), false);
        css = SysResource.INSTANCE.getCss();

        this.addOpenHandler(openHandler);
        this.addCloseHandler(closeHandler);
    }

    /**
     * 构建TAG标签 .
     *
     * @param tags the tags
     * @return string
     */
    public static String buildTags(String[] tags) {
        if (tags == null) {
            return "";
        }
        String html = "";
        String style = SysResource.INSTANCE.getCss().tag();
        for (String tag : tags) {
            String[] kv = tag.split("/");
            if (kv.length == 2) {
                style = kv[1];
                tag = kv[0];
            }
            html += "<span class='" + style + "'>" + tag + "</span>";
        }
        return html;
    }

    /**
     * Checks if is open.
     *
     * @param key the key
     * @return true, if is open
     */
    public boolean isOpen(String key) {
        String data = LocalStorage.val(key);
        return "1".equals(data);
    }

    /**
     * Sets the open.
     *
     * @param key  the key
     * @param open the open
     */
    public void setOpen(String key, boolean open) {
        LocalStorage.save(key, open ? "1" : "0");
    }

    /**
     * Parses the data.
     *
     * @param data the data
     */
    public void parseData(ApiDoc data) {
        this.clear();
        this.apiDoc = data;
        Group group = data.root();
        //不显示最顶级的节点
        parseGroup(null, group, data);
    }

    public ApiDoc getApiDoc() {
        return apiDoc;
    }

    /**
     * Parses the group.
     *
     * @param root   the root
     * @param group  the group
     * @param apiDoc
     */
    private void parseGroup(TreeItem root, Group group, ApiDoc apiDoc) {

        JsArray<Group> subs = group.subGroups();
        // 处理子节点
        for (int i = 0; i < subs.length(); i++) {
            Group g = subs.get(i);
            TreeItem item = new TreeItem();
            Label lb = new Label(g.name());
            lb.setStyleName(css.group());

            item.setUserObject(g);
            item.setWidget(lb);
            if (root == null) {
                this.addItem(item);
            } else {
                root.addItem(item);
            }
            parseGroup(item, g, apiDoc);
        }
        // 处理方法
        JsArray<Entry> entries = group.entries();
        for (int i = 0; i < entries.length(); i++) {
            Entry e = entries.get(i);
            e.servletPath(apiDoc.servletPath());
            TreeItem item = new TreeItem();
            String tags = buildTags(e.tags());

            String html = "<div >" + ((i + 1) + ".") + tags + e.title() + "</div>";
            HTML widget = new HTML(html);
            item.setWidget(widget);
            item.setUserObject(e);
            item.setTitle("实现类:" + e.parentClassName() + "\r\n方法" + e.methodName());
            if (root == null) {
                this.addItem(item);
            } else {
                root.addItem(item);
            }
        }
        if (root != null) {
            root.setState(isOpen(group.fullName()));
        }
    }

    public TreeItem findItem(String hashTag) {
        for (int i = 0; i < this.getItemCount(); i++) {
            TreeItem item = this.getItem(i);
            TreeItem selected = findItem(item, hashTag);
            if (selected != null) {
                return selected;
            }
        }
        return null;
    }

    /**
     * 根据hashTag找到树中的结点
     *
     * @param hashTag
     * @return
     */
    public TreeItem findItem(TreeItem node, String hashTag) {
        TreeItem selected = null;
        Entry entry = (Entry) node.getUserObject();
        if (entry != null) {
            String url = entry.url();
            GWT.log("compare " + url);
            if (url != null && url.endsWith(hashTag)) {
                return node;
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            TreeItem item = node.getChild(i);
            selected = findItem(item, hashTag);
            if (selected != null) {
                return selected;
            }
        }
        return null;
    }

    /**
     * API样式
     *
     * @param e
     * @return
     */
    private String findApiStyle(Entry e) {
        if (e.style().equalsIgnoreCase("IMPORTANT")) {
            return css.entryImportent();
        } else {
            return css.entry();
        }
    }

}
