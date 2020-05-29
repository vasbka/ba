package bavision.builder;

import bavision.command.commands.CommandType;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class KeyBoardBuilder {
    private static final SimpleDateFormat TIME_PATTERN = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");

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

    public ReplyKeyboard buildPhoneNumberAccess() {
        KeyboardButton accessPboneNumberButton = new KeyboardButton();
        accessPboneNumberButton.setText("Отправить мой номер телефона");

        accessPboneNumberButton.setRequestContact(true);
        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add(accessPboneNumberButton);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardButtons);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboard buildHelloKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        InlineKeyboardButton requestSession = new InlineKeyboardButton();
//        InlineKeyboardButton specialistInformation = new InlineKeyboardButton();
//        InlineKeyboardButton serviceInformation = new InlineKeyboardButton();
        InlineKeyboardButton checkTime = new InlineKeyboardButton();
        InlineKeyboardButton addClient = new InlineKeyboardButton();
//        requestSession.setText("Записаться на сегодня");
//        requestSession.setCallbackData("/show_free_today");
//        specialistInformation.setText("Просмотреть специалистов");
//        specialistInformation.setCallbackData(CommandType.SHOW_SPECIALIST.getCommand());
//        serviceInformation.setText("Просмотреть процедуры");
//        serviceInformation.setCallbackData(CommandType.SHOW_SERVICES.getCommand());
        checkTime.setText("Посмотреть мои записи.");
        checkTime.setCallbackData(CommandType.TIME_SLOTS.getCommand());
        addClient.setText("Добавить запись");
        addClient.setCallbackData(CommandType.ADD_RECORD.getCommand());
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
//        row1.add(requestSession);
//        row2.add(specialistInformation);
//        row3.add(serviceInformation);
        row1.add(checkTime);
        row2.add(addClient);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);
        rowList.add(row3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard buildTimeSlots(JSONArray array){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> timeSlotRow = new ArrayList<>();
        InlineKeyboardButton startTimeButton = new InlineKeyboardButton();
        InlineKeyboardButton endTimeButton = new InlineKeyboardButton();
        startTimeButton.setText("От:");
        startTimeButton.setCallbackData(CommandType.IGNORE.getCommand());
        endTimeButton.setText("До:");
        endTimeButton.setCallbackData(CommandType.IGNORE.getCommand());
        timeSlotRow.add(startTimeButton);
        timeSlotRow.add(endTimeButton);
        rowList.add(timeSlotRow);
        for (int i = 0; i < array.length(); i++) {
            timeSlotRow = new ArrayList<>();
            startTimeButton = new InlineKeyboardButton();
            endTimeButton = new InlineKeyboardButton();
            JSONObject json = array.getJSONObject(i);
            try {
                Date startTime = TIME_PATTERN.parse(json.get("startTime").toString());
                Date endTime  = TIME_PATTERN.parse(json.get("endTime").toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:MM");
                startTimeButton.setText(simpleDateFormat.format(startTime));
                startTimeButton.setCallbackData(CommandType.IGNORE.getCommand());
                endTimeButton.setText(simpleDateFormat.format(endTime));
                endTimeButton.setCallbackData(CommandType.IGNORE.getCommand());
                timeSlotRow.add(startTimeButton);
                timeSlotRow.add(endTimeButton);
                rowList.add(timeSlotRow);
            } catch (Exception e) {}
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static ReplyKeyboard buildSpecialistButtons(JSONArray jsonArray) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> specialistRow = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            InlineKeyboardButton specialistButton = new InlineKeyboardButton();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            specialistButton.setText(jsonObject.get("firstName").toString());
            specialistButton.setCallbackData(CommandType.EXPERT_RECORDS.getCommand() + " " +  jsonObject.get("id").toString());
            specialistRow.add(specialistButton);
        }
        rowList.add(specialistRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static ReplyKeyboard buildExpertProcedures(JSONArray jsonArray, List<Object> proceduresIds) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        int perRow = 0;
        List<InlineKeyboardButton> specialistRow = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            if( perRow % 3 == 0) {
                perRow = 0;
                rowList.add(specialistRow);
                specialistRow = new ArrayList<>();
            }
            InlineKeyboardButton specialistButton = new InlineKeyboardButton();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (proceduresIds.contains(jsonObject.get("id").toString())) {
                specialistButton.setText(jsonObject.get("title").toString() + "\u2705");
                specialistButton.setCallbackData(CommandType.REMOVE_PROCEDURE.getCommand() + " " + jsonObject.get("id").toString());
            } else {
                specialistButton.setText(jsonObject.get("title").toString());
                specialistButton.setCallbackData(CommandType.CHOOSE_PROCEDURE.getCommand() + " " + jsonObject.get("id").toString());
            }
            specialistRow.add(specialistButton);
            perRow++;
        }
        rowList.add(specialistRow);
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton confirmButton = new InlineKeyboardButton();
        confirmButton.setText("Продолжить");
        confirmButton.setCallbackData(CommandType.RENDER_DATE.getCommand());
        secondRow.add(confirmButton);
        rowList.add(secondRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static ReplyKeyboard buildCalendarKeyboard(List wokingDayList) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> weekRow = new ArrayList<>();
        InlineKeyboardButton monday = new InlineKeyboardButton();
        monday.setText("ПН");
        monday.setCallbackData(CommandType.IGNORE.getCommand());
        InlineKeyboardButton tuesday = new InlineKeyboardButton();
        tuesday.setText("ВТ");
        tuesday.setCallbackData(CommandType.IGNORE.getCommand());
        InlineKeyboardButton wednesday = new InlineKeyboardButton();
        wednesday.setText("СР");
        wednesday.setCallbackData(CommandType.IGNORE.getCommand());
        InlineKeyboardButton thirthday = new InlineKeyboardButton();
        thirthday.setText("ЧТ");
        thirthday.setCallbackData(CommandType.IGNORE.getCommand());
        InlineKeyboardButton friday = new InlineKeyboardButton();
        friday.setText("ПТ");
        friday.setCallbackData(CommandType.IGNORE.getCommand());
        InlineKeyboardButton saturday = new InlineKeyboardButton();
        saturday.setText("СБ");
        saturday.setCallbackData(CommandType.IGNORE.getCommand());
        InlineKeyboardButton sunday = new InlineKeyboardButton();
        sunday.setText("ВС");
        sunday.setCallbackData(CommandType.IGNORE.getCommand());
        weekRow.add(monday);
        weekRow.add(tuesday);
        weekRow.add(wednesday);
        weekRow.add(thirthday);
        weekRow.add(friday);
        weekRow.add(saturday);
        weekRow.add(sunday);
        rowList.add(weekRow);

        weekRow = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate localDate1 = now.withDayOfMonth(1);
        DayOfWeek dayOfWeek = localDate1.getDayOfWeek();
        int i;
        for (i = 1; i < dayOfWeek.getValue(); i++) {
            InlineKeyboardButton emptyDay = new InlineKeyboardButton();
            emptyDay.setText("-");
            emptyDay.setCallbackData(String.valueOf(CommandType.IGNORE));
            weekRow.add(emptyDay);
        }
        Calendar instance = Calendar.getInstance();
        for (i = 1; i < instance.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            if (wokingDayList.contains(i)) {
                InlineKeyboardButton day = new InlineKeyboardButton();
                day.setText(String.valueOf(i));
                day.setCallbackData(CommandType.CHOICE_DATE.getCommand() + " " + i);
                weekRow.add(day);
            } else {
                InlineKeyboardButton emptyDay = new InlineKeyboardButton();
                emptyDay.setText("-");
                emptyDay.setCallbackData(String.valueOf(CommandType.IGNORE));
                weekRow.add(emptyDay);
            }
            if (now.withDayOfMonth(i).getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                rowList.add(weekRow);
                weekRow = new ArrayList<>();
            }
        }
        for (int j = now.withDayOfMonth(i).getDayOfWeek().getValue(); j <= 7; j++) {
            InlineKeyboardButton emptyDay = new InlineKeyboardButton();
            emptyDay.setText("-");
            emptyDay.setCallbackData(String.valueOf(CommandType.IGNORE));
            weekRow.add(emptyDay);
        }
        rowList.add(weekRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static ReplyKeyboard buildTimeSlots(List timeSlots) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
        if (timeSlots.isEmpty()) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("К сожалению нету свободных окошечек.");
            inlineKeyboardButton.setCallbackData(CommandType.IGNORE.getCommand());
            buttonsRow.add(inlineKeyboardButton);
            rowList.add(buttonsRow);
            buttonsRow = new ArrayList<>();
            buttonsRow.add(KeyBoardBuilder.buildBackToMainButton());
            rowList.add(buttonsRow);
        } else {
            for (int i = 0; i < timeSlots.size(); i++) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(timeSlots.get(i).toString());
                inlineKeyboardButton.setCallbackData(CommandType.CONFIRM.getCommand() + " " + timeSlots.get(i).toString());
                buttonsRow.add(inlineKeyboardButton);
                if (i % 3 == 0) {
                    rowList.add(buttonsRow);
                    buttonsRow = new ArrayList<>();
                }
            }
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardButton buildBackToMainButton() {
        InlineKeyboardButton backToMenu = new InlineKeyboardButton();
        backToMenu.setText("Вернуться в главное меню");
        backToMenu.setCallbackData(CommandType.START.getCommand());
        return backToMenu;
    }

    public ReplyKeyboard buildClientTimeSlots(JSONArray jsonArray) {
        String pattern = "Вы записаны %s на %s. Цена: %s грн.";
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> recordRow = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            InlineKeyboardButton specialistButton = new InlineKeyboardButton();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String startTime = "";
            try {
                startTime = new SimpleDateFormat("hh:mm").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(jsonObject.getString("startTime")));
            } catch (Exception e) {}
            specialistButton.setText(String.format(pattern, jsonObject.get("date"), startTime, jsonObject.get("price")));
            specialistButton.setCallbackData(CommandType.EXPERT_RECORDS.getCommand() + " " +  jsonObject.get("id").toString());
            recordRow.add(specialistButton);
            rowList.add(recordRow);
            recordRow = new ArrayList<>();
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
