package com.objectcomputing.cubeboard;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

@Controller("/api")
public class DashboardsController {

    private final DashboardFetcher dashboardFetcher;

    public DashboardsController(DashboardFetcher dashboardFetcher) {
        this.dashboardFetcher = dashboardFetcher;
    }

    @Get("/dashboards")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public DashboardPage index() {
        List<Dashboard> dashboardList = dashboardFetcher.findAll();
        DashboardPage page = new DashboardPage();
        page.setCount(dashboardList.size());
        page.setData(dashboardList);
        return page;
    }
}
