package si.pgc.soy.templates.views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;

import java.nio.charset.Charset;
import java.util.Locale;

public class SoyView<T> {

    private final String templateName;
    private final Timer renderingTimer;
    private final Charset charset;
    private final T data;
    private final Object ijData;
    private final Locale locale;

    public SoyView(String templateName) {
        this.templateName = resolveName(templateName);
        this.charset = Charset.forName(Charsets.UTF_8.name());
        this.data = null;
        this.ijData = null;
        this.renderingTimer = Metrics.defaultRegistry().newTimer(getClass(), "rendering");
        this.locale = null;
    }

    public SoyView(String templateName, T data) {
        this.templateName = resolveName(templateName);
        this.charset = Charset.forName(Charsets.UTF_8.name());
        this.data = data;
        this.ijData = null;
        this.renderingTimer = Metrics.defaultRegistry().newTimer(getClass(), "rendering");
        this.locale = null;
    }

    public SoyView(String templateName, T data, Object ijData) {
        this.templateName = resolveName(templateName);
        this.charset = Charset.forName(Charsets.UTF_8.name());
        this.data = data;
        this.ijData = ijData;
        this.renderingTimer = Metrics.defaultRegistry().newTimer(getClass(), "rendering");
        this.locale = null;
    }

    public SoyView(String templateName, T data, Object ijData, Locale locale) {
        this.templateName = resolveName(templateName);
        this.charset = Charset.forName(Charsets.UTF_8.name());
        this.data = data;
        this.ijData = ijData;
        this.renderingTimer = Metrics.defaultRegistry().newTimer(getClass(), "rendering");
        this.locale = locale;
    }

    private String resolveName(String templateName) {
        return templateName;
    }

    @JsonIgnore
    public String getTemplateName() {
        return templateName;
    }

    @JsonIgnore
    public Optional<Charset> getCharset() {
        return charset != null ? Optional.of(charset) : Optional.<Charset>absent();
    }

    @JsonIgnore
    Timer getRenderingTimer() {
        return renderingTimer;
    }

    public T getData() {
        return data;
    }

    public Object getIjData() {
        return ijData;
    }

    public Locale getLocale() {
        return locale;
    }
}