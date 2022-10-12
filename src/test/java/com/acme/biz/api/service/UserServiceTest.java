package com.acme.biz.api.service;

import com.acme.biz.api.ApiResponse;
import com.acme.biz.api.entity.User;
import com.acme.biz.api.feign.interceptor.ApiCompatibleInterceptor;
import com.acme.biz.api.http.converter.ApiResponseHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author qi.li
 * @email liq@hzgjgc.com
 * @since 2022/10/12
 */
@SpringBootTest(classes = {UserServiceTest.TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableFeignClients
public class UserServiceTest {
    @Autowired
    private UserApi userApi;

    @Autowired
    private UserPastApi userPastApi;

    @Test
    public void should_use_current_client_api() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        Boolean result = userApi.register(user);

        assertTrue(result);
    }

    @Test
    public void should_use_past_client_api() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        ApiResponse<Boolean> response = userPastApi.register(user);

        assertTrue(response.getBody());
    }

    @FeignClient(name = "user-service", url = "http://localhost:8080")
    interface UserApi {
        @PostMapping(value = "/api/user/register/v3")
        Boolean register(@RequestBody User user);

    }

    @FeignClient(name = "user-service-past", url = "http://localhost:8080")
    interface UserPastApi {
        @PostMapping(value = "/api/user/register/v2")
        ApiResponse<Boolean> register(@RequestBody User user);
    }

    @EnableAutoConfiguration
    @SpringBootConfiguration
    static class TestConfig {

        @Bean
        public ApiCompatibleInterceptor apiCompatibleInterceptor() {
            ApiCompatibleInterceptor apiCompatibleInterceptor = new ApiCompatibleInterceptor();
            apiCompatibleInterceptor.addApiCompat(new ApiCompatibleInterceptor.ApiCompat("user-service", "/api/user/register",
                    "v3", List.of("v2")));
            return apiCompatibleInterceptor;
        }

        @Bean
        public HttpMessageConverter<?> apiResponseHttpMessageConverter(ObjectMapper objectMapper) {
            ApiResponseHttpMessageConverter apiResponseHttpMessageConverter = new ApiResponseHttpMessageConverter(objectMapper);
            return apiResponseHttpMessageConverter;
        }
    }
}
