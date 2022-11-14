package reactiveWebFlux.R2DBC_Flux.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactiveWebFlux.R2DBC_Flux.domain.Customer;
import reactiveWebFlux.R2DBC_Flux.domain.CustomerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final Sinks.Many<Customer> sink;

    // A요청 -> Flux -> Stream
    // B요청 -> Flux -> Stream
    // -> Flux.merge -> sink

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping("/flux")
    public Flux<Integer> flux() {
        return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/fluxStream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxStream() {
        return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1)).log();
    }


    @GetMapping(value = "/customer", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Customer> findAll() {
        return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping("/customer/{id}")
    public Mono<Customer> findById(@PathVariable Long id) {
        return customerRepository.findById(id).log();
    }

    /**
     *  sse프로토콜로 flux(response)를 계속 연결중인 상태
     */
    @GetMapping(value = "/customer/sse")    //생략 가능 produces = MediaType.TEXT_EVENT_STREAM_VALUE
    public Flux<ServerSentEvent<Customer>> findAllSSE() {
        //연결이 끊어지는 순간 blockLast로 알게됨(?) 다시 연결하면 다시 요청 받을 수 았음 (비동기 단일 스레드)
        return sink.asFlux().map(c -> ServerSentEvent.builder(c).build()).doOnCancel(() ->{
            sink.asFlux().blockLast();
        });
    }

    //새로운 데이터가 삽입 되면 sse에게 삽입함 onNext()
    @PostMapping("/customer")
    public Mono<Customer> save() {
        return customerRepository.save(new Customer("길동", "홍")).doOnNext(c ->{
            sink.tryEmitNext(c);
        });
    }
}
