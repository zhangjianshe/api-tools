package cn.mapway.document.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import cn.mapway.document.ui.client.main.MainFrame;
import cn.mapway.document.ui.client.resource.SysResource;


/**
 * The Class ApiDocEntry.
 */
public class ApiDocEntry implements EntryPoint {

    /*
     * (non-Javadoc)
     *
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    public void onModuleLoad() {

        SysResource.INSTANCE.getCss().ensureInjected();
        StyleInjector.injectStylesheetAtEnd(SysResource.INSTANCE.main().getText());

        MainFrame frame = new MainFrame();
        RootLayoutPanel.get().add(frame);
        GWT.log("base " + GWT.getHostPageBaseURL() + "/doc/data");
        String target = GWT.getHostPageBaseURL() + "/doc/data";
        frame.init(target);
    }

}
