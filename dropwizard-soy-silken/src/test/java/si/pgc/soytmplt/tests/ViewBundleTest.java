package si.pgc.soytmplt.tests;

import com.papercut.silken.TemplateRenderer;
import com.yammer.dropwizard.config.Environment;
import org.junit.Test;
import si.pgc.soy.templates.SoyViewTemplatesBundle;
import si.pgc.soy.templates.config.SoyTmplConfiguration;
import si.pgc.soy.templates.views.SoyViewMessageBodyWriter;
import si.pgc.soy.templates.views.SoyViewRenderer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ViewBundleTest {
    private final Environment environment = mock(Environment.class);
    private final ExampleConfiguration configuration = mock(ExampleConfiguration.class);
    private final SoyTmplConfiguration soyTmplConfig = mock(SoyTmplConfiguration.class);
    private final TemplateRenderer templateRenderer = mock(TemplateRenderer.class);
    private final SoyViewMessageBodyWriter mbw = new SoyViewMessageBodyWriter(
            new SoyViewRenderer(templateRenderer));

    @Test
    public void addsTheViewMessageBodyWriterToTheEnvironment() throws Exception {

        when(soyTmplConfig.getTemplateRenderer()).thenReturn(templateRenderer);
        environment.addProvider(mbw);

        SoyViewTemplatesBundle svb = new SoyViewTemplatesBundle<ExampleConfiguration>() {
            @Override
            public SoyTmplConfiguration getSoyTmplConfiguration(ExampleConfiguration configuration) {
                return soyTmplConfig;
            }
        };

        svb.run(configuration, environment);

        verify(environment).addProvider(mbw);
    }
}
