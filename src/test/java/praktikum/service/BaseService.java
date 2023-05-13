package praktikum.service;

import com.github.javafaker.Faker;

import java.util.concurrent.ThreadLocalRandom;

public class BaseService {
    static Faker faker = new Faker();
    ThreadLocalRandom random = ThreadLocalRandom.current();
}
