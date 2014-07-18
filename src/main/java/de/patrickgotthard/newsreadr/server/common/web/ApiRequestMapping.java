package de.patrickgotthard.newsreadr.server.common.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.patrickgotthard.newsreadr.shared.request.Request;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRequestMapping {

    Class<? extends Request<?>> value();

}
