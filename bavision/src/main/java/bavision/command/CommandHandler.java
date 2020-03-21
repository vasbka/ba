package bavision.command;

import bavision.builder.KeyBoardBuilder;
import bavision.command.commands.AdminCommands;
import bavision.command.commands.CommandType;
import bavision.entity.*;
import bavision.handler.AbstractHandler;
import bavision.handler.ServiceHandler;
import bavision.handler.SpecialistHandler;
import bavision.storage.Storage;
import bavision.storage.impl.InMemoryStorageImpl;
import bavision.storage.impl.JsonDbImpl;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import static bavision.util.Const.CLOCK_SMILE_CODE;
import static bavision.util.Const.DEFAULT_PICUTRE;

public class CommandHandler {
    private static Map<Enum, MessageHandler<PartialBotApiMethod>> commandTypeHandler = new HashMap<>();
    private KeyBoardBuilder keyBoardBuilder;
    private Storage storage;
    private Map<Class, AbstractHandler> handlers = new HashMap<>();

    public CommandHandler() {
        storage = new JsonDbImpl();
        keyBoardBuilder = new KeyBoardBuilder();
        handlers.put(Specialist.class, new SpecialistHandler(this.storage));
        handlers.put(Service.class, new ServiceHandler(this.storage));
        commandTypeHandler.put(CommandType.SHOW_FREE, (message -> buildTimeSlots(storage.getTimeSlots().values(), null)));
        commandTypeHandler.put(CommandType.SHOW_FREE_TODAY, message -> buildFreeTimesSlotsToday(storage.getTimeSlots().values()));
        commandTypeHandler.put(CommandType.START, message -> buildHelloMessage());
        commandTypeHandler.put(CommandType.SHOW_SPECIALIST, handlers.get(Specialist.class)::showById);
        commandTypeHandler.put(CommandType.NEXT_SPECIALIST, handlers.get(Specialist.class)::showNext);
        commandTypeHandler.put(CommandType.PREVIOUS_SPECIALIST, handlers.get(Specialist.class)::showPrevious);
        commandTypeHandler.put(CommandType.SHOW_SERVICES, handlers.get(Service.class)::showById);
        commandTypeHandler.put(CommandType.NEXT_SERVICE, handlers.get(Service.class)::showNext);
        commandTypeHandler.put(CommandType.PREVIOUS_SERVICE, handlers.get(Service.class)::showPrevious);
        commandTypeHandler.put(CommandType.IGNORE, null);

        commandTypeHandler.put(AdminCommands.CREATE_SERVICE, handlers.get(Service.class)::create);
    }


    public PartialBotApiMethod handleAdminMethod(String textMessage, Command command) {
        String s = textMessage.split("\\s")[1];
        if (!s.equals("123qwe")) {
            return null;
        }
        MessageHandler<PartialBotApiMethod> stringMessageHandler;
        try {
            stringMessageHandler = commandTypeHandler.get(parseAdminCommandType(command.getCommandType()));
        } catch (IllegalArgumentException e) {
            stringMessageHandler = null;
        }
        if (stringMessageHandler == null) {
            return null;
        }
        PartialBotApiMethod call = stringMessageHandler.call(textMessage);
        return call;
    }

    public PartialBotApiMethod handle(String textMessage) {
        Command command = parseTextMessage(textMessage);
        for (AdminCommands value : AdminCommands.values()) {
            if (command.getCommandType().equals(value.getAdminCommand())) {
                return handleAdminMethod(textMessage, command);
            }
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
        PartialBotApiMethod call = stringMessageHandler.call(command.getCommandText());
        return call;
    }

    private Command parseTextMessage(String textMessage) {
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
                    .setReplyMarkup(keyBoardBuilder.buildHelloKeyboard());
        } catch (Exception e){
        }
        return null;
    }

    private PartialBotApiMethod buildFreeTimesSlotsToday(Collection<TimeSlot> slots) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime of = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59);
        return buildTimeSlots(slots, of);
    }

    private PartialBotApiMethod buildTimeSlots(Collection<TimeSlot> slots, LocalDateTime localDate) {
        StringBuilder stringBuilder = new StringBuilder();
        Stream<TimeSlot> localSlots = slots.stream();
        if (localDate != null) {
            LocalDateTime startDate = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
            localSlots = slots.stream()
                    .filter(timeSlot -> timeSlot.getDate().getDayOfMonth() == localDate.getDayOfMonth())
                    .filter(timeSlot -> timeSlot.getDate().compareTo(localDate) < 0)
                    .filter(timeSlot -> timeSlot.getDate().compareTo(startDate) > 0);
        }
        localSlots.forEach(timeSlot -> {
            LocalDateTime date = timeSlot.getDate();
            stringBuilder.append(date.format(DateTimeFormatter.ofPattern("dd"))).append(System.lineSeparator());
            stringBuilder.append(CLOCK_SMILE_CODE).append(date.format(DateTimeFormatter.ofPattern("HH"))).append(" : ").append(date.format(DateTimeFormatter.ofPattern("mm"))).append(" - ").append(timeSlot.getSpecialist().toBeatyString());
        });
        return new SendMessage().setText(stringBuilder.toString());
    }


}
