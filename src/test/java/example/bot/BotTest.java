package example.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BotTest {

    @Mock
    private Bot bot;

    private BotLogic botLogic;
    private User user;

    @BeforeEach
    void setUp() {
        bot = mock(Bot.class);
        botLogic = new BotLogic(bot);
        user = new User(1L);
    }

    // Тестирование команды /test с неправильными ответами
    @Test
    void givenUserAnswersWrong_whenTestCommand() {
        botLogic.processCommand(user, "/test");
        assertEquals(State.TEST, user.getState());
        verify(bot).sendMessage(eq(user.getChatId()), eq("Вычислите степень: 10^2"));

        botLogic.processCommand(user, "10");
        verify(bot).sendMessage(eq(user.getChatId()), eq("Вы ошиблись, верный ответ: 100"));
        assertEquals(1, user.getWrongAnswerQuestions().size());
        verify(bot).sendMessage(eq(user.getChatId()), eq("Вычислите степень: 10^2"));
        verify(bot).sendMessage(eq(user.getChatId()), eq("Сколько будет 2 + 2 * 2"));

        botLogic.processCommand(user, "10");
        verify(bot).sendMessage(eq(user.getChatId()), eq("Вы ошиблись, верный ответ: 6"));
        assertEquals(2, user.getWrongAnswerQuestions().size());
        verify(bot).sendMessage(eq(user.getChatId()), eq("Сколько будет 2 + 2 * 2"));
        assertEquals(State.INIT, user.getState());
        verify(bot).sendMessage(eq(user.getChatId()), eq("Тест завершен"));
    }

    // // Тестирование команды /notify для проверки отправки уведомлений с задержкой
    @Test
    void whenNotifyCommand_thenSendAtTime() throws InterruptedException {
        botLogic.processCommand(user, "/notify");
        assertEquals(State.SET_NOTIFY_TEXT, user.getState());
        verify(bot).sendMessage(eq(user.getChatId()), eq("Введите текст напоминания"));

        botLogic.processCommand(user, "Текст");
        assertEquals(State.SET_NOTIFY_DELAY, user.getState());
        verify(bot).sendMessage(eq(user.getChatId()), eq("Через сколько секунд напомнить?"));

        botLogic.processCommand(user, "abc");
        assertEquals(State.SET_NOTIFY_DELAY, user.getState());
        verify(bot).sendMessage(eq(user.getChatId()), eq("Пожалуйста, введите целое число"));

        botLogic.processCommand(user, "1");
        assertEquals(State.INIT, user.getState());
        verify(bot).sendMessage(eq(user.getChatId()), eq("Напоминание установлено"));

        // Wait for a short time to allow the notification to be sent
        verify(bot, timeout(2000)).sendMessage(eq(user.getChatId()), contains("Текст"));
    }

    // Тестирование команды /repeat когда у пользователя нет неправильных ответов
    @Test
    void givenUserWithoutWrongAnswers_whenRepeat_thenSendNoQuestionsToRepeat() {
        botLogic.processCommand(user, "/repeat");

        verify(bot).sendMessage(eq(user.getChatId()), eq("Нет вопросов для повторения"));
    }
}
