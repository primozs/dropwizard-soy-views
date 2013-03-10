package si.pgc.soy.templates.views;

import com.google.common.annotations.VisibleForTesting;
import com.yammer.metrics.core.TimerContext;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

@Provider
@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XHTML_XML})
public class SoyViewMessageBodyWriter implements MessageBodyWriter<SoyView> {

    private static final String MISSING_TEMPLATE_MSG =
            "<html>" +
                    "<head><title>Template RunTime Exception</title></head>" +
                    "<body><p>{0}</p></body>" +
                    "</html>";
    private static final String TEMPLATE_SYNTAX_ERR_MSG =
            "<html>" +
                    "<head><title>Template Syntax error</title></head>" +
                    "<body><p>{0}</p></body>" +
                    "</html>";


    @Context
    @SuppressWarnings("FieldMayBeFinal")
    private HttpHeaders headers;

    private SoyViewRenderer renderer;

    @SuppressWarnings("UnusedDeclaration")
    public SoyViewMessageBodyWriter(SoyViewRenderer renderer) {
        this(null, renderer);
    }

    @VisibleForTesting
    public SoyViewMessageBodyWriter(HttpHeaders headers, SoyViewRenderer renderer) {
        this.headers = headers;
        this.renderer = renderer;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return SoyView.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(SoyView t,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(SoyView t,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException, WebApplicationException {
        final TimerContext context = t.getRenderingTimer().time();
        try {
            if (renderer.isRenderable(t)) {
                renderer.render(t, detectLocale(headers), entityStream);
                return;
            }
            throw new ViewRenderException("Unable to find a renderer for " + t.getTemplateName());
        } catch (ViewRenderException e) {
            final String msg = MessageFormat.format(TEMPLATE_SYNTAX_ERR_MSG, e.getMessage());
            throw new WebApplicationException(Response.serverError()
                    .type(MediaType.TEXT_HTML_TYPE)
                    .entity(msg)
                    .build());
        }
        catch (RuntimeException e) {
            final String msg = MessageFormat.format(MISSING_TEMPLATE_MSG, e.getMessage());
            throw new WebApplicationException(Response.serverError()
                    .type(MediaType.TEXT_HTML_TYPE)
                    .entity(msg)
                    .build());
        }
        finally {
            context.stop();
        }
    }

    private Locale detectLocale(HttpHeaders headers) {
        final List<Locale> languages = headers.getAcceptableLanguages();
        for (Locale locale : languages) {
            return locale;
        }
        return Locale.getDefault();
    }
}
