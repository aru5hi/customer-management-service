package com.example.restApi.Service;

import com.example.restApi.DTO.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExternalRestApiTemplateService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient webClient;

    public UserDto getUSerASObject(Long userId){
        String url = "https://jsonplaceholder.typicode.com/users/{id}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<Void> request = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, request, UserDto.class, userId).getBody();
    }

    public UserDto getUsers(Long userId) {
        String url = "https://jsonplaceholder.typicode.com/users/{id}";
        return webClient.get()
                .uri(url,userId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError ,
                        response -> Mono.error(new RuntimeException("client error" + response))
                )
                .bodyToMono(UserDto.class)
                .block();
    }
}
