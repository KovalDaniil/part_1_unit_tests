package praktikum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngredientTest {

    @ParameterizedTest(name = "Ingredient({0}, \"{1}\", {2}): getType() == {0}")
    @DisplayName("getType(): ингредиент хранит переданный тип")
    @CsvSource({
            "SAUCE,   'hot sauce',    100.0",
            "SAUCE,   'sour cream',   200.0",
            "FILLING, cutlet,         150.0",
            "FILLING, dinosaur,       250.0"
    })
    void getType_returnsTypeFromConstructor(IngredientType type, String name, float price) {
        Ingredient ingredient = new Ingredient(type, name, price);

        assertEquals(type, ingredient.getType());
    }

    @ParameterizedTest(name = "Ingredient(\"{0}\"): getName() == \"{0}\"")
    @DisplayName("getName(): ингредиент хранит переданное название")
    @ValueSource(strings = {"cutlet", "sausage", "chili sauce"})
    void getName_returnsNameFromConstructor(String expectedName) {
        Ingredient ingredient = new Ingredient(IngredientType.FILLING, expectedName, 100f);

        assertEquals(expectedName, ingredient.getName());
    }

    @ParameterizedTest(name = "Ingredient(price={0}): getPrice() == {0}")
    @DisplayName("getPrice(): ингредиент хранит переданную цену")
    @CsvSource({
            "100.0",
            "200.0",
            "300.0"
    })
    void getPrice_returnsPriceFromConstructor(float expectedPrice) {
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "test sauce", expectedPrice);

        assertEquals(expectedPrice, ingredient.getPrice(), 0.0001f);
    }

    @ParameterizedTest(name = "Все значения enum корректны: {0}")
    @DisplayName("Каждое значение IngredientType возвращается геттером без изменений")
    @EnumSource(IngredientType.class)
    void constructor_acceptsEveryIngredientType(IngredientType type) {
        Ingredient ingredient = new Ingredient(type, "universal ingredient", 42f);

        assertEquals(type, ingredient.getType());
    }
}
