package bavision.builder;

import bavision.command.commands.CommandType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyBoardBuilder {

    public ReplyKeyboard buildNavigationButtons(int currentSpecialistId, int min, int max, String next, String previous) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton previousSpecialist = new InlineKeyboardButton();
        InlineKeyboardButton nextSpecialist = new InlineKeyboardButton();
        previousSpecialist.setText("Дальше");
        previousSpecialist.setCallbackData(next + " " + currentSpecialistId);
        nextSpecialist.setText("Назад");
        nextSpecialist.setCallbackData(previous + " " + currentSpecialistId);
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        if (currentSpecialistId > min) {
            firstRow.add(nextSpecialist);
        }
        if (currentSpecialistId < max) {
            firstRow.add(previousSpecialist);
        }
        List<InlineKeyboardButton> rowWithBackToMainMenu = generateRowWithBackToMainMenu();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(firstRow);
        rowList.add(rowWithBackToMainMenu);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public List<InlineKeyboardButton> generateRowWithBackToMainMenu() {
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton backToMenu = new InlineKeyboardButton();
        backToMenu.setText("Вернуться в главное меню");
        backToMenu.setCallbackData(CommandType.START.getCommand());
        secondRow.add(backToMenu);
        return secondRow;
    }

    public ReplyKeyboard buildHelloKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton requestSession = new InlineKeyboardButton();
        InlineKeyboardButton specialistInformation = new InlineKeyboardButton();
        InlineKeyboardButton serviceInformation = new InlineKeyboardButton();
        requestSession.setText("Записаться на сегодня");
        requestSession.setCallbackData("/show_free_today");
        specialistInformation.setText("Просмотреть специалистов");
        specialistInformation.setCallbackData(CommandType.SHOW_SPECIALIST.getCommand());
        serviceInformation.setText("Просмотреть процедуры");
        serviceInformation.setCallbackData(CommandType.SHOW_SERVICES.getCommand());
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row1.add(requestSession);
        row2.add(specialistInformation);
        row3.add(serviceInformation);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);
        rowList.add(row3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

}
