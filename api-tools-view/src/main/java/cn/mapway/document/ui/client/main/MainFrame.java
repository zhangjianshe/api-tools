package cn.mapway.document.ui.client.main;

import cn.mapway.document.ui.client.component.CustomButton;
import cn.mapway.document.ui.client.module.ApiDoc;
import cn.mapway.document.ui.client.module.Entry;
import cn.mapway.document.ui.client.module.Group;
import cn.mapway.document.ui.client.module.JarInfo;
import cn.mapway.document.ui.client.resource.SysResource;
import cn.mapway.document.ui.client.rpc.ApiDocProxy;
import cn.mapway.document.ui.client.rpc.Github;
import cn.mapway.document.ui.client.rpc.IOnData;
import cn.mapway.document.ui.client.rpc.github.GithubUser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 * The Class MainFrame.
 */
public class MainFrame extends Composite {

    /**
     * The ui binder.
     */
    private static MainFrameUiBinder uiBinder = GWT.create(MainFrameUiBinder.class);

    /**
     * The Interface MainFrameUiBinder.
     */
    interface MainFrameUiBinder extends UiBinder<Widget, MainFrame> {
    }

    /**
     * The current item.
     */
    TreeItem currentItem = null;

    /**
     * The entry panel.
     */
    EntryPanel entryPanel;

    /**
     * The tree select.
     */
    private SelectionHandler<TreeItem> treeSelect = new SelectionHandler<TreeItem>() {

        @Override
        public void onSelection(SelectionEvent<TreeItem> arg0) {
            TreeItem item = arg0.getSelectedItem();
            if (currentItem != null) {
                currentItem.removeStyleName(SysResource.INSTANCE.getCss().entrySelected());
            }

            handleItem(item);

            currentItem = item;
            currentItem.addStyleName(SysResource.INSTANCE.getCss().entrySelected());
        }

    };

    /**
     * Handle item.
     *
     * @param item the item
     */
    private void handleItem(TreeItem item) {
        if (item.getTitle().length() == 0) {
            Group group = (Group) item.getUserObject();
            showEntryList(group);
        } else {
            Entry e = (Entry) item.getUserObject();
            showEntry(e);
        }

    }

    /**
     * Show entry.
     *
     * @param e the e
     */
    private void showEntry(Entry e) {
        if (entryPanel == null) {
            entryPanel = new EntryPanel();

        }
        if (!entryPanel.isAttached()) {
            content.clear();
            content.add(entryPanel);
        }
        entryPanel.parse(e);
        content.scrollToTop();

    }

    /**
     * The list.
     */
    EntryListPanel list;

    /**
     * Show entry list.
     *
     * @param group the group
     */
    protected void showEntryList(Group group) {
        if (list == null) {
            list = new EntryListPanel();
        }
        if (!list.isAttached()) {
            content.clear();
            content.add(list);
        }
        list.parse(group, "");
        content.scrollToTop();
    }


    /**
     * Instantiates a new main frame.
     */
    public MainFrame() {
        initWidget(uiBinder.createAndBindUi(this));
        logo.setUrl(SysResource.INSTANCE.logo().getSafeUri());

        apiTree.addSelectionHandler(treeSelect);

        adjustUI();
    }

    /**
     * 根据参数调整UI布局
     */
    private void adjustUI() {
        String showTop = Window.Location.getParameter("showTop");
        if (showTop == null || showTop.length() == 0) {
            // 显示TOP
        } else {
            // 不显示TOP
            root.setWidgetHidden(topBar, true);
        }
    }

    /**
     * The doc.
     */
    ApiDoc doc;

