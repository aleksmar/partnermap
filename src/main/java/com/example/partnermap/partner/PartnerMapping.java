package com.example.partnermap.partner;

import com.example.partnermap.customer.Customer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Привязка сервиса к абоненту.
 */
@Entity
public class PartnerMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    /**
     * Идентификатор сервиса-партнера.
     */
    private String partnerId;
    /**
     * Идентификатор аккаунта абонента в партнерском сервисе.
     */
    @Column(nullable = false)
    private String accountId;
    /**
     * ФИО в партнерском сервисе.
     */
    private String name;
    /**
     * Аватар в партнерском сервисе.
     */
    private byte[] avatarPic;
    /**
     * Абонент.
     */
    @Column(name = "customer_id")
    private Long customerId;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getAvatarPic() {
        return avatarPic;
    }

    public void setAvatarPic(byte[] avatarPic) {
        this.avatarPic = avatarPic;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
