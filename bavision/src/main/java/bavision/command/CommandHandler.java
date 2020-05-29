package bavision.command;

import bavision.builder.KeyBoardBuilder;
import bavision.command.commands.AdminCommands;
import bavision.command.commands.CommandType;
import bavision.entity.*;
import bavision.handler.AbstractHandler;
import bavision.handler.ServiceHandler;
import bavision.handler.SpecialistHandler;
import bavision.storage.Storage;
import bavision.storage.impl.JsonDbImpl;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static bavision.util.Const.DEFAULT_PICUTRE;

public class CommandHandler {
    private static Map<Enum, MessageHandler<PartialBotApiMethod>> commandTypeHandler = new HashMap<>();
    private KeyBoardBuilder keyBoardBuilder;
    private Storage storage;
    private Map<Class, AbstractHandler> handlers = new HashMap<>();
    private static final Map<Long,String> chatIdPhoneNumber = new HashMap<>();
    private Map<Long, Map<String, Set<String>>> chatIdExpertSelectedProcedures;
    Cache<Long, JSONObject> chatIdOrderInfo;

    public CommandHandler() {
        CacheLoader<Long, JSONObject> loader = getCacheLoader();

        chatIdOrderInfo = CacheBuilder.newBuilder()
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(loader);
        chatIdExpertSelectedProcedures = new HashMap<>();
        storage = new JsonDbImpl();
        keyBoardBuilder = new KeyBoardBuilder();
        handlers.put(Specialist.class, new SpecialistHandler(this.storage));
        handlers.put(Service.class, new ServiceHandler(this.storage));
        commandTypeHandler.put(CommandType.RENDER_DATE, this::renderDate);
        commandTypeHandler.put(CommandType.CHOICE_DATE, this::choiceDate);
        commandTypeHandler.put(CommandType.CONFIRM, this::confirm);
        commandTypeHandler.put(CommandType.CHOOSE_PROCEDURE, this::chooseProcedure);
        commandTypeHandler.put(CommandType.REMOVE_PROCEDURE, this::removeProcedure);
        commandTypeHandler.put(CommandType.EXPERT_RECORDS, this::getExpertByID);
        commandTypeHandler.put(CommandType.TIME_SLOTS, this::getTimeSlots);
        commandTypeHandler.put(CommandType.ADD_RECORD, this::addRecord);
//        commandTypeHandler.put(CommandType.SHOW_FREE, (message -> buildTimeSlots(storage.getTimeSlots().values(), null)));
        commandTypeHandler.put(CommandType.START, message -> buildHelloMessage());
//        commandTypeHandler.put(CommandType.SHOW_SPECIALIST, handlers.get(Specialist.class)::showById);
//        commandTypeHandler.put(CommandType.NEXT_SPECIALIST, handlers.get(Specialist.class)::showNext);
//        commandTypeHandler.put(CommandType.PREVIOUS_SPECIALIST, handlers.get(Specialist.class)::showPrevious);
//        commandTypeHandler.put(CommandType.SHOW_SERVICES, handlers.get(Service.class)::showById);
//        commandTypeHandler.put(CommandType.NEXT_SERVICE, handlers.get(Service.class)::showNext);
//        commandTypeHandler.put(CommandType.PREVIOUS_SERVICE, handlers.get(Service.class)::showPrevious);
        commandTypeHandler.put(CommandType.IGNORE, null);
//
//        commandTypeHandler.put(AdminCommands.CREATE_SERVICE, handlers.get(Service.class)::create);
    }

