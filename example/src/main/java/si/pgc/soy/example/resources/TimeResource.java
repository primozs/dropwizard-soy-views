package si.pgc.soy.example.resources;

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.core.HttpContext;
import si.pgc.soy.example.model.Time;
import si.pgc.soy.templates.views.SoyView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Path("/time")
public class TimeResource {

    @GET
    @Path("/now_json")
    @Produces(MediaType.APPLICATION_JSON)
    public Time currentTimeJSON() {

        String nowString = SimpleDateFormat.getDateTimeInstance().format(new Date());
        Long nowLong = System.currentTimeMillis();

        Time t = new Time();
        t.timeLong = nowLong;
        t.timeString = nowString;

        return t;
    }

    @GET @Path("/now_soy")
    public SoyView currentTimeUsingSoy(@Context HttpContext httpContext) {

        // locale from context, user or where ever
        Locale locale = new Locale("sl", "SI");

        String now = SimpleDateFormat.getDateTimeInstance().format(new Date());
        return new SoyView("playground.time.TimeDisplay", ImmutableMap.of("time", now), null, locale);
    }
}
