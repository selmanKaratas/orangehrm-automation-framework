package com.selman.automation.models;

import net.datafaker.Faker;

public record Employee(String firstName, String middleName, String lastName) {

    private static final Faker FAKER = new Faker();

    public static Employee random() {
        return new Employee(
                FAKER.name().firstName(),
                FAKER.name().firstName(),
                FAKER.name().lastName()
        );
    }

    public String fullName() {
        return firstName + " " + lastName;
    }
}