    /**
     * The goto word handler.
     */
    private ClickHandler gotoWordHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            Window.open(doc.wordUrl(), "wordExport", "");
        }
    };

    /**
     * The download link handler.
     */
    private ClickHandler downloadLinkHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            CustomButton btn = (CustomButton) event.getSource();
            JarInfo jar = (JarInfo) btn.getData();
            Window.open(jar.link(), "Connecgtor 下载", "");
        }
    };

    /**
     * 获取模板文件中的数据.
     *
     * @return the api doc
     */
    private final native ApiDoc findDocData()/*-{
        if ($wnd.g_data != undefined) {
            return $wnd.g_data;
        }
        return null;
    }-*/;

    /**
     * Inits the.
     *
     * @param target the target
     */
    public void init(String target) {

        ApiDoc doc = findDocData();
        if (doc == null) {
            ApiDocProxy.fetchData(target, new IOnData<ApiDoc>() {
                @Override
                public void onError(String url, String error) {
                    Window.alert(error);
                }

                @Override
                public void onSuccess(String url, ApiDoc data) {
                    parseData(data);
                }
            });
        } else {
            parseData(doc);
        }
    }

    /**
     * Parses the data.
     *
     * @param doc the doc
     */
    void parseData(final ApiDoc doc) {
        this.doc = doc;
        linkSinglePage.setHref("html");

        JsArray<JarInfo> jars = doc.getDownloads();

        if (jars.length() > 0) {
            for (int index = 0; index < jars.length(); index++) {
                JarInfo jar = jars.get(index);
                CustomButton btn = new CustomButton(jar.summary());
                btn.setData(jar);
                btn.addClickHandler(downloadLinkHandler);
                tools.add(btn);
            }
        }

        lbTitle.setText(doc.title());
        apiTree.parseData(doc);
        lbSubtitle.setText(doc.summary());

        if (doc.homeUrl().length() > 0) {
            logo.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            logo.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    Window.Location.assign(doc.homeUrl());
                }
            });
        } else {
            logo.getElement().getStyle().setCursor(Style.Cursor.AUTO);
        }

        //显示相应的节点
        TreeItem selectedTreeItem = null;
        String hasTag = hashTag();
        GWT.log("init " + hasTag);
        if (hasTag != null || hasTag.trim().length() > 0) {
            selectedTreeItem = apiTree.findItem(hasTag);
            GWT.log("cnnot find item " + hasTag);
        }
        if (selectedTreeItem == null) {
            selectedTreeItem = apiTree.getItem(0);
        }

        handleItem(selectedTreeItem);

        if (doc.logo() != null) {
            logo.setUrl(doc.logo());
        }
        apiVersion.setText("版本:" + doc.apiVersion());

        if (doc.domain() != null) {
            anchorDomain.setText(doc.domain());
        }

        if (doc.copyright() != null) {
            lbCopy.setHTML("&copy; " + doc.copyright());
        }


    }

    public native String hashTag()/*-{
        var hash = $wnd.location.hash;
        if (hash == undefined || hash.length == 0) {
            return "";
        }
        return hash.substr(1);
    }-*/;

    @UiHandler("btnLogin")
    public void btnLoginClick(ClickEvent event) {
        if (btnLogin.getText().startsWith("登录")) {
            Github github = new Github();
            github.login("zhangjianshe", "", new IOnData<GithubUser>() {
                @Override
                public void onError(String url, String error) {
                    avatar.setVisible(false);
                    btnLogin.setText("登录Github账号");
                }

                @Override
                public void onSuccess(String url, GithubUser data) {
                    avatar.setVisible(true);
                    avatar.setUrl(data.avatar());
                    btnLogin.setText("退出 " + data.name() + "");
                }
            });
        } else {
            avatar.setVisible(false);
            btnLogin.setText("登录Github账号");
        }
    }


    /**
     * The Api version.
     */
    @UiField
    Label apiVersion;

    /**
     * The api tree.
     */
    @UiField
    ApiTree apiTree;

    /**
     * The content.
     */
    @UiField
    ScrollPanel content;

    /**
     * The lb title.
     */
    @UiField
    Label lbTitle;

    /**
     * The logo.
     */
    @UiField
    Image logo;

    /**
     * The tools.
     */
    @UiField
    HorizontalPanel tools;

    /**
     * sub title.
     */
    @UiField
    Label lbSubtitle;

    /**
     * sub title.
     */
    @UiField
    HTML lbCopy;

    /**
     * sub title.
     */
    @UiField
    Anchor anchorDomain;

    /**
     * The Top bar.
     */
    @UiField
    HorizontalPanel topBar;

    /**
     * The Root.
     */
    @UiField
    DockLayoutPanel root;
    @UiField
    Image avatar;
    @UiField
    Label btnLogin;
    @UiField
    Anchor linkSinglePage;

}
