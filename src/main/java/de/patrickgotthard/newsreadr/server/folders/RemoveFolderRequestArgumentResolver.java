package de.patrickgotthard.newsreadr.server.folders;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import de.patrickgotthard.newsreadr.server.common.web.CustomArgumentResolver;
import de.patrickgotthard.newsreadr.server.common.web.RequestParameters;
import de.patrickgotthard.newsreadr.shared.request.RemoveFolderRequest;

@Component
public class RemoveFolderRequestArgumentResolver implements CustomArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return RemoveFolderRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {

        final Long folderId = RequestParameters.getLong(webRequest, "folderId");

        return new RemoveFolderRequest(folderId);

    }
}
