package com.nimbleways.springboilerplate.common.infra.mappers;

import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class AbstractDefaultEnumMapperUnitTests {

    @Test
    void cannot_parse_non_string_objects() {
        TestEnumMapper testEnumMapper = new TestEnumMapper();
        boolean canParse = testEnumMapper.canParse(43);
        assertFalse(canParse);
    }

    @Test
    void can_parse_string_with_valid_value() {
        TestEnumMapper testEnumMapper = new TestEnumMapper();
        boolean canParse = testEnumMapper.canParse("VALID_VALUE");
        assertTrue(canParse);
    }

    @Test
    void can_parse_string_with_invalid_value() {
        TestEnumMapper testEnumMapper = new TestEnumMapper();
        boolean canParse = testEnumMapper.canParse("INVALID_VALUE");
        assertFalse(canParse);
    }

    private enum TestEnum {
        VALID_VALUE;
    }

    private static class TestEnumMapper extends AbstractDefaultEnumMapper<TestEnum> {
        protected TestEnumMapper() {
            super(TestEnum.class);
        }
    }
}
