package si.pgc.soy.templates.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

import com.google.common.collect.Lists;
import com.papercut.silken.*;
import com.yammer.dropwizard.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.pgc.soy.templates.silken.SilkenFileSetResolver;

import java.util.List;

public class SoyTmplConfiguration extends Configuration {

    private static final Logger logger = LoggerFactory.getLogger(SoyTmplConfiguration.class);

    /**
     * Initialize silken servlet or not. By default servlet is not initialized
     */
    @NotNull
    @JsonProperty
    private Boolean initSilkenServlet = false;

    /**
     * Silken servlet default path
     */
    @NotNull
    @JsonProperty
    private String silkenServletUrl = "/soy";

    /**
     * Dissable silken compile caching
     */
    @NotNull
    @JsonProperty
    private Boolean silkenDisableCaching = false;

    /**
     * default resources folders for soy templates
     */
    @NotNull
    @JsonProperty
    private String silkenSearchPath = "/templates";

    /**
     * Full class name for CompileTimeGlobalsProvider implementation class
     */
    @JsonProperty
    private String silkenCompileTimeGlobalsProvider;

    @JsonIgnore
    private Config silkenConfig;

    @JsonIgnore
    private TemplateRenderer renderer;

    public Config getSilkenConfig() {
        Config config = new Config();
        config.setShowStackTracesInErrors(!silkenDisableCaching);
        config.setDisableCaching(silkenDisableCaching);

        List<String> sharedNamespaces = Lists.newArrayList();
        sharedNamespaces.add("shared");
        config.setSharedNameSpaces(sharedNamespaces);

        config.setFileSetResolver(new SilkenFileSetResolver());
        config.setModelResolver(new RequestAttributeModelResolver());
        //config.setLocaleResolver(new AcceptHeaderLocaleResolver());
        config.setSearchPath("$PATH" + silkenSearchPath);

        if (silkenCompileTimeGlobalsProvider != null) {
            try{
                CompileTimeGlobalsProvider p = (CompileTimeGlobalsProvider) Class.forName(silkenCompileTimeGlobalsProvider)
                        .newInstance();
                config.setCompileTimeGlobalsProvider(p);
            } catch (Exception e) {
                logger.error("Error adding CompileTimeGlobalsProvider", e);
            }
        }

        return config;
    }

    /**
     * Create and return new TemplateRenderer if renderer is not instantiated
     * @return
     */
    public TemplateRenderer getTemplateRenderer() {
        if (renderer == null) {
            renderer = new TemplateRenderer(getSilkenConfig());
        }
        return renderer;
    }

    /**
     * Create and return new TemplateRenderer with new modified config
     * @param config
     * @return
     */
    public TemplateRenderer getTemplateRenderer(Config config) {
        renderer = new TemplateRenderer(config);
        return renderer;
    }

    public String getSilkenServletUrl() {
        return silkenServletUrl;
    }

    public Boolean getSilkenDisableCaching() {
        return silkenDisableCaching;
    }

    public String getSilkenSearchPath() {
        return silkenSearchPath;
    }

    public Boolean getInitSilkenServlet() {
        return initSilkenServlet;
    }
}
