package reactiveWebFlux.R2DBC_Flux.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactiveWebFlux.R2DBC_Flux.domain.Customer;
import reactiveWebFlux.R2DBC_Flux.domain.CustomerRepository;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

//@WebFluxTest

/**
 * 통합테스트
 */
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {
/*
    @Autowired
    CustomerRepository customerRepository;*/
    @Autowired
    private WebTestClient webTestClient;        //비동기로 http 요청

    @Test
    public void 한건찾기_테스트() {

    }
}