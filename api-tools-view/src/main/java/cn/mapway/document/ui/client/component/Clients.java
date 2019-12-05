package cn.mapway.document.ui.client.component;

import cn.mapway.document.ui.client.module.Entry;
import com.google.gwt.user.client.Window;


/**
 * The Class Clients.
 */
public class Clients {

    /**
     * 获取主机网络地址.
     *
     * @return the host port
     */
    public static final String getHostPort() {
        String host = Window.Location.getHost();
        String protocol = Window.Location.getProtocol();
        String r = protocol + "//" + host + "/";
        return r;
    }

    /**
     * 计算调用的URL
     *
     * @param e
     * @return
     */
    public static String calculateInvokePath(Entry e) {
        String loc = Window.Location.getHref();
        int location = loc.lastIndexOf(e.servletPath());
        if (location > 0) {
            return loc.substring(0, location) + "/" + e.url();
        } else {
            return getHostPort() + e.url();
        }
    }
}
