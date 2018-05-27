package com.example.partnermap.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerRepository repository;

    /**
     * Получение информации об абоненте.
     * @return Информация об абоненте.
     * @throws CustomerNotFound 404 Абонент не найден.
     */
    @GetMapping("{id}")
    public ResponseEntity<Customer> getCustomer(Authentication auth) throws CustomerNotFound {
        Long customerId = Long.valueOf(auth.getName());
        Optional<Customer> customer = repository.findById(customerId);
        return customer.map(ResponseEntity::ok)
                .orElseThrow(() -> new CustomerNotFound("No customer with id: " + customerId));
    }

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @ControllerAdvice
    public static class PartnerMappingExceptionHandler {
        @ExceptionHandler(CustomerNotFound.class)
        public ResponseEntity<String> handleException(CustomerNotFound ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
