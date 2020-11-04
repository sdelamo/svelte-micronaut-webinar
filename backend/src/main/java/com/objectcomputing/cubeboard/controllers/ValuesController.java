package com.objectcomputing.cubeboard.controllers;

import com.objectcomputing.cubeboard.NoSuchLabelException;
import com.objectcomputing.cubeboard.Value;
import com.objectcomputing.cubeboard.ValuePage;
import com.objectcomputing.cubeboard.ValuesFetcher;
import com.objectcomputing.cubeboard.versioning.ApiVersion;
import io.micronaut.core.version.annotation.Version;
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

    @Version(ApiVersion.V1)
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
