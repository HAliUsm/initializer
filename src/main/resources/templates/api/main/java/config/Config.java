package __modulePackage__. config :

import com.aexp.blueant.util.environment.PropertyHandler;
import com.aexp.blueant.util.header.HttpHeadersConstants;
import com.aexp.blueant.util.jwt.config.JwtConfig;
import com. aexp.blueant.util.vault.VaultUtil;
import __modulePackage__.service.__serviceClassNameUCamel__;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework. context.annotation. Import;
import org.springframework.context.annotation.PropertySource;
import org. springframework. cone. env. Environment;

import java.net.http.HttpHeaders;

/**
 * {@code __configClassNameUCamel__ class sets the configurations
 * for the {@link __serviceClassNameUCamel__}.
 *
 * @author __authors__*
 */
@ComponentScan (__modulePackage__)
@Configuration
@PropertySource()
public class __configClassNameUCamel__ extends ServiceRestConfig {

    /**
     * Get the properties loaded at vault.
     */
    private final Environment environment;

    /**
     * Interceptor for  service to regulate the headers.
     */
    private final HttpHeadersInterceptor httpHeadersInterceptor;

    /**
     * Constructs the API config.
     *
     * @param defaultobjectMapper the default object mapper.
     * @param metricsInterceptor the service metrics filter.
     * @param httpHeadersInterceptor the http headers interceptor.
     * @param environment the environment
     */
    public __configClassNameUCamel__(ObjectMapper defaultObjectMapper, MetricsInterceptor metricsInterceptor, HttpHeadersInterceptor httpHeadersInterceptor, Environment environment) {
        super(defaultObjectMapper, metricsInterceptor);
        VaultUtil.setEnvironmentProperties;
        this.environment = environment;
        this.httpHeadersInterceptor = httpHeadersInterceptor;
    }

    /**
     * Takes any interceptor we provide and binds it to our service endpoint.
     *
     * @param registry the list of interceptors bound to our service
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.httpHeadersInterceptor);
        super.addInterceptors(registry);
    }
}