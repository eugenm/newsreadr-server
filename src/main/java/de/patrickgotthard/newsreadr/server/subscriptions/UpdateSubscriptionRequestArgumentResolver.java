package de.patrickgotthard.newsreadr.server.subscriptions;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import de.patrickgotthard.newsreadr.server.common.config.CustomArgumentResolver;
import de.patrickgotthard.newsreadr.server.common.util.ParameterResolver;
import de.patrickgotthard.newsreadr.shared.request.UpdateSubscriptionRequest;

@Component
class UpdateSubscriptionRequestArgumentResolver implements CustomArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return UpdateSubscriptionRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {

        final ParameterResolver resolver = new ParameterResolver(webRequest);

        final Long subscriptionId = resolver.getLong("subscriptionId");
        final Long folderId = resolver.getLong("folderId");
        final String title = resolver.getString("title");

        return new UpdateSubscriptionRequest(subscriptionId, folderId, title);

    }

}
