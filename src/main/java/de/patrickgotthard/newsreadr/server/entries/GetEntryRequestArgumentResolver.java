package de.patrickgotthard.newsreadr.server.entries;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import de.patrickgotthard.newsreadr.server.common.web.CustomArgumentResolver;
import de.patrickgotthard.newsreadr.server.common.web.RequestParameters;
import de.patrickgotthard.newsreadr.shared.request.GetEntryRequest;

@Component
class GetEntryRequestArgumentResolver implements CustomArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return GetEntryRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {

        final Long userEntryId = RequestParameters.getLong(webRequest, "userEntryId");

        return new GetEntryRequest(userEntryId);

    }

}
