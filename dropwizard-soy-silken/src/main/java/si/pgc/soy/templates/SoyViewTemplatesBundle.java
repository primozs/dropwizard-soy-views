package si.pgc.soy.templates;

import com.papercut.silken.SilkenServlet;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.config.ServletBuilder;
import si.pgc.soy.templates.config.ConfigurationStrategy;
import si.pgc.soy.templates.config.SoyTmplConfiguration;
import si.pgc.soy.templates.views.SoyViewMessageBodyWriter;
import si.pgc.soy.templates.views.SoyViewRenderer;

public abstract class SoyViewTemplatesBundle<T extends Configuration>
        implements ConfiguredBundle<T>, ConfigurationStrategy<T> {

    @Override
    public void initialize(Bootstrap<?> bootstrap) {}

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        final SoyTmplConfiguration soyTmplConfig = getSoyTmplConfiguration(configuration);

        // init servlet
        if (soyTmplConfig.getInitSilkenServlet()) {
            String servletUrl = soyTmplConfig.getSilkenServletUrl();
            Boolean disableCaching = soyTmplConfig.getSilkenDisableCaching();

            SilkenServlet ssrv = new SilkenServlet();
            ServletBuilder servletBuilder = environment.addServlet(ssrv, servletUrl);

            servletBuilder.setName("silken");
            servletBuilder.setInitParam("disableCaching", String.valueOf(disableCaching));
            servletBuilder.setInitParam("showStackTracesInErrors", String.valueOf(!disableCaching));
            servletBuilder.setInitParam("searchPath", "$WEBROOT" + soyTmplConfig.getSilkenSearchPath());
        }

        // add SoyView renderer
        environment.addProvider(
                new SoyViewMessageBodyWriter(
                    new SoyViewRenderer(soyTmplConfig.getTemplateRenderer())));

    }
}
