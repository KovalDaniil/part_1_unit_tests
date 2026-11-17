package praktikum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngredientTypeTest {

    @Test
    @DisplayName("В enum ровно два значения: SAUCE и FILLING")
    void enum_hasExactlyTwoValues() {
        IngredientType[] values = IngredientType.values();

        assertEquals(2, values.length);
        assertEquals(IngredientType.SAUCE, values[0]);
        assertEquals(IngredientType.FILLING, values[1]);
    }

    @ParameterizedTest(name = "{0}: name() == \"{0}\" и valueOf(\"{0}\") возвращает его же")
    @DisplayName("name()/valueOf(): строковое имя совпадает с константой")
    @CsvSource({
            "SAUCE",
            "FILLING"
    })
    void name_and_valueOf_areConsistent(String expectedName) {
        IngredientType type = IngredientType.valueOf(expectedName);

        assertEquals(expectedName, type.name());
        assertEquals(expectedName, type.toString());
    }
}
