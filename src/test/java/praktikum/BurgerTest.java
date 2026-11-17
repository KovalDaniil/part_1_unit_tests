package praktikum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BurgerTest {
    private static final float PRICE_DELTA = 0.0001f;
    @Mock
    private Bun bunMock;
    @Mock
    private Ingredient ingredientMock;
    private Burger burger;
    @BeforeEach
    void setUp() {
        burger = new Burger();
    }

    @Test
    @DisplayName("setBuns(): сохранённая булка доступна в публичном поле bun")
    void setBuns_storesGivenBun() {
        burger.setBuns(bunMock);
        assertEquals(bunMock, burger.bun);
    }

    @Test
    @DisplayName("addIngredient(): ингредиент добавляется в конец списка ingredients")
    void addIngredient_appendsIngredientToList() {
        burger.addIngredient(ingredientMock);

        List<Ingredient> ingredients = burger.ingredients;
        assertEquals(1, ingredients.size());
        assertEquals(ingredientMock, ingredients.get(0));
    }

    @Test
    @DisplayName("removeIngredient(): удаляет элемент по индексу, остальные сохраняются")
    void removeIngredient_byIndex_keepsOtherIngredients() {
        Ingredient firstIngredient = new Ingredient(IngredientType.SAUCE, "first sauce", 10f);
        Ingredient secondIngredient = new Ingredient(IngredientType.FILLING, "second filling", 20f);
        burger.addIngredient(firstIngredient);
        burger.addIngredient(secondIngredient);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
        assertEquals(secondIngredient, burger.ingredients.get(0));
    }

    @Test
    @DisplayName("moveIngredient(): переносит ингредиент на новый индекс")
    void moveIngredient_movesElementToNewIndex() {
        Ingredient cutlet = new Ingredient(IngredientType.FILLING, "cutlet", 100f);
        Ingredient hotSauce = new Ingredient(IngredientType.SAUCE, "hot sauce", 50f);
        Ingredient sourCream = new Ingredient(IngredientType.SAUCE, "sour cream", 30f);
        burger.addIngredient(cutlet);
        burger.addIngredient(hotSauce);
        burger.addIngredient(sourCream);

        burger.moveIngredient(2, 1);

        assertEquals(List.of(cutlet, sourCream, hotSauce), burger.ingredients);
    }

    @Test
    @DisplayName("getPrice() с МОКОМ булки: цена = 2 * getPrice() булки (проверено verify)")
    void getPrice_withMockedBun_doublesBunPrice() {
        when(bunMock.getPrice()).thenReturn(50f);
        burger.setBuns(bunMock);

        float actualPrice = burger.getPrice();

        assertEquals(100f, actualPrice, PRICE_DELTA);
        verify(bunMock, times(1)).getPrice();
    }

    @ParameterizedTest(name = "Булка {0} руб. x2 + ингредиенты {1} руб. => {2} руб.")
    @DisplayName("getPrice() с РЕАЛЬНЫМИ объектами: сумма булок и всех ингредиентов (параметризация)")
    @CsvSource({
            "100.0, 50.0,  250.0",
            "150.0, 75.5,  375.5",
            "200.0, 0.0,   400.0"
    })
    void getPrice_withRealObjects_sumsBunsAndIngredients(float bunPrice,
                                                         float ingredientPrice,
                                                         float expectedPrice) {
        burger.setBuns(new Bun("test bun", bunPrice));
        if (ingredientPrice > 0f) {
            burger.addIngredient(new Ingredient(IngredientType.FILLING, "test filling", ingredientPrice));
        }

        assertEquals(expectedPrice, burger.getPrice(), PRICE_DELTA);
    }

    @Test
    @DisplayName("getPrice() с МОКОМ ингредиента: цена складывается из стабов булки и ингредиента")
    void getPrice_withMockedIngredient_addsIngredientPrice() {
        when(bunMock.getPrice()).thenReturn(100f);
        when(ingredientMock.getPrice()).thenReturn(30f);
        burger.setBuns(bunMock);
        burger.addIngredient(ingredientMock);

        float actualPrice = burger.getPrice();

        assertEquals(230f, actualPrice, PRICE_DELTA);
        verify(ingredientMock, times(1)).getPrice();
    }

    @Test
    @DisplayName("getReceipt() с МОКАМИ: чек содержит имена булки и ингредиента, тип в нижнем регистре")
    void getReceipt_withMocks_containsNamesAndLowercasedType() {
        when(bunMock.getName()).thenReturn("black bun");
        when(ingredientMock.getName()).thenReturn("cutlet");
        when(ingredientMock.getType()).thenReturn(IngredientType.FILLING);
        when(bunMock.getPrice()).thenReturn(10f);
        when(ingredientMock.getPrice()).thenReturn(5f);
        burger.setBuns(bunMock);
        burger.addIngredient(ingredientMock);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains("(==== black bun ====)"), "Чек должен содержать заголовок с именем булки");
        assertTrue(receipt.contains("= filling cutlet ="), "Чек должен содержать тип (строчными) и имя ингредиента");
        assertTrue(receipt.contains("Price: 25"), "Чек должен содержать итоговую цену");
    }

    @Test
    @DisplayName("getReceipt() пустого бургера: только булки и цена без ингредиентов")
    void getReceipt_withoutIngredients_containsOnlyBunAndPrice() {
        Bun realBun = new Bun("white bun", 200f);
        burger.setBuns(realBun);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains("(==== white bun ====)"));
        assertEquals(2, receipt.split("==== white bun ====").length - 1,
                "Имя булки должно встречаться дважды: сверху и снизу бургера");
        assertTrue(receipt.contains("Price: 400"));
    }
}
