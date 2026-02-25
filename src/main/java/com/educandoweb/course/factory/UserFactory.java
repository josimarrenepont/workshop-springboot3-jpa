package com.educandoweb.course.factory;

import com.educandoweb.course.entities.User;

import java.util.Set;

public class UserFactory {

    public static User createUser() {
        return new User(
                1L,
                "maria",
                "123",
                true,
                true,
                true,
                true,
                "maria@email.com",
                "999999999",
                Set.of()
        );
    }
}
