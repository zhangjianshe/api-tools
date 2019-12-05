/******************************************************************************
 <pre>

 =============================================================
 -   ____ _  _ ____ _  _ ____  _ _ ____ _  _ ____ _  _ ____  -
 -    __] |__| |__| |\ | | __  | | |__| |\ | [__  |__| |___  -
 -   [___ |  | |  | | \| |__| _| | |  | | \| ___] |  | |___  -
 -           http://hi.baidu.com/zhangjianshe                -
 =============================================================

 </pre>
 *******************************************************************************/
package cn.mapway.document.resource;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 模板类.
 *
 * @author zhangjianshe @navinfo.com
 */
public class Template {

    private final static Log log = Logs.getLog(Template.class);

    /**
     * 读取相对资源路径.
     *
     * @param fileName the file name
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public final static String readTemplate(String fileName) throws IOException {

        InputStream in = Template.class.getResourceAsStream(fileName);

        String returnText = "error to read file @" + fileName;
        if (in != null) {
            log.info("read file @ " + fileName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int count = 0;
            byte[] bytes = new byte[1024];
            count = in.read(bytes);
            while (count > 0) {
                out.write(bytes, 0, count);
                count = in.read(bytes);
            }
            returnText = new String(out.toByteArray(), "UTF-8");
            in.close();
        } else {
            log.info("read file @ " + fileName + ",but sorry i can not find it!");
        }
        return returnText;
    }
}