    private CacheLoader<Long, JSONObject> getCacheLoader() {
        CacheLoader<Long, JSONObject> loader;
        loader = new CacheLoader<Long, JSONObject>() {
            @Override
            public JSONObject load(Long key) {
                return new JSONObject(key);
            }
        };
        return loader;
    }


//    public PartialBotApiMethod handleAdminMethod(String textMessage, Command command) {
//        String s = textMessage.split("\\s")[1];
//        if (!s.equals("123qwe")) {
//            return null;
//        }
//        MessageHandler<PartialBotApiMethod> stringMessageHandler;
//        try {
//            stringMessageHandler = commandTypeHandler.get(parseAdminCommandType(command.getCommandType()));
//        } catch (IllegalArgumentException e) {
//            stringMessageHandler = null;
//        }
//        if (stringMessageHandler == null) {
//            return null;
//        }
//        PartialBotApiMethod call = stringMessageHandler.call(textMessage);
//        return call;
//    }

    public PartialBotApiMethod handle(Message message) {
        if (message.getContact() != null) {
            chatIdPhoneNumber.put(message.getFrom().getId().longValue(), message.getContact().getPhoneNumber());
        }
        String textMessage = message.getText();
        Command command;
        if ((message instanceof MessageWrapper) && ((MessageWrapper)message).getAttribte("callbackdata")!= null) {
            command = parseTextMessage(((MessageWrapper)message).getAttribte("callbackdata").toString());
        } else {
            command = parseTextMessage(textMessage);
        }

        if (command == null) {
            command = new Command();
            command.setCommand(CommandType.START.getCommand());
        }

        MessageHandler<PartialBotApiMethod> stringMessageHandler;
        try {
             stringMessageHandler = commandTypeHandler.get(parseCommandType(command.getCommandType()));
        } catch (IllegalArgumentException e) {
            stringMessageHandler = null;
        }
        if (stringMessageHandler == null) {
            return null;
        }
        return stringMessageHandler.call(message);
    }

    private Command parseTextMessage(String textMessage) {
        if (textMessage == null || textMessage.isEmpty()) {
            return null;
        }
        String[] split = textMessage.split("\\s");
        Command command = new Command();
        command.setCommand(split[0]);
        if (split.length > 1) {
            command.setCommandText(split[1]);
        }
        return command;
    }
    private CommandType parseCommandType(String command) throws IllegalArgumentException {
        return CommandType.valueOf(command.substring(1).toUpperCase());
    }

    private AdminCommands parseAdminCommandType(String command) throws IllegalArgumentException {
        return AdminCommands.valueOf(command.substring(1).toUpperCase());
    }

