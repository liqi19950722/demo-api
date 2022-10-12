package com.acme.biz.api.http.converter;

import com.acme.biz.api.ApiRequest;
import com.acme.biz.api.ApiResponse;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author qi.li
 * @email liq@hzgjgc.com
 * @since 2022/10/12
 */
public class ApiResponseHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    private final ObjectMapper objectMapper;

    public ApiResponseHttpMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        setSupportedMediaTypes(List.of(
                MediaType.APPLICATION_JSON,
                MediaType.valueOf("application/*+json")));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ApiRequest.class);
    }

    @Override
    public boolean canRead(Type type, @Nullable Class<?> contextClass, @Nullable MediaType mediaType) {
        if (type instanceof ParameterizedType parameterizedType) {
            return parameterizedType.getRawType().equals(ApiResponse.class);
        }
        return false;
    }

    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        ApiRequest apiRequest = (ApiRequest) o;
        Object body = apiRequest.getBody();
        OutputStream outputStream = StreamUtils.nonClosing(outputMessage.getBody());
        JsonEncoding encoding = JsonEncoding.UTF8;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, encoding);
        objectMapper.writer().writeValue(generator, body);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        JavaType javaType = this.objectMapper.constructType(GenericTypeResolver.resolveType(clazz, (Class<?>) null));

        InputStream inputStream = StreamUtils.nonClosing(inputMessage.getBody());
        return objectMapper.readValue(inputStream, javaType);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream inputStream = StreamUtils.nonClosing(inputMessage.getBody());
        Object value = objectMapper.readValue(inputStream, Object.class);

        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(1);
        response.setMessage("SUCCESS");
        response.setBody(value);
        return response;
    }
}
