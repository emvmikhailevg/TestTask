package example.note;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {

    private NoteLogic noteLogic;

    @Before
    public void setUp() {
        noteLogic = new NoteLogic();
    }

    /**
     * Команда удаления совершает удаление заметки
     */
    @Test
    public void deleteCommandTest() {
        noteLogic.handleMessage("/add Task");
        noteLogic.handleMessage("/add One More Task");
        assertEquals("Note deleted!", noteLogic.handleMessage("/del 1"));
        assertEquals("Your notes:\n"
                + "1. One More Task", noteLogic.handleMessage("/notes"));
    }

    /**
     * Команда добавления создает заметку
     */
    @Test
    public void addCommandTest() {
        String response = noteLogic.handleMessage("/add Task");
        assertEquals("Note added!", response);
        assertEquals("Your notes:\n"
                + "1. Task", noteLogic.handleMessage("/notes"));
    }

    /**
     * Команда редактирования совершает изменение заметки
     */
    @Test
    public void editCommandTest() {
        noteLogic.handleMessage("/add Task");
        assertEquals("Note edited!", noteLogic.handleMessage("/edit 1 Another Task"));
        assertEquals("Your notes:\n"
                + "1. Another Task", noteLogic.handleMessage("/notes"));
    }
}
