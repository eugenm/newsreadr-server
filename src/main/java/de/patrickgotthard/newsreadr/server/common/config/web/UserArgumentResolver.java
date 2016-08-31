package de.patrickgotthard.newsreadr.server.common.config.web;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import de.patrickgotthard.newsreadr.server.common.rest.NewsreadrUserDetails;

@Component
class UserArgumentResolver implements CustomArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return long.class.equals(parameter.getParameterType()) && "currentUserId".equals(parameter.getParameterName());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory) throws Exception {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        final Object principal = authentication.getPrincipal();
        final NewsreadrUserDetails userDetails = (NewsreadrUserDetails) principal;
        return userDetails.getId();
    }

}
