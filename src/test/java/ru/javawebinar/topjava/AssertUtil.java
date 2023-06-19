package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.AbstractBaseEntity;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andrei Durkin <a.durkin@goodt.me> at 20.06.2023
 */

public class AssertUtil {

    private final String[] ignoredFields;

    public AssertUtil(String... ignoredFields) {
        this.ignoredFields = ignoredFields;
    }

    public <T extends AbstractBaseEntity> void assertMatch(T actual, T expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(ignoredFields).isEqualTo(expected);
    }

    @SafeVarargs
    public final <T extends AbstractBaseEntity> void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public <T extends AbstractBaseEntity> void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields(ignoredFields).isEqualTo(expected);
    }
}
