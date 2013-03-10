package si.pgc.soytmplt.tests;

import com.sun.jersey.core.util.StringKeyIgnoreCaseMultivaluedMap;
import org.junit.Test;
import si.pgc.soy.templates.views.SoyViewMessageBodyWriter;
import si.pgc.soy.templates.views.SoyViewRenderer;
import si.pgc.soy.templates.config.SoyTmplConfiguration;
import si.pgc.soy.templates.views.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ViewMessageBodyWriterTest {
    private static final Annotation[] NONE = { };

    private final SoyTmplConfiguration configuration = new SoyTmplConfiguration();
    private final SoyViewRenderer svr = new SoyViewRenderer(configuration.getTemplateRenderer());

    private final HttpHeaders headers = mock(HttpHeaders.class);
    private final SoyViewMessageBodyWriter writer = new SoyViewMessageBodyWriter(headers, svr);

    @Test
    public void canWriteViews() throws Exception {
        assertThat(writer.isWriteable(MyView.class, MyView.class, NONE, MediaType.TEXT_HTML_TYPE))
                .isTrue();
    }

    @Test
    public void cantWriteNonViews() throws Exception {
        assertThat(writer.isWriteable(String.class, String.class, NONE, MediaType.TEXT_HTML_TYPE))
                .isFalse();
    }

    @Test
    public void writesSoyViews() throws Exception {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final MyView view = new MyView("SUŠA");

        writer.writeTo(view,
                MyView.class,
                null,
                NONE,
                MediaType.TEXT_HTML_TYPE,
                new StringKeyIgnoreCaseMultivaluedMap<Object>(),
                output);

        assertThat(output.toString())
                .isEqualTo("Hello SUŠA!");
    }

    @Test
    public void writesSoyViews2() throws Exception {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final MyOtherView view = new MyOtherView();

        writer.writeTo(view,
                MyView.class,
                null,
                NONE,
                MediaType.TEXT_HTML_TYPE,
                new StringKeyIgnoreCaseMultivaluedMap<Object>(),
                output);

        assertThat(output.toString())
                .isEqualTo("Hello world!");
    }

    @Test
    public void writesErrorMessagesForMissingTemplates() throws Exception {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final UnknownView view = new UnknownView();

        try {
            writer.writeTo(view,
                    MyView.class,
                    null,
                    NONE,
                    MediaType.TEXT_HTML_TYPE,
                    new StringKeyIgnoreCaseMultivaluedMap<Object>(),
                    output);
        } catch (WebApplicationException e) {
            final Response response = e.getResponse();

            assertThat(response.getStatus())
                    .isEqualTo(500);

            assertThat((String) response.getEntity())
                    .isEqualTo("<html><head><title>Template RunTime Exception</title></head><body><p>com.google.common.util.concurrent.UncheckedExecutionException: java.lang.RuntimeException: No soy template files found in namespace: some_not_existing</p></body></html>");
        }
    }

    @Test
    public void writesErrorMessagesForBadTemplates() throws Exception {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final BadView view = new BadView();

        try {
            writer.writeTo(view,
                    MyView.class,
                    null,
                    NONE,
                    MediaType.TEXT_HTML_TYPE,
                    new StringKeyIgnoreCaseMultivaluedMap<Object>(),
                    output);
        } catch (WebApplicationException e) {
            final Response response = e.getResponse();

            assertThat(response.getStatus())
                    .isEqualTo(500);

            assertThat((String) response.getEntity())
                    .isEqualTo("<html><head><title>Template Syntax error</title></head><body><p>com.google.template.soy.base.SoySyntaxException: In file file:/Users/primozsusa/Documents/DEVELOPMENT/intelij_workspace/dropwizard-soy-views/dropwizard-soy-silken/target/test-classes/templates/example/badtemplate.soy:6, template example.badtemplate: Found references to data keys that are not declared in SoyDoc: [wrong]</p></body></html>");
        }
    }
}
