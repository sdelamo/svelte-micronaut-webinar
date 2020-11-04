package com.objectcomputing.cubeboard.controllers;

import com.objectcomputing.cubeboard.conf.AppConfiguration;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/ui")
public class UiController {
    private final AppConfiguration appConfiguration;

    public UiController(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get
    public HttpResponse<?> index() {
        return HttpResponse.seeOther(UriBuilder.of(appConfiguration.getLoginRedirect()).build());
    }
}
