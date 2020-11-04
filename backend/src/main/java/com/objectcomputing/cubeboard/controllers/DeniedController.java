package com.objectcomputing.cubeboard.controllers;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/denied")
public class DeniedController {
    public static final String DENIED_HTML = "<!DOCTYPE html><html><head><title>Denied</title></head><body><h1>Denied</h1></body></html>";

    @Get
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Produces(MediaType.TEXT_HTML)
    public String index() {
        return DENIED_HTML;
    }
}
