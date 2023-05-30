package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address implements Cloneable{

    @Id
    @SequenceGenerator(name = "gen", sequenceName = "seq",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    public Address(String street) {
        this.id = null;
        this.street = street;
    }

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    @Override
    public Address clone() {
        return new Address(
                this.id,
                this.street
        );
    }
    public Long getId() {

        return id;
    }

    public String getStreet() {

        return street;
    }

}
