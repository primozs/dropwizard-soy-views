package si.pgc.soy.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import si.pgc.soy.templates.config.SoyTmplConfiguration;

import javax.validation.constraints.NotNull;

public class ExampleConfiguration extends Configuration {

    @NotNull
    @JsonProperty
    SoyTmplConfiguration soyview;

    public SoyTmplConfiguration getSoyTemplateConfiguration() {
        return soyview;
    }

}
