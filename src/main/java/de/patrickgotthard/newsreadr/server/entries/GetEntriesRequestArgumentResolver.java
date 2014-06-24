package de.patrickgotthard.newsreadr.server.entries;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import de.patrickgotthard.newsreadr.server.common.CustomArgumentResolver;
import de.patrickgotthard.newsreadr.server.common.util.RequestParameters;
import de.patrickgotthard.newsreadr.shared.request.GetEntriesRequest;

@Component
class GetEntriesRequestArgumentResolver implements CustomArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return GetEntriesRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {

        final String feed = RequestParameters.getString(webRequest, "feed");
        final Long latestEntryId = RequestParameters.getLong(webRequest, "latestEntryId");
        final Boolean unreadOnly = RequestParameters.getBoolean(webRequest, "unreadOnly");
        final Integer page = RequestParameters.getInteger(webRequest, "page");

        return new GetEntriesRequest(feed, latestEntryId, unreadOnly, page);

    }

}
