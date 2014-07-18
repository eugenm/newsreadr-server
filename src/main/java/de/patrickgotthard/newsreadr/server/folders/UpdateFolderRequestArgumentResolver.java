package de.patrickgotthard.newsreadr.server.folders;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import de.patrickgotthard.newsreadr.server.common.web.CustomArgumentResolver;
import de.patrickgotthard.newsreadr.server.common.web.RequestParameters;
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

        final Long folderId = RequestParameters.getLong(webRequest, "folderId");
        final String title = RequestParameters.getString(webRequest, "title");

        return new UpdateFolderRequest(folderId, title);

    }

}
