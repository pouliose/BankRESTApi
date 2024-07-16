package com.company.fintech.domain.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @NotNull(message = "Source account id is mandatory")
    private String sourceAccountId;

    @NotNull(message = "Target account id is mandatory")
    private String targetAccountId;

    @NotNull(message = "Balance is mandatory")
    private BigDecimal amount;

    @NotNull(message = "Currency is mandatory")
    private String currency;

    private LocalDateTime executedAt;

    @JsonIgnore
    @ManyToMany(mappedBy = "transactions")
    private Set<Account> accounts = new HashSet<>();
}
