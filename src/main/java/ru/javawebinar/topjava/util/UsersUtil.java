package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Roman on 15.10.2018.
 */
public class UsersUtil {
    public static final List<User> USERS = Arrays.asList(
            new User("Tony", "tonytadzh@gmail.com", "1qaz2wsx", Role.ROLE_ADMIN, Role.ROLE_USER),
            new User("Smoke", "smokimo@rambler.com", "biga", Role.ROLE_USER),
            new User("Chacha", "chacha@ya.ru", "rabotnichek", Role.ROLE_USER),
            new User("Duplet", "dupa@mail.ru", "dupadu", Role.ROLE_ADMIN)
    );

    public static List<User> getSorted(Collection<User> users) {
        return users.stream()
                .sorted(Comparator.comparing(User::getName)
                .thenComparing(User::getId))
                .collect(Collectors.toList());
    }
}