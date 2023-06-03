package cn.mapway.document.ui.client.main;

import cn.mapway.document.ui.client.module.ApiDoc;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * 类图
 */
public class ClassDiagramPanel extends Composite {
    private ApiDoc apiDoc;

    interface ClassDiagramPanelUiBinder extends UiBinder<HTMLPanel, ClassDiagramPanel> {
    }

    private static ClassDiagramPanelUiBinder ourUiBinder = GWT.create(ClassDiagramPanelUiBinder.class);
    @UiField
    HTMLPanel root;

    public ClassDiagramPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setData(ApiDoc apiDoc)
    {
        this.apiDoc = apiDoc;
        toUI();
    }

    private void toUI() {
        root.clear();

    }


}