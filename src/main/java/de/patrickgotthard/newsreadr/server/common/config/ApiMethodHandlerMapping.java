package de.patrickgotthard.newsreadr.server.common.config;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import de.patrickgotthard.newsreadr.server.common.web.ApiController;
import de.patrickgotthard.newsreadr.server.common.web.ApiRequestMapping;
import de.patrickgotthard.newsreadr.shared.annotation.ApiMethod;
import de.patrickgotthard.newsreadr.shared.request.Request;

public class ApiMethodHandlerMapping extends AbstractHandlerMethodMapping<RequestMappingInfo> {

    @Override
    protected boolean isHandler(final Class<?> beanType) {
        return AnnotationUtils.findAnnotation(beanType, ApiController.class) != null;
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(final Method method, final Class<?> handlerType) {

        final ApiRequestMapping apiRequestMapping = AnnotationUtils.findAnnotation(method, ApiRequestMapping.class);
        final Class<? extends Request<?>> requestClass = apiRequestMapping.value();

        final ApiMethod apiMethod = AnnotationUtils.findAnnotation(requestClass, ApiMethod.class);
        final String methodParam = apiMethod.value();

        final PatternsRequestCondition patterns = new PatternsRequestCondition("/api");
        final RequestMethodsRequestCondition methods = new RequestMethodsRequestCondition(new RequestMethod[] {});
        final ParamsRequestCondition params = new ParamsRequestCondition("method=" + methodParam);
        final HeadersRequestCondition headers = new HeadersRequestCondition(new String[] {});
        final ConsumesRequestCondition consumes = new ConsumesRequestCondition(new String[] {});
        final ProducesRequestCondition produces = new ProducesRequestCondition(new String[] {});
        final RequestCondition<?> custom = null;

        final RequestMappingInfo info = new RequestMappingInfo(patterns, methods, params, headers, consumes, produces, custom);
        return info;

    }

    @Override
    protected Set<String> getMappingPathPatterns(final RequestMappingInfo mapping) {
        return mapping.getPatternsCondition().getPatterns();
    }

    @Override
    protected RequestMappingInfo getMatchingMapping(final RequestMappingInfo mapping, final HttpServletRequest request) {
        return mapping.getMatchingCondition(request);
    }

    @Override
    protected Comparator<RequestMappingInfo> getMappingComparator(final HttpServletRequest request) {
        return (info1, info2) -> info1.compareTo(info2, request);
    }

}
