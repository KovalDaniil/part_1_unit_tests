package praktikum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BunTest {
    @ParameterizedTest(name = "Bun(\"{0}\", {1}): getName() == \"{0}\"")
    @DisplayName("Конструктор и getName(): булка хранит переданное название")
    @CsvSource({
            "'black bun', 100.0",
            "'white bun', 200.0",
            "'red bun',   300.0"
    })
    void getName_returnsNameFromConstructor(String expectedName, float expectedPrice) {
        Bun bun = new Bun(expectedName, expectedPrice);
        assertEquals(expectedName, bun.getName());
    }

    @ParameterizedTest(name = "Bun(\"{0}\", {1}): getPrice() == {1}")
    @DisplayName("Конструктор и getPrice(): булка хранит переданную цену")
    @CsvSource({
            "'black bun', 100.0",
            "'white bun', 200.0",
            "'red bun',   300.0"
    })
    void getPrice_returnsPriceFromConstructor(String expectedName, float expectedPrice) {
        Bun bun = new Bun(expectedName, expectedPrice);
        assertEquals(expectedPrice, bun.getPrice(), 0.0001f);
    }
}
