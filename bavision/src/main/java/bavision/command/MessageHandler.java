package bavision.command;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler<E> {
    E call(Message message);

//    E call(String message);
}
