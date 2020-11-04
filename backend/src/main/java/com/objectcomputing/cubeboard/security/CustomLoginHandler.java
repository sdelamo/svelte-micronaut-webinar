package com.objectcomputing.cubeboard.security;

import com.objectcomputing.cubeboard.conf.AppConfiguration;
import io.micronaut.context.annotation.Primary;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.handlers.RedirectingLoginHandler;
import io.micronaut.security.oauth2.endpoint.token.response.OpenIdUserDetailsMapper;

import javax.inject.Singleton;
import java.util.Map;

@Primary
@Singleton
public class CustomLoginHandler implements RedirectingLoginHandler {

    private final AppConfiguration appConfiguration;

    public CustomLoginHandler(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Override
    public MutableHttpResponse<?> loginSuccess(UserDetails userDetails, HttpRequest<?> request) {
        Map<String, Object> attributes = userDetails.getAttributes("roles", "username"); //TODOuse config instead
        if (attributes.containsKey(OpenIdUserDetailsMapper.OPENID_TOKEN_KEY)) {
            return HttpResponse.seeOther(UriBuilder.of(appConfiguration.getLoginRedirect()).queryParam(appConfiguration.getParamToken(), attributes.get(OpenIdUserDetailsMapper.OPENID_TOKEN_KEY)).build());

        }
        //TODO LOG we should never reach this
        return HttpResponse.unauthorized();
    }

    @Override
    public MutableHttpResponse<?> loginRefresh(UserDetails userDetails, String refreshToken, HttpRequest<?> request) {
        return null;
    }

    @Override
    public MutableHttpResponse<?> loginFailed(AuthenticationResponse authenticationResponse, HttpRequest<?> request) {
        return null;
    }
}
