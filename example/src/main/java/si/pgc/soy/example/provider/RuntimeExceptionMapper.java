package si.pgc.soy.example.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.pgc.soy.templates.views.SoyView;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
* <p>Provider to provide the following to Jersey framework:</p>
* <ul>
* <li>Provision of general runtime exception to response mapping</li>
* </ul>
*/
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private final Logger logger = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException runtime) {
        // Build default response
        Response defaultResponse = Response
                .serverError()
                .entity(new SoyView("error.e500"))
                .build();

        // Check for any specific handling
        if (runtime instanceof WebApplicationException) {
            return handleWebApplicationException(runtime, defaultResponse);
        }

        // Use the default
        logger.error(runtime.getMessage(), runtime);
        return defaultResponse;
    }

    private Response handleWebApplicationException(RuntimeException exception, Response defaultResponse) {
        WebApplicationException webAppException = (WebApplicationException) exception;

        // No logging
        if (webAppException.getResponse().getStatus() == 401) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(new SoyView("error.e401"))
                    .build();
        }
        if (webAppException.getResponse().getStatus() == 404) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new SoyView("error.e404"))
                    .build();
        }

        // Error logging
        logger.error(exception.getMessage(), exception);

        return defaultResponse;
    }
}
