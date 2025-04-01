package -_modulePackage__.controller;

import io.americanexpress.synapse.api.rest.imperative.interceptor.BaseHttpInterceptor;
import io.americanexpress.synapse.framework.exception.ApplicationClientException;
import io.americanexpress.synapse.framework.exception.model.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import static com.americanexpress.security.oneidentity.constant.HttpHeaderConstants.AUTHORIZATION;
import static io.americanexpress.synapse.api.rest.imperative.controller.healthcheck.HealthCheckController.HEALTH_CHECK_ENDPOINT;
import static io.americanexpress.synapse.api.rest.imperative.controller.healthcheck.HealthCheckController.HEALTH_CHECK__GDHA_ENDPOII

/**
 * {@code HttpHeadersInterceptor} is used to regulate the headers portion of the request.
 *
 * @author -_authors__
 */
 @Component
 @XSlf4j
 public class HttpHeadersInterceptor extends BaseHttpInterceptor {

     /**
      * The constant ACTUATOR_ENDPOINT.
     */
    private static final String ACTUATOR_ENDPOINT = "/actuator";

    /**
     * The constant CORRELATION ID literal.
     */
     public static final String AMEX_CORRELATION_ID = "AmEx-Correlation-ID";

     /**
     * The constant SAGA_ID literal.
     */
     public static final String AX_CORRELATION_ID = "AX-Correlation-ID";

    /**
     * Validates request headers if the request contains all the required headers or not.
     *
     * @param httpServletRequest the HTTP server request received
     * @param httpServletResponse the HTTP server response send
     * @param handler provide the handler to execute for typed and/or instance evaluation
     * @return true if the request contains all the required headers, otherwise false
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        if (!shouldNotFilter(httpServletRequest)) {
            validateRequestHttpHeaders(httpServletRequest);
        }
        return true;
    }

    /**
     * The list contains the required headers expected by the api.
     *
     * @return the required HTTP header names
     */
    @Override
    protected List<String> getRequiredHttpHeaderNames) { return List.of(AUTHORIZATION); }

    /**
     * Validates the request HTTP headers.
     *
     * @param httpServletRequest the HTTP server request received
     */
    private void validateRequestHttpHeadersHttpServletRequest httpServletRequest) {
        var missingHeaders = new ArrayList<>();
        for (var requiredHttpHeaderName : getRequiredHttpHeaderNames()) {
            var httpHeaderValue = httpServletRequest.getHeader(requiredHttpHeaderName);
            if (StringUtils.isBlank(httpHeaderValue)) {
                missingHeaders.add(requiredHttpHeaderName);
            }
        }
        if (!missingHeaders.isEmpty()) {
            var missingHeader = StringUtils.join(missingHeaders, ", ");
            throw new ApplicationClientException("Request HTTP Header/s " + missingHeader + " is missing.",
                    ErrorCode.MISSING_HTTP_HEADER_ERROR, missingHeader);
        }
    }

    /**
     * Checks whether the current resource path being hit should be filtered and checked for the required HTTP headers.
     * Currently, the actuator and health check endpoints are not filtered.
     *
     * @param httpServletRequest the http server request received
     * @return true if the resource path should not be filtered for the required headers listed below; false otherwise
     */
    private boolean shouldNotFilter(HttpServletRequest httpServletRequest) {
            return StringUtils.startsWithAny(httpServletRequest.getRequestURI(),
                    HEALTH_CHECK_ENDPOINT, HEALTH_CHECK__GDHA_ENDPOINT, ACTUATOR_ENDPOINT);
    }
}