    private PartialBotApiMethod buildHelloMessage() {
        try {
            return new SendPhoto()
                    .setCaption("Welcome!")
                    .setPhoto("BAVision", new FileInputStream(new File(DEFAULT_PICUTRE)))
                    .setPhoto(new File("D:\\test.jpg"))
                    .setReplyMarkup(new ReplyKeyboardRemove())
                    .setReplyMarkup(keyBoardBuilder.buildHelloKeyboard());
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }


    private PartialBotApiMethod getTimeSlots(Message message) {
        String phoneNumber = chatIdPhoneNumber.get(message.getChatId());
        if (phoneNumber == null) {
            return new SendMessage().setText("Отправте номер телефона для получения сегодняшнго рассписания").setReplyMarkup(keyBoardBuilder.buildPhoneNumberAccess());
        }
        String format = String.format("http://localhost:3000/experts/get_by_phone/%s/time_slots.json", phoneNumber);
        WebTarget target = ClientBuilder.newClient().target(format);
        String response = target.request().get(String.class);
        JSONArray objects = new JSONArray(response);
        return new SendMessage().setText("Рассписание на сегодня: ").setReplyMarkup(new ReplyKeyboardRemove()).setReplyMarkup(keyBoardBuilder.buildTimeSlots(objects));
    }

    private PartialBotApiMethod addRecord(Message message) {
        Message messageOriginal = (Message)((MessageWrapper)message).getAttribte("message");

        if (!chatIdPhoneNumber.containsKey(messageOriginal.getChat().getId())) {
            return new SendMessage().setText("Хотите предоставить нам свой номер?").setReplyMarkup(keyBoardBuilder.buildPhoneNumberAccess());
        }
        WebTarget target = ClientBuilder.newClient().target("http://localhost:3000/experts.json");
        String response = target.request().get(String.class);
        return new SendMessage().setText("Мастера").setReplyMarkup(KeyBoardBuilder.buildSpecialistButtons(new JSONArray(response)));
    }

    private PartialBotApiMethod getExpertByID(Message message) {
        String callbackdata = ((MessageWrapper) message).getAttribte("callbackdata").toString();
        String expertId = callbackdata.split("\\s")[1];
        WebTarget target = ClientBuilder.newClient().target(String.format("http://localhost:3000/experts/%s/procedures.json", expertId));
        String response = target.request().get(String.class);

        Message messageOriginal = (Message)((MessageWrapper)message).getAttribte("message");
        JSONObject v = new JSONObject();
        v.put("expertId", expertId);

        if (!v.has("procedures")) {
            v.put("procedures", new HashSet<>());
        }
        JSONArray procedures = v.getJSONArray("procedures");

        chatIdOrderInfo.put(messageOriginal.getChatId(), v);

        return new SendMessage().setText("Процедуры мастера").setReplyMarkup(KeyBoardBuilder.buildExpertProcedures(new JSONArray(response), procedures.toList()));
    }

    private PartialBotApiMethod chooseProcedure(Message message) {
        String callbackdata = ((MessageWrapper) message).getAttribte("callbackdata").toString();
        Message messageOriginal = (Message)((MessageWrapper)message).getAttribte("message");
        JSONObject orderInfo = chatIdOrderInfo.getIfPresent(messageOriginal.getChatId());
        String procedureId = callbackdata.split("\\s")[1];
        List<Object> procedures = orderInfo.getJSONArray("procedures").toList();
        procedures.add(procedureId);

        orderInfo.put("procedures", procedures);
        chatIdOrderInfo.put(messageOriginal.getChatId(), orderInfo);

        String expertId = orderInfo.getString("expertId");
        WebTarget target = ClientBuilder.newClient().target(String.format("http://localhost:3000/experts/%s/procedures.json", expertId));
        String response = target.request().get(String.class);
        return new SendMessage().setText("Процедуры мастера").setReplyMarkup(KeyBoardBuilder.buildExpertProcedures(new JSONArray(response), procedures));
    }


    private PartialBotApiMethod removeProcedure(Message message) {
        String callBackData = ((MessageWrapper) message).getAttribte("callbackdata").toString();
        Message messageOriginal = (Message)((MessageWrapper)message).getAttribte("message");
        JSONObject orderInfo = chatIdOrderInfo.getIfPresent(messageOriginal.getChatId());
        String procedureId = callBackData.split("\\s")[1];

        List<Object> procedures = orderInfo.getJSONArray("procedures").toList();
        procedures.remove(procedureId);
        orderInfo.put("procedures", procedures);
        chatIdOrderInfo.put(messageOriginal.getChatId(), orderInfo);

        String expertId = orderInfo.getString("expertId");
        WebTarget target = ClientBuilder.newClient().target(String.format("http://localhost:3000/experts/%s/procedures.json", expertId));
        String response = target.request().get(String.class);
        return new SendMessage().setText("Процедуры мастера").setReplyMarkup(KeyBoardBuilder.buildExpertProcedures(new JSONArray(response), procedures));
    }

    private PartialBotApiMethod renderDate(Message message) {
        Message messageOriginal = (Message)((MessageWrapper)message).getAttribte("message");
        JSONObject orderInfo = chatIdOrderInfo.getIfPresent(messageOriginal.getChatId());

        String expertId = orderInfo.getString("expertId");
        WebTarget target = ClientBuilder.newClient().target(String.format("http://localhost:3000/experts/%s/workingDays.json", expertId));

        JSONArray jsonArray = new JSONArray(target.request().get(String.class));
        List<Integer> workingDaysNumber = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            workingDaysNumber.add(jsonArray.getJSONObject(i).getInt("dayNumber"));
        }
        return new SendMessage().setText("Процедуры мастера").setReplyMarkup(KeyBoardBuilder.buildCalendarKeyboard(workingDaysNumber));
    }

