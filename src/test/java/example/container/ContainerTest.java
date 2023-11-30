package example.container;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {

    private Container container;

    @Before
    public void setUp() {
        container = new Container();
    }

    @Test
    @DisplayName("Тест наличия элемента")
    public void containsMethodTest() {
        Item item = new Item(9L);
        assertFalse(container.contains(item));
        container.add(item);
        assertTrue(container.contains(item));
    }

    @Test
    @DisplayName("Тест измерения размера контйнера")
    public void getSizeMethodTest() {
        Item item = new Item(9L);
        assertEquals(0, container.size());
        container.add(item);
        assertEquals(1, container.size());
    }

    @Test
    @DisplayName("Тест добавления вещи в контейнер")
    public void addMethodTest() {
        assertEquals(0, container.size());
        Item item = new Item(9L);
        container.add(item);
        assertEquals(1, container.size());
        assertEquals(item, container.get(0));
    }

    @Test
    @DisplayName("Тест удаления из контейнера")
    public void removeMethodTest() {
        Item item = new Item(9L);
        container.add(item);
        container.remove(item);
        assertEquals(0, container.size());
        assertFalse(container.contains(item));
    }

    @Test
    @DisplayName("Тест получения элемента из контейнера, если он пуст")
    public void getMethodTest() {
        Item item = new Item(9L);
        container.add(item);
        assertEquals(item, container.get(0));
    }
}
