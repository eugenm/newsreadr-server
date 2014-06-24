package de.patrickgotthard.newsreadr.server.users;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import de.patrickgotthard.newsreadr.server.common.config.CustomArgumentResolver;
import de.patrickgotthard.newsreadr.server.common.util.ParameterResolver;
import de.patrickgotthard.newsreadr.shared.request.UpdateUserRequest;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@Component
class UpdateUserRequestArgumentResolver implements CustomArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return UpdateUserRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {

        final ParameterResolver resolver = new ParameterResolver(webRequest);

        final Long userId = resolver.getLong("userId");
        final String username = resolver.getString("username");
        final String password = resolver.getString("password");
        final Role role = resolver.getEnum("role", Role.class);

        return new UpdateUserRequest(userId, username, password, role);

    }

}
