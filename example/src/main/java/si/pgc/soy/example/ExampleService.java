package si.pgc.soy.example;

import com.papercut.silken.*;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import si.pgc.soy.templates.SoyViewTemplatesBundle;
import si.pgc.soy.templates.config.SoyTmplConfiguration;
import si.pgc.soy.example.provider.RuntimeExceptionMapper;
import si.pgc.soy.example.resources.BoatResource;
import si.pgc.soy.example.resources.TimeResource;

public class ExampleService extends Service<ExampleConfiguration> {

    public static void main(String[] args) throws Exception {
        new ExampleService().run(args);
    }

    @Override
    public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
        bootstrap.setName("soyexample");

        // share templates, only required when using soy silken servlet
        bootstrap.addBundle(new AssetsBundle("/templates", "/templates"));

        bootstrap.addBundle(new SoyViewTemplatesBundle<ExampleConfiguration>() {
            @Override
            public SoyTmplConfiguration getSoyTmplConfiguration(ExampleConfiguration configuration) {
                return configuration.getSoyTemplateConfiguration();
            }
        });
    }

    @Override
    public void run(ExampleConfiguration configuration, Environment environment) throws Exception {
        // possible additional silken servlet configuration
        // environment.addServletListeners(new ExampleServletContextListener());
        // environment.addFilter(new SilkenFilter(), "/*");

        // silken template renderer without silken servlet
        // this is additional renderer used as an example of direct renderer usage
        // in BoatResource (getBoat, getBoatRender) note that SoyViewRenderer is using another
        // template renderer instantiated in SoyViewTemplatesBundle
        TemplateRenderer renderer = configuration.getSoyTemplateConfiguration()
                .getTemplateRenderer();

        // runtime exception mapper example
        environment.addProvider(new RuntimeExceptionMapper());

        // resources
        environment.addResource(new BoatResource(renderer));
        environment.addResource(new TimeResource());
    }
}
