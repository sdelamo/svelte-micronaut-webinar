package com.objectcomputing.cubeboard.controllers;

import com.objectcomputing.cubeboard.Dashboard;
import com.objectcomputing.cubeboard.DashboardFetcher;
import com.objectcomputing.cubeboard.versioning.ApiVersion;
import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.Optional;

@Controller("/api")
public class DashboardController {

    private final DashboardFetcher dashboardFetcher;

    public DashboardController(DashboardFetcher dashboardFetcher) {
        this.dashboardFetcher = dashboardFetcher;
    }

    @Version(ApiVersion.V1)
    @Get("/dashboards/{slug}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<?> index(@PathVariable String slug) {
        Optional<Dashboard> dashboardOptional = dashboardFetcher.findBySlug(slug);
        if (dashboardOptional.isPresent()) {
            return HttpResponse.ok(dashboardOptional.get());
        }
        return HttpResponse.notFound();
    }
}
