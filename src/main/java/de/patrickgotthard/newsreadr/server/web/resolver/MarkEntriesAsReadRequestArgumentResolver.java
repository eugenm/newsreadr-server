package de.patrickgotthard.newsreadr.server.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import de.patrickgotthard.newsreadr.shared.request.MarkEntriesAsReadRequest;

@Component
public class MarkEntriesAsReadRequestArgumentResolver implements CustomArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return MarkEntriesAsReadRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {

        final ParameterResolver resolver = new ParameterResolver(webRequest);

        final String feed = resolver.getString("feed");
        final Long latestEntryId = resolver.getLong("latestEntryId");

        return new MarkEntriesAsReadRequest(feed, latestEntryId);

    }

}
