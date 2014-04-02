package de.patrickgotthard.newsreadr.server.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import de.patrickgotthard.newsreadr.shared.request.RemoveUserRequest;

@Component
public class RemoveUserRequestArgumentResolver implements CustomArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return RemoveUserRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {

        final ParameterResolver resolver = new ParameterResolver(webRequest);

        final Long userId = resolver.getLong("userId");

        return new RemoveUserRequest(userId);

    }

}
