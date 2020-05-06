package com.frank.api.gateway.auth;

import com.frank.api.gateway.auth.constant.ApiGatewayAuthConfigConstant;
import com.frank.api.gateway.auth.constant.ApiGatewayAuthErrorCode;
import com.frank.api.gateway.auth.exception.ApiGatewayException;
import com.frank.api.gateway.auth.pojo.AppInfoWithToken;
import com.frank.api.gateway.auth.repository.AppInfoRepository;
import com.frank.api.gateway.util.AccessTokenGenerator;
import lombok.NonNull;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AccessTokenService {

    private ReactiveRedisTemplate reactiveRedisTemplate;

    private AppInfoRepository appInfoRepository;

    public AccessTokenService(ReactiveRedisTemplate reactiveRedisTemplate, AppInfoRepository appInfoRepository) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.appInfoRepository = appInfoRepository;
    }

    public Mono<AppInfoWithToken> getAccessToken(@NonNull final String appId, @NonNull final String secret) {

        return appInfoRepository
            .findById(appId)
            .switchIfEmpty(Mono.error(new ApiGatewayException(ApiGatewayAuthErrorCode.CAN_NOT_FIND_APP_INFO,"无法找到应用信息，请检查应用信息")))
            .filter(appInfo->appInfo.getSecret().equals(secret))
            .switchIfEmpty(Mono.error(new ApiGatewayException(ApiGatewayAuthErrorCode.APP_AUTH_INFO_ERROR,"鉴权应用信息错误，请检查应用信息")))
            .flatMap(appInfo-> {
                final AppInfoWithToken appInfoWithToken = new AppInfoWithToken(AccessTokenGenerator.create(appInfo),appInfo);
                reactiveRedisTemplate.opsForHash()
                    .put(ApiGatewayAuthConfigConstant.ACCESSTOKEN_CACHE_KEY,appInfoWithToken.getAccessToken(),appInfoWithToken.getAppId());
                return Mono.just(appInfoWithToken);
            });
    }
}
