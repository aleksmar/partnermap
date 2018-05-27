package com.example.partnermap.customer;

import com.example.partnermap.partner.PartnerMapping;

import javax.persistence.*;
import java.util.List;

/**
 * Абонент.
 */
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    /**
     * ФИО.
     */
    private String name;
    /**
     * Баланс.
     */
    private long balance;
    /**
     * Статус активности.
     */
    private boolean active;
    /**
     * Логин.
     */
    private String login;
    /**
     * Пароль.
     */
    private String password;
    /**
     * Привязки абонента к партнерским сервисам.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<PartnerMapping> mappings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public List<PartnerMapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<PartnerMapping> mappings) {
        this.mappings = mappings;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
