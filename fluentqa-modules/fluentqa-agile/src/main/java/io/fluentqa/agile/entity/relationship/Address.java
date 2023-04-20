package io.fluentqa.agile.entity.relationship;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {

    private String street;
    private String city;
    private String postalCode;


}