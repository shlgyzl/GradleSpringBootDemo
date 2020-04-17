package com.application.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()) {
            case 400:
            case 404:
            case 500:
            case 503:
            case 505:
                return new FeignClientException("feign调用接口错误", response.status());
            default:
                return defaultDecoder.decode(methodKey, response);
        }

    }

}
