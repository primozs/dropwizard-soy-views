package si.pgc.soy.templates.config;

import com.yammer.dropwizard.config.Configuration;

public interface ConfigurationStrategy<T extends Configuration> {
    SoyTmplConfiguration getSoyTmplConfiguration(T configuration);
}
