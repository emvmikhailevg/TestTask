package example.container;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {

    private Container container;

    @Before
    public void setUp() {
        container = new Container();
    }

    /**
     * Тест наличия элемента
     */
    @Test
    public void containsMethodTest() {
        Item item = new Item(9L);
        assertFalse(container.contains(item));
        container.add(item);
        assertTrue(container.contains(item));
    }

    /**
     * Тест измерения размера контйнера
     */
    @Test
    public void getSizeMethodTest() {
        Item item = new Item(9L);
        assertEquals(0, container.size());
        container.add(item);
        assertEquals(1, container.size());
    }

    /**
     * Тест добавления вещи в контейнер
     */
    @Test
    public void addMethodTest() {
        assertEquals(0, container.size());
        Item item = new Item(9L);
        container.add(item);
        assertEquals(1, container.size());
        assertEquals(item, container.get(0));
    }

    /**
     * Тест удаления из контейнера
     */
    @Test
    public void removeMethodTest() {
        Item item = new Item(9L);
        container.add(item);
        container.remove(item);
        assertEquals(0, container.size());
        assertFalse(container.contains(item));
    }

    /**
     * Тест получения элемента из контейнера, если он пуст
     */
    @Test
    public void getMethodTest() {
        Item item = new Item(9L);
        container.add(item);
        assertEquals(item, container.get(0));
    }
}
