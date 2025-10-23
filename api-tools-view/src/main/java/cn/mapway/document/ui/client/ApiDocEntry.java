package cn.mapway.document.ui.client;

import cn.mapway.document.ui.client.main.MainFrame;
import cn.mapway.document.ui.client.resource.SysResource;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.ui.RootLayoutPanel;


/**
 * The Class ApiDocEntry.
 */
public class ApiDocEntry implements EntryPoint {

    public static native String find_target_url()/*-{

        if ($wnd.G_DATA_URL != undefined) {
            return $wnd.G_DATA_URL;
        }
        return null;
    }-*/;

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
        String target = GWT.getHostPageBaseURL() + "/doc/data";
        String target_url = find_target_url();
        if (target_url != null) {
            target = target_url;
        }
        GWT.log(target);
        frame.init(target);
    }


}
