package com.example.partnermap;

import com.example.partnermap.customer.Customer;
import com.example.partnermap.customer.CustomerRepository;
import com.example.partnermap.partner.PartnerMapping;
import com.example.partnermap.partner.PartnerMappingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class Configuration {
    @Bean
    CommandLineRunner initializer(CustomerRepository customerRepository, PartnerMappingRepository partnerMappingRepository) {
        return args -> {
            Customer customer = new Customer();
            customer.setActive(true);
            customer.setName("customer1");
            customer.setLogin("login1");
            customer.setPassword("password1");
            customer.setBalance(10000);
            customerRepository.save(customer);

            PartnerMapping pm = new PartnerMapping();
            pm.setCustomerId(1L);
            pm.setAccountId("12312");
            pm.setName("12efwfw");
            pm.setPartnerId("324234");
            partnerMappingRepository.save(pm);

        };
    }
}
