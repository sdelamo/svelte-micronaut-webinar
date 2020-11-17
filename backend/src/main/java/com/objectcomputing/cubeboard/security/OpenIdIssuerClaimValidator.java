package com.objectcomputing.cubeboard.security;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.config.SecurityConfigurationProperties;
import io.micronaut.security.oauth2.configuration.OpenIdClientConfiguration;
import io.micronaut.security.token.jwt.generator.claims.JwtClaims;
import io.micronaut.security.token.jwt.validator.JwtClaimsValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Requires(property = SecurityConfigurationProperties.PREFIX + ".authentication", value = "idtoken")
@Requires(property = JwtClaimsValidator.PREFIX + ".openid-issuer", notEquals = StringUtils.FALSE)
@Singleton
public class OpenIdIssuerClaimValidator implements JwtClaimsValidator {

    private final List<String> openIdClientIssuers;

    /**
     *
     * @param openIdClientConfigurations OpenId client configurations
     */
    public OpenIdIssuerClaimValidator(Collection<OpenIdClientConfiguration> openIdClientConfigurations) {
        this.openIdClientIssuers = openIdClientIssuers(openIdClientConfigurations);
    }

    /**
     *
     * @param openIdClientConfigurations OpenId client configurations
     * @return a list of issuers urls
     */
    @NonNull
    protected List<String> openIdClientIssuers(@NonNull Collection<OpenIdClientConfiguration> openIdClientConfigurations) {
        return openIdClientConfigurations.stream()
                .filter(openIdClientConfiguration -> openIdClientConfiguration.getIssuer().isPresent())
                .map(openIdClientConfiguration -> openIdClientConfiguration.getIssuer().get().toString())
                .collect(Collectors.toList());
    }

    @Override
    public boolean validate(JwtClaims claims) {
        return validate(claims, null);
    }

    @Override
    public boolean validate(@NotNull JwtClaims claims, @Nullable HttpRequest<?> request) {
        if (claims.contains(JwtClaims.ISSUER)) {
            Object obj = claims.get(JwtClaims.ISSUER);
            if (obj == null) {
                return false;
            }
            String issuer = obj.toString();
            return openIdClientIssuers
                    .stream()
                    .anyMatch(iss -> iss.equalsIgnoreCase(issuer));
        }
        return false;
    }
}