    private PartialBotApiMethod choiceDate(Message message) {
        String callBackData = ((MessageWrapper) message).getAttribte("callbackdata").toString();
        Message messageOriginal = (Message)((MessageWrapper)message).getAttribte("message");
        JSONObject orderInfo = chatIdOrderInfo.getIfPresent(messageOriginal.getChatId());
        String dayNumber = callBackData.split("\\s")[1];
        orderInfo.put("dayNumber", dayNumber);
        chatIdOrderInfo.put(messageOriginal.getChatId(), orderInfo);
        String expertId = orderInfo.getString("expertId");
        WebTarget target = ClientBuilder.newClient().target(String.format("http://localhost:3000/experts/%s/%s/getPossibleTimeSlots.json", expertId ,dayNumber));
        for (Object procedures : orderInfo.getJSONArray("procedures").toList()) {
            target = target.queryParam("procedures", procedures);
        }
        JSONArray jsonArray = new JSONArray(target.request().get(String.class));
        return new SendMessage().setText("Выберите время").setReplyMarkup(KeyBoardBuilder.buildTimeSlots(jsonArray.toList()));
    }

    private PartialBotApiMethod confirm(Message message) {
        String callBackData = ((MessageWrapper) message).getAttribte("callbackdata").toString();
        Message messageOriginal = (Message)((MessageWrapper)message).getAttribte("message");

        String choicedTime = callBackData.split("\\s")[1];
        JSONObject orderInfo = chatIdOrderInfo.getIfPresent(messageOriginal.getChatId());
        orderInfo.put("time", choicedTime);
        chatIdOrderInfo.put(messageOriginal.getChatId(), orderInfo);
        String phoneNumber = chatIdPhoneNumber.get(messageOriginal.getChatId());
        Response response = buildConfirmRecord(orderInfo, phoneNumber, messageOriginal.getChat().getFirstName());
        if (response.getStatus() == 200) {
            return getClientTimeSlots(messageOriginal);
        }
        return null;
    }

    private PartialBotApiMethod getClientTimeSlots(Message message) {
        String phoneNumber = chatIdPhoneNumber.get(message.getChatId());
        String format = String.format("http://localhost:3000/time_slots/get_by_client_phone/%s.json", phoneNumber);
        WebTarget target = ClientBuilder.newClient().target(format);
        String response = target.request().get(String.class);
        JSONArray objects = new JSONArray(response);
        return new SendMessage().setText("Записи").setReplyMarkup(keyBoardBuilder.buildClientTimeSlots(objects));
    }

    public Response  buildConfirmRecord(JSONObject orderInfo, String phoneNumber, String firstName) {
        WebTarget target = ClientBuilder.newClient().target("http://localhost:3000/time_slots");
        JSONObject resultObject = new JSONObject();
        resultObject.put("procedures", orderInfo.getJSONArray("procedures"));
        resultObject.put("client_phone_number", phoneNumber);
        resultObject.put("client_first_name", firstName);
        resultObject.put("expert_id", orderInfo.getString("expertId"));
        resultObject.put("date", java.time.LocalDate.now().toString());
        JSONObject value = new JSONObject();
        LocalDateTime localDateTime = LocalDateTime.now();
        value.put("start_time(1i)", localDateTime.getYear());
        value.put("start_time(2i)", localDateTime.getMonthValue());
        value.put("start_time(3i)", orderInfo.get("dayNumber"));
        String time = orderInfo.getString("time");
        String hour = time.split(":")[0];
        String minutes = time.split(":")[1];
        value.put("start_time(4i)", hour);
        value.put("start_time(5i)", minutes);
        resultObject.put("detail_form", value);
        return target.request().post(Entity.json(resultObject.toString()));
    }


}
