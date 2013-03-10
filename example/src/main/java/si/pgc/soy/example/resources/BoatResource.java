package si.pgc.soy.example.resources;

import com.google.common.collect.ImmutableMap;
import com.papercut.silken.SilkenServlet;
import com.papercut.silken.TemplateRenderer;
import com.sun.tools.javac.util.List;
import si.pgc.soy.example.model.Boat;
import si.pgc.soy.templates.views.SoyView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Path("/")
public class BoatResource {

    TemplateRenderer renderer;

    public BoatResource(TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    @GET
    @Path("boat/ex")
    @Produces(MediaType.APPLICATION_JSON)
    public Boat getBoat(@Context Request request) {
        Boat b = new Boat("Elvira", 1);

        // rendering with Servlet template renderer
        // will fail if config is not set to init silken servlet. default is false
        TemplateRenderer renderer = SilkenServlet.getTemplateRenderer();
        String render = renderer.render("example.template", null);

        ImmutableMap<String, ?> map = ImmutableMap.of("productName", "Australia II", "qty", 19);
        render = renderer.render("example.remaining", map);

        render = renderer.render("example.simple.helloWorld", null);
        render = renderer.render("example.simple.helloName", ImmutableMap.of("name", "Elvira"));

        map = ImmutableMap.of("name", "Erik", "additionalNames", List.of("Saš", "Ck", "Taras"));
        render = renderer.render("example.simple.helloNames", map);

        return b;
    }

    @GET
    @Path("boat/exrender")
    @Produces(MediaType.APPLICATION_JSON)
    public Boat getBoatRender() {
        Boat b = new Boat("Elvira", 2);

        // rendering with injected templated renderer
        String render = renderer.render("example.template", null);

        ImmutableMap<String, ?> map = ImmutableMap.of("productName", "Australia II", "qty", 19);
        render = renderer.render("example.remaining", map);

        render = renderer.render("example.simple.helloWorld", null);
        render = renderer.render("example.simple.helloName", ImmutableMap.of("name", "Elvira"));

        map = ImmutableMap.of("name", "Erik", "additionalNames", List.of("Saš", "Ck", "Taras"));
        render = renderer.render("example.simple.helloNames", map);

        return b;
    }

    @GET
    @Path("boat/exview")
    public SoyView getBoatView() {
        return new SoyView("example.template");
    }

    @GET
    @Path("boat/exview_data")
    public SoyView index() {
        ImmutableMap<String, ?> map = ImmutableMap.of("name", "Erik",
                "additionalNames", List.of("Saš", "Ck", "Taras"));

        return new SoyView("example.simple.helloNames", map);
    }

    /**
     * This is not working at the moment maybe it works with silkenservlet
     * @return
     */
    @GET
    @Path("boat/global")
    public SoyView someGlobal() {

        ImmutableMap<String, ?> globals = ImmutableMap.of(
                "username", "primoz",
                "userAgent", "chrome",
                "someothervar", "SOME OTHER INJECTED GLOBAL VAR DIRECTLY FROM RESOURCE");

        return new SoyView("example.global", null, globals);
    }

    @GET
    @Path("boat/exception")
    public Response throwException() {

        // will throw an error and runtime exception mapper will render error.e500
        Integer i = 1;
        if(i.equals(1)) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.status(Response.Status.OK).build();
    }
}
