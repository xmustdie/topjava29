package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static ru.javawebinar.topjava.Profiles.JPA;

/**
 * @author Andrei Durkin <a.durkin@goodt.me> at 30.06.2023
 */

@ActiveProfiles(JPA)
public class JpaMealServiceTest extends AbstractMealServiceTest {
}
