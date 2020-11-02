package com.objectcomputing.cubeboard;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

@Controller("/api")
public class ValuesController {

    private final ValuesFetcher valuesFetcher;

    public ValuesController(ValuesFetcher valuesFetcher) {
        this.valuesFetcher = valuesFetcher;
    }

    @Get("/values/{label}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<?> index(@PathVariable String label) {
        try {
            List<Value> values = valuesFetcher.findByLabel(label);
            ValuePage page = new ValuePage();
            page.setCount(values.size());
            page.setData(values);
            return HttpResponse.ok(page);
        } catch (NoSuchLabelException e) {
            e.printStackTrace();
            return HttpResponse.notFound();
        }
    }
}
