package com.example.atipieraproject.client;

import com.example.atipieraproject.error.APIRateExceededException;
import com.example.atipieraproject.error.GithubUserNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class GithubClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new GithubUserNotFoundException();
        }
        if (response.status() == 403) {
            return new APIRateExceededException();
        }
        return new Exception(response.toString());
    }
}