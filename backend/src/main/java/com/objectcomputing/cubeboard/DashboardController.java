package com.objectcomputing.cubeboard;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

import java.util.Optional;

@Controller("/api")
public class DashboardController {

    private final DashboardFetcher dashboardFetcher;

    public DashboardController(DashboardFetcher dashboardFetcher) {
        this.dashboardFetcher = dashboardFetcher;
    }

    @Get("/dashboards/{slug}")
    public HttpResponse<?> index(@PathVariable String slug) {
        Optional<Dashboard> dashboardOptional = dashboardFetcher.findBySlug(slug);
        if (dashboardOptional.isPresent()) {
            return HttpResponse.ok(dashboardOptional.get());
        }
        return HttpResponse.notFound();
    }
}
