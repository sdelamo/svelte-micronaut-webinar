package com.objectcomputing.cubeboard;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;

@Controller("/api")
public class WidgetsController {

    private final WidgetFetcher widgetFetcher;

    public WidgetsController(WidgetFetcher widgetFetcher) {
        this.widgetFetcher = widgetFetcher;
    }

    /**
     *
     * @return Get a collection of the user's widget
     */
    @Get("/widgets")
    public WidgetPage index() {
        List<Widget> widgetList = widgetFetcher.findAll();
        WidgetPage page = new WidgetPage();
        page.setCount(widgetList.size());
        page.setData(widgetList);
        return page;
    }
}