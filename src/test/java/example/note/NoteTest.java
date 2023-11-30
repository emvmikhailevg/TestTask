package example.note;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {

    private NoteLogic noteLogic;

    @Before
    public void setUp() {
        noteLogic = new NoteLogic();
    }

    @Test
    @DisplayName("Команда удаления совершает удаление заметки")
    public void deleteCommandTest() {
        noteLogic.handleMessage("/add Task");
        noteLogic.handleMessage("/add Another task");
        assertEquals("Note deleted!", noteLogic.handleMessage("/del 1"));
        assertEquals("Your notes:\n1. Another task", noteLogic.handleMessage("/notes"));
    }

    @Test
    @DisplayName("Команда добавления создает заметку")
    public void addCommandTest() {
        String response = noteLogic.handleMessage("/add Task");
        assertEquals("Note added!", response);
        assertEquals("Your notes:\n1. Task", noteLogic.handleMessage("/notes"));
    }

    @Test
    @DisplayName("Команда редактирования совершает изменение заметки")
    public void editCommandTest() {
        noteLogic.handleMessage("/add Task");
        assertEquals("Note edited!", noteLogic.handleMessage("/edit 1 Another Task"));
        assertEquals("Your notes:\n1. Another Task", noteLogic.handleMessage("/notes"));
    }
}
