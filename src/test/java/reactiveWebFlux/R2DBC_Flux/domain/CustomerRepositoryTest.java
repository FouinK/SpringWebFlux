package reactiveWebFlux.R2DBC_Flux.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactiveWebFlux.R2DBC_Flux.DBinit;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@DataR2dbcTest
@Import(DBinit.class)
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void 한건찾기_테스트() {
        Mono<Customer> findCustomer = customerRepository.findById(1L);
        StepVerifier
                .create(customerRepository.findById(1L))
                .expectNextMatches((c) -> {
                    return c.getFirstName().equals("Jack");
                })
                .expectComplete()
                .verify();
    }
}