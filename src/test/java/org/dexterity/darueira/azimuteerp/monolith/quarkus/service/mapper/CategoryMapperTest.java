package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CategoryAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CategoryTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class CategoryMapperTest {

    @Inject
    CategoryMapper categoryMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategorySample1();
        var actual = categoryMapper.toEntity(categoryMapper.toDto(expected));
        assertCategoryAllPropertiesEquals(expected, actual);
    }
}
