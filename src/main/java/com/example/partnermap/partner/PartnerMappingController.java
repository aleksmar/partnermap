package com.example.partnermap.partner;

import com.example.partnermap.customer.Customer;
import com.example.partnermap.customer.CustomerNotFound;
import com.example.partnermap.customer.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/customers/{customerId}/mappings")
public class PartnerMappingController {
    private final CustomerRepository customerRepository;
    private final PartnerMappingRepository partnerMappingRepository;

    public PartnerMappingController(CustomerRepository customerRepository, PartnerMappingRepository partnerMappingRepository) {
        this.customerRepository = customerRepository;
        this.partnerMappingRepository = partnerMappingRepository;
    }

    /**
     * Добавление новой привязки к сервису.
     * @param mapping Информация о новой привязки к сервису.
     * @return Добавленная привязка к сервису.
     * @throws CustomerNotFound 404 Абонент не найден.
     */
    @PostMapping
    public ResponseEntity<PartnerMapping> addMapping (@RequestBody PartnerMapping mapping, Authentication auth)
            throws CustomerNotFound {
        Long customerId = Long.valueOf(auth.getName());
        if (!customerRepository.existsById(customerId))
            throw new CustomerNotFound("No customer with id: " + customerId);

        mapping.setCustomerId(customerId);
        PartnerMapping save = partnerMappingRepository.save(mapping);

        return ResponseEntity.ok(save);
    }

    /**
     * Получение информации о привязках .
     * @return Информация о всех привязках абонента.
     * @throws CustomerNotFound 404 Абонент не найден.
     */
    @GetMapping
    public ResponseEntity<List<PartnerMapping>> getMappings(Authentication auth) throws CustomerNotFound {
        Long customerId = Long.valueOf(auth.getName());
        if (!customerRepository.existsById(customerId))
            throw new CustomerNotFound("No customer with id: " + customerId);

        List<PartnerMapping> mappings = partnerMappingRepository.findAllByCustomerId(customerId);
        return ResponseEntity.ok(mappings);
    }

    /**
     * Получение привязки по id.
     * @param mappingId Идентификатор привязки.
     * @return Информация о привязки к сервису.
     * @throws CustomerNotFound 404 Абонент не найден.
     * @throws PartnerMappingNotFound 404 Привязка у абонента не найдена.
     */
    @GetMapping("{mappingId:[0-9]+}")
    public ResponseEntity<PartnerMapping> getMappingById(@PathVariable("mappingId") Long mappingId, Authentication auth)
            throws CustomerNotFound, PartnerMappingNotFound {

        Long customerId = Long.valueOf(auth.getName());
        if (!customerRepository.existsById(customerId))
            throw new CustomerNotFound("No customer with id: " + customerId);

        PartnerMapping mapping = partnerMappingRepository.findByIdAndCustomerId(mappingId, customerId)
                .orElseThrow(() -> new PartnerMappingNotFound("No mapping with id: " + mappingId + " for customer with id: " + customerId));
        return ResponseEntity.ok(mapping);
    }


    /**
     * Обновление существующей привязки.
     * @param mappingId Идентификатор сохранения приявзки.
     * @param mapping Информация о существующей привязке.
     * @return Обновленная привязка.
     * @throws CustomerNotFound 404 Абонент не найден.
     */
    @PutMapping("{mappingId:[0-9]+}")
    public ResponseEntity<PartnerMapping> updateMapping(@PathVariable("mappingId") Long mappingId, @RequestBody PartnerMapping mapping, Authentication auth)
            throws CustomerNotFound {

        Long customerId = Long.valueOf(auth.getName());
        if (!customerRepository.existsById(customerId))
            throw new CustomerNotFound("No customer with id: " + customerId);

        mapping.setCustomerId(customerId);
        mapping.setId(mappingId);
        PartnerMapping updated = partnerMappingRepository.save(mapping);

        return ResponseEntity.ok(updated);
    }

    /**
     * Удаление привяки абонента к сервису.
     * @param mappingId Идентификатор приязки.
     * @return Сообщение об удалении информации о привязке к сервису.
     * @throws CustomerNotFound 404 Абонент не найден.
     * @throws PartnerMappingNotFound Привязка у абонента не найдена.
     */
    @DeleteMapping("{mappingId:[0-9]+}")
    public ResponseEntity<String> deleteMapping(@PathVariable("mappingId") Long mappingId, Authentication auth) throws CustomerNotFound, PartnerMappingNotFound {
        Long customerId = Long.valueOf(auth.getName());
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFound("No customer with id: " + customerId));

        PartnerMapping mapping = customer.getMappings().stream().filter(m -> Objects.equals(mappingId, m.getId())).findFirst().orElseThrow(() -> new PartnerMappingNotFound("No mapping with id: " + mappingId + " for customer with id: " + customerId));
        partnerMappingRepository.delete(mapping);
        return ResponseEntity.ok("Successfully deleted.");
    }

    @ControllerAdvice
    public static class PartnerMappingExceptionHandler {
        @ExceptionHandler(PartnerMappingNotFound.class)
        public ResponseEntity<String> handleException(PartnerMappingNotFound ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
