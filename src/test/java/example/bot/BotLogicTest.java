package example.bot;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class BotLogicTest {

    private FakeBot bot;
    private BotLogic botLogic;
    private User user;

    @Before
    public void setUp() {
        bot = new FakeBot();
        botLogic = new BotLogic(bot);
        user = new User(9L);
    }

    @Test
    @DisplayName("Проверка команды \"/test\", если пользователь отвечает на вопросы неправильно")
    public void givenWrongResultByUser() {
        botLogic.processCommand(user, "/test");
        assertEquals(State.TEST, user.getState());
        assertEquals("Вычислите степень: 10^2", bot.getMessages().get(0));
        botLogic.processCommand(user, "10");
        assertEquals("Вы ошиблись, верный ответ: 100", bot.getMessages().get(1));
        assertEquals(1, user.getWrongAnswerQuestions().size());
        assertEquals("Вычислите степень: 10^2", user.getWrongAnswerQuestions().get(0).getText());
        assertEquals("Сколько будет 2 + 2 * 2", bot.getMessages().get(2));
        botLogic.processCommand(user, "10");
        assertEquals("Вы ошиблись, верный ответ: 6", bot.getMessages().get(3));
        assertEquals(2, user.getWrongAnswerQuestions().size());

        assertEquals("Сколько будет 2 + 2 * 2", user.getWrongAnswerQuestions().get(1).getText());
        assertEquals(State.INIT, user.getState());
        assertEquals("Тест завершен", bot.getMessages().get(4));
    }

    @Test
    @DisplayName("Проверка команды \"/test\", если пользователь отвечает на вопросы правильно")
    public void givenCorrectResultByUser() {
        botLogic.processCommand(user, "/test");
        assertEquals(State.TEST, user.getState());
        assertEquals("Вычислите степень: 10^2", bot.getMessages().get(0));
        botLogic.processCommand(user, "100");
        assertEquals("Правильный ответ!", bot.getMessages().get(1));
        assertEquals("Сколько будет 2 + 2 * 2", bot.getMessages().get(2));
        botLogic.processCommand(user, "6");
        assertEquals("Правильный ответ!", bot.getMessages().get(3));
        assertTrue(user.getWrongAnswerQuestions().isEmpty());
        assertEquals(State.INIT, user.getState());
        assertEquals("Тест завершен", bot.getMessages().get(4));
    }

    @Test
    @DisplayName("Проверка команды \"/notify\", чтоб напоминания приходили вовремя")
    public void commandNotifyForResultAtTime() throws InterruptedException {
        botLogic.processCommand(user, "/notify");
        assertEquals(State.SET_NOTIFY_TEXT, user.getState());
        assertEquals("Введите текст напоминания", bot.getMessages().get(0));
        botLogic.processCommand(user, "Текст");
        assertEquals(State.SET_NOTIFY_DELAY, user.getState());
        assertEquals("Через сколько секунд напомнить?", bot.getMessages().get(1));
        botLogic.processCommand(user, "abc");
        assertEquals(State.SET_NOTIFY_DELAY, user.getState());
        assertEquals("Пожалуйста, введите целое число", bot.getMessages().get(2));
        botLogic.processCommand(user, "1");
        assertEquals(State.INIT, user.getState());
        assertEquals("Напоминание установлено", bot.getMessages().get(3));

        assertEquals(4, bot.getMessages().size());
        Thread.sleep(2000L);
        assertEquals("Сработало напоминание: 'Текст'", bot.getMessages().get(4));
    }

    @Test
    @DisplayName("Если пользователь имеет неправильные ответы на вопросы, то при последующей отправке пользователем " +
            "команды \"/repeat\", бот должен сделать повторение")
    public void givenUserWithWrongAnswers_whenRepeat_thenBeginRepeatSession() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "10");
        botLogic.processCommand(user, "10");

        botLogic.processCommand(user, "/repeat");
        assertEquals(State.REPEAT, user.getState());
        assertEquals("Вычислите степень: 10^2", bot.getMessages().get(5));
        botLogic.processCommand(user, "100");
        assertEquals("Правильный ответ!", bot.getMessages().get(6));
        assertEquals(1, user.getWrongAnswerQuestions().size());
        assertEquals("Сколько будет 2 + 2 * 2", bot.getMessages().get(7));
        botLogic.processCommand(user, "6");
        assertEquals("Правильный ответ!", bot.getMessages().get(8));
        assertTrue(user.getWrongAnswerQuestions().isEmpty());

        assertEquals(State.INIT, user.getState());
        assertEquals("Тест завершен", bot.getMessages().get(9));
    }

    @Test
    @DisplayName("Если пользователь не имеет неправильных ответов на вопросы, то при последующей отправке " +
            "пользователем команды \"/repeat\" бот должен прислать сообщение, что вопросы на повторения отсутствуют")
    public void givenUserWithoutWrongAnswers_whenRepeat_thenSendNoQuestionsToRepeat() {
        botLogic.processCommand(user, "/repeat");
        assertEquals("Нет вопросов для повторения", bot.getMessages().get(0));
    }
}
