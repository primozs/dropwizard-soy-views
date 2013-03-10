package si.pgc.soy.templates.views;

import com.google.common.base.Charsets;
import com.papercut.silken.TemplateRenderer;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

public class SoyViewRenderer implements ViewRenderer {

    TemplateRenderer renderer;

    public SoyViewRenderer(TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public boolean isRenderable(SoyView view) {
        return true;
    }

    @Override
    public void render(SoyView view,
                       Locale locale,
                       OutputStream output) throws IOException, WebApplicationException {
        try {
            /*
                Here could be also dumb locale by soy namespace selection.
                Instead of "someNamespace" we would call "someNamespace_sl_SI",
                which would be the same soy template with slo language. In this case
                soy msg command would be useless. Problem is, if the template would
                not be found in repository this would code would break and we would
                have problem with default language fallback.
            */

            // force slo test
            //locale = new Locale("sl", "SI");

            // this should be probably solved with injectable locale provider
            if (view.getLocale() != null) {
                locale = view.getLocale();
            }

            // ijData injected data is used for run time globals include
            String rendered = renderer.render(view.getTemplateName(),
                    view.getData(), view.getIjData(), locale);

            output.write(rendered.getBytes(Charsets.UTF_8));
        }catch (RuntimeException e){
            if (e.getMessage().startsWith("com.google.template.soy.base.SoySyntaxException:")) {
                throw new ViewRenderException(e.getMessage());
            }
            throw new RuntimeException(e);
        }finally {
            output.close();
        }
    }

}
