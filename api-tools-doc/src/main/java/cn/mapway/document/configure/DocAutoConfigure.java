package cn.mapway.document.configure;


import cn.mapway.document.servlet.MapwayDocServlet;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动配置文档Servlet
 */
@Configuration
@ConditionalOnProperty(prefix = "mapway.apidoc", name = "enabled", havingValue = "true", matchIfMissing = false)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(ApiDocProperties.class)
public class DocAutoConfigure {

    private static Log log = Logs.getLog(DocAutoConfigure.class);

    /**
     * The Properties.
     */
    @Autowired
    ApiDocProperties properties;

    /**
     * Instantiates a new Doc auto configure.
     */
    public DocAutoConfigure() {
        log.info("AutoConfigure start mapway document api servlet");
    }

    /**
     * Api document servlet servlet registration bean.
     *
     * @return the servlet registration bean
     */
    @Bean
    ServletRegistrationBean apiDocumentServlet() {
        ServletRegistrationBean srb = new ServletRegistrationBean();
        MapwayDocServlet servlet = new MapwayDocServlet();
        srb.setServlet(servlet);

        srb.setUrlMappings(Lang.array2list(Strings.split(properties.url, false, ',', ';')));

        Map<String, String> params = new HashMap<String, String>();
        params.put(MapwayDocServlet.PARAM_AUTHOR, properties.author);
        params.put(MapwayDocServlet.PARAM_BASE_PATH, properties.basePath);
        params.put(MapwayDocServlet.PARAM_DOMAIN, properties.domain);
        params.put(MapwayDocServlet.PARAM_SCAN_PACKAGES, properties.scans);
        params.put(MapwayDocServlet.PARAM_SUB_TITLE, properties.subTitle);
        params.put(MapwayDocServlet.PARAM_TITLE, properties.title);
        params.put(MapwayDocServlet.PARAM_COPY_RIGHT, properties.copyright);
        params.put(MapwayDocServlet.PARAM_API_VERSION, properties.version);
        params.put(MapwayDocServlet.PARAM_LOGO_BASE64, properties.logo);
        params.put(MapwayDocServlet.PARAM_HOME_URL, properties.home);
        params.put(MapwayDocServlet.PARAM_CSS_STYLE, properties.style);
        params.put(MapwayDocServlet.PARAM_ENABLE_PRIVATE_FIELD, properties.getEnableParsePrivateField().toString());
        srb.setInitParameters(params);
        srb.setLoadOnStartup(1);
        return srb;
    }
}
