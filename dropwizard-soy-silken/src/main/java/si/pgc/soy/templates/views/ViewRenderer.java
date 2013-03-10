package si.pgc.soy.templates.views;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

public interface ViewRenderer {
    boolean isRenderable(SoyView view);

    void render(SoyView view, Locale locale, OutputStream output) throws IOException, WebApplicationException;
}
