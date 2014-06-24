package de.patrickgotthard.newsreadr.server.users;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import de.patrickgotthard.newsreadr.server.common.CustomArgumentResolver;
import de.patrickgotthard.newsreadr.server.common.util.RequestParameters;
import de.patrickgotthard.newsreadr.shared.request.AddUserRequest;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@Component
class AddUserRequestArgumentResolver implements CustomArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return AddUserRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {

        final String username = RequestParameters.getString(webRequest, "username");
        final String password = RequestParameters.getString(webRequest, "password");
        final Role role = RequestParameters.getEnum(webRequest, "role", Role.class);

        return new AddUserRequest(username, password, role);

    }

}
