package bavision;

import bavision.command.CommandHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class BaVisionBot extends TelegramLongPollingBot {
    private CommandHandler commandHandler;

    public BaVisionBot() {
        commandHandler = new CommandHandler();
    }

    @Override
    public void onUpdateReceived(Update update) {
        PartialBotApiMethod handle;
        Message message;
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            message = callbackQuery.getMessage();
            handle = commandHandler.handle(callbackQuery.getData());
        } else {
            handle = commandHandler.handle(update.getMessage().getText());
            message = update.getMessage();
        }

        if (handle == null || message == null) {
            return;
        }

        try {
            handleMessage(handle, message);
        } catch (TelegramApiException e) {

        }
    }

    public void handleMessage(PartialBotApiMethod handle, Message message) throws TelegramApiException {
        int messageId = message.getMessageId();
        if (handle instanceof SendMessage) {
            handleSendMessage(handle, message);
        }
        if (handle instanceof SendPhoto) {
            handleSendPhoto(handle, message);
        }
        execute(new DeleteMessage(message.getChatId(), messageId));
    }

    public void handleSendMessage(PartialBotApiMethod handle, Message message) throws TelegramApiException {
        ((SendMessage)handle).setChatId(message.getChatId());
        execute((SendMessage)handle);
    }

    public void handleSendPhoto(PartialBotApiMethod handle, Message message) throws TelegramApiException {
        ((SendPhoto)handle).setChatId(message.getChatId());
        execute((SendPhoto)handle);
    }

    @Override
    public String getBotUsername() {
        return "Service project bot";
    }

    @Override
    public String getBotToken() {
        return "";
    }
}
