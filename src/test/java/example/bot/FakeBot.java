package example.bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Фэйковая реализация бота для тестов
 */
public class FakeBot implements Bot {

    /**
     * Сообщения, которые отправлены ботом
     */
    private final List<String> messages = new ArrayList<>();

    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(message);
    }

    /**
     * Возвращает сообщения бота
     *
     * @return сообщения бота
     */
    public List<String> getMessages() {
        return messages;
    }
}
