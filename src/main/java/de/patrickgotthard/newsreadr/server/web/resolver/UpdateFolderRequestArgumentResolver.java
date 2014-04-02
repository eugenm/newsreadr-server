package de.patrickgotthard.newsreadr.server.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import de.patrickgotthard.newsreadr.shared.request.UpdateFolderRequest;

@Component
public class UpdateFolderRequestArgumentResolver implements CustomArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return UpdateFolderRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {

        final ParameterResolver resolver = new ParameterResolver(webRequest);

        final Long folderId = resolver.getLong("folderId");
        final String title = resolver.getString("title");

        return new UpdateFolderRequest(folderId, title);

    }

}
