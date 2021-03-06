package cn.mapway.document.ui.client.main;

import cn.mapway.document.ui.client.component.Clients;
import cn.mapway.document.ui.client.component.CloseDialogBox;
import cn.mapway.document.ui.client.module.Entry;
import cn.mapway.document.ui.client.resource.SysResource;
import cn.mapway.document.ui.client.test.TestPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;


/**
 * The Class EntryPanel.
 */
public class EntryPanel extends Composite {

    /**
     * The ui binder.
     */
    private static EntryPanelUiBinder uiBinder = GWT.create(EntryPanelUiBinder.class);
    /**
     * The m entry.
     */
    Entry mEntry;
    /**
     * The java source.
     */
    @UiField
    HTML javaSource;
    /**
     * The lb TITLE.
     */
    @UiField
    Label lbTITLE;
    /**
     * The lb SUMMARY.
     */
    @UiField
    HTML lbSUMMARY;
    /**
     * The lb URL.
     */
    @UiField
    Label lbURL;
    /**
     * The lb AUTHOR.
     */
    @UiField
    Label lbAUTHOR;
    /**
     * The lb INVOKE.
     */
    @UiField
    Label lbINVOKE;
    /**
     * The para in.
     */
    @UiField
    InputParameterPanel paraIn;
    /**
     * The para out.
     */
    @UiField
    OutputParameter paraOut;
    /**
     * The dlg.
     */
    CloseDialogBox dlg;
    /**
     * The test panel.
     */
    TestPanel testPanel = null;
    /**
     * The btn test.
     */
    @UiField
    Button btnTest;

    /**
     * Instantiates a new entry panel.
     */
    public EntryPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        btnTest.setStyleName(SysResource.INSTANCE.getCss().btn());
    }

    /**
     * Parses the.
     *
     * @param e the e
     */
    public void parse(Entry e) {
        mEntry = e;
        lbTITLE.setText(e.title());
        lbSUMMARY.setHTML(e.summary());


        lbURL.setText("接口网址:  " + Clients.calculateInvokePath(e));
        lbAUTHOR.setText("作者:" + e.author());

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < e.invokeMethods().length(); i++) {
            String invokeMethod = e.invokeMethods().get(i);
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(invokeMethod);
        }
        lbINVOKE.setText("调用方法:" + sb.toString());


        paraIn.parseEntry(e);

        paraOut.parse(e.output());

        String html = toJavaMethod(e);


        javaSource.setHTML(html);
    }

    /**
     * 返回调用Java Connector方法
     *
     * @param e
     * @return
     */
    private String toJavaMethod(Entry e) {
        String html = "<p>调用方法:" + e.methodName() + "</p>";
        html += "<p>所属控制器:" + e.parentClassName() + "</p>";
        return html;
    }

    /**
     * @param e
     * @return
     */
    private String toSourceInfo(Entry e) {
        String html = "<p>JAVA源码信息<br/><table cellpadding='5px'>";
        html += "<tr><td>控制类</td><td>" + e.parentClassName() + "</td></tr>";
        html += "<tr><td>控制方法</td><td>" + e.methodName() + "</td></tr>";

        if (e.input().length() > 0) {
            html += "<tr><td>输入参数 </td><td>" + e.input().get(0).type() + "</td></tr>";
        }
        if (e.output() != null) {
            html += "<tr><td>输出参数</td><td>" + e.output().type() + "</td></tr>";
        }
        html += "</table>";
        return html;
    }

    /**
     * On test.
     *
     * @param e the e
     */
    @UiHandler("btnTest")
    void onTest(ClickEvent e) {
        if (dlg == null) {
            dlg = new CloseDialogBox();
            dlg.setText("接口测试");
            testPanel = new TestPanel();
            dlg.setWidget(testPanel);
            dlg.setGlassEnabled(true);
            dlg.setAutoHideEnabled(false);
            dlg.setStyleName(SysResource.INSTANCE.getCss().dlg());
        }
        dlg.center();
        dlg.setText("接口测试-" + mEntry.title());
        dlg.setSummary(Clients.calculateInvokePath(mEntry));
        testPanel.invoke(mEntry);
    }

    /**
     * The Interface EntryPanelUiBinder.
     */
    interface EntryPanelUiBinder extends UiBinder<Widget, EntryPanel> {
    }

}
