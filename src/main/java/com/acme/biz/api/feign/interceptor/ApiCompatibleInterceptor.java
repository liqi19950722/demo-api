package com.acme.biz.api.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author qi.li
 * @email liq@hzgjgc.com
 * @since 2022/10/12
 */
public class ApiCompatibleInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // 获取服务信息 服务名 url 版本
        String serviceName = template.feignTarget().name();
        String url = template.request().url();
        int versionIndex = url.lastIndexOf("/");
        String serviceUrl = url.substring(0, versionIndex);
        String version = url.substring(versionIndex + 1);

        // 找最新版本
        String actualVersion = compatibleTable.stream().filter(it -> {
            if (serviceName.contains(it.serviceName()) && Objects.equals(it.location(), serviceUrl)) {
                if (Objects.equals(it.currentApiVersion(), version)) {
                    return true;
                }
                return it.compatibleApiVersion().contains(version);
            }
            return false;
        }).findAny().map(ApiCompat::currentApiVersion).orElseThrow(() -> new RuntimeException("不兼容"));



        //重新设置url
        template.uri(serviceUrl + "/" + actualVersion,false);

    }

    public void addApiCompat(ApiCompat apiCompat) {
        compatibleTable.add(apiCompat);
    }

    static List<ApiCompat> compatibleTable = new ArrayList<>();


    public record ApiCompat(String serviceName, String location,
                     String currentApiVersion, List<String> compatibleApiVersion) {

    }

}
