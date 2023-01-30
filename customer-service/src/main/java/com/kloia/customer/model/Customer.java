package com.kloia.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@Table(name = "customer", schema = "public")
@SequenceGenerator(name = "customer_id_seq", schema = "public", sequenceName = "customer_id_seq", allocationSize = 1)
public class Customer {

    @Id
    @GeneratedValue(generator = "customer_id_seq")
    private Integer id;

   // @Column(name = "name", nullable = false)
    private String name;

   // @Column(name = "age", nullable = false)
    private Integer age;

   // @Column(name = "account_id", nullable = false)
    private Long accountId;

}
