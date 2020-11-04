package com.objectcomputing.cubeboard.controllers;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/unauthorized")
public class UnauthorizedController {
    public static final String UNAUTHORIZED_HTML = "<!DOCTYPE html><html><head><title>Unauthorized</title></head><body><h1>Unauthorized</h1></body></html>";

    @Get
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Produces(MediaType.TEXT_HTML)
    public String index() {
        return UNAUTHORIZED_HTML;
    }
}
