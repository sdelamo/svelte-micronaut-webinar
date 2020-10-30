package com.objectcomputing.cubeboard;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;

@Controller("/api")
public class DashboardsController {

    private final DashboardFetcher dashboardFetcher;

    public DashboardsController(DashboardFetcher dashboardFetcher) {
        this.dashboardFetcher = dashboardFetcher;
    }

    @Get("/dashboards")
    public DashboardPage index() {
        List<Dashboard> dashboardList = dashboardFetcher.findAll();
        DashboardPage page = new DashboardPage();
        page.setCount(dashboardList.size());
        page.setData(dashboardList);
        return page;
    }
}
