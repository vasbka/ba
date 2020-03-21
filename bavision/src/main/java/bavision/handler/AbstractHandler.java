package bavision.handler;

import bavision.builder.KeyBoardBuilder;
import bavision.entity.Entity;
import bavision.entity.Service;
import bavision.entity.Specialist;
import bavision.storage.Storage;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.File;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bavision.util.Const.DEFAULT_PICUTRE;
import static bavision.util.Const.DEFAULT_PICUTRE_FORMAT;

public abstract class AbstractHandler<E extends Entity> {

    protected Storage storage;
    private KeyBoardBuilder keyBoardBuilder;


    protected AbstractHandler(Storage storage) {
        this.storage = storage;
        this.keyBoardBuilder = new KeyBoardBuilder();
    }

    private PartialBotApiMethod buildMessageBySpecificId(int currentId, Stream<Integer> min, Stream<Integer> max, String nextItem,
                                                         String previousItem, String photoPath, Entity entity) {
        ReplyKeyboard replyKeyboard = keyBoardBuilder.buildNavigationButtons(
                currentId, min.mapToInt(value -> value).min().getAsInt(), max.mapToInt(value -> value).max().getAsInt(),
                nextItem, previousItem);
        File file = new File(photoPath + entity.getId() + DEFAULT_PICUTRE_FORMAT);
        if (!file.exists()) {
            file = new File(DEFAULT_PICUTRE);
        }
        return new SendPhoto()
                .setCaption(entity.toBeatyString())
                .setPhoto(file)
                .setReplyMarkup(replyKeyboard);
    }

    public PartialBotApiMethod showById(String message) {
        int currentSpecialistId = 0;
        if (message != null) {
            currentSpecialistId = Integer.valueOf(message);
        }
        return prepareAccordingToEntity(currentSpecialistId, getClass().getGenericSuperclass().getClass());
    }

    public PartialBotApiMethod showPrevious(String message) {
        return showPrevious(message, Specialist.class);
    }

    public PartialBotApiMethod showNext(String message) {
        return showNext(message, Specialist.class);
    }

    protected Stream<Integer> getSortedListOfIdsByClassType(Class clasz, Comparator<Integer> comparator) {
        if (clasz == Specialist.class) {
            return getDataFromStorage().keySet().stream().sorted(comparator);
        }
        if (clasz == Service.class) {
            return getDataFromStorage().keySet().stream().sorted(comparator);
        }
        return null;
    }


    protected PartialBotApiMethod showPrevious(String message, Class clasz) {
        Integer currentId = Integer.valueOf(message);
        Stream<Integer> sorted = getSortedListOfIdsByClassType(clasz, Comparator.naturalOrder());
        Integer previousId = getValueBeforeCurrentId(currentId, sorted);
        return this.prepareAccordingToEntity(previousId, clasz);
    }


    protected PartialBotApiMethod showNext(String message, Class clasz) {
        Integer currentId = Integer.valueOf(message);
        Stream<Integer> sorted = getSortedListOfIdsByClassType(clasz, Comparator.reverseOrder());
        Integer nextId = getValueBeforeCurrentId(currentId, sorted);
        return this.prepareAccordingToEntity(nextId, clasz);
    }

    protected Integer getValueBeforeCurrentId(Integer currentId, Stream<Integer> sorted) {
        Integer searchedId = -1;
        for (Integer integer : sorted.collect(Collectors.toList())) {
            if (currentId.equals(integer)) {
                break;
            }
            searchedId = integer;
        }
        if (searchedId == -1) {
            searchedId = currentId;
        }
        return searchedId;
    }

    protected PartialBotApiMethod prepareAccordingToEntity(int currentId, Class clasz) {
        Stream<Integer> minStream = getDataFromStorage().values().stream().map(Entity::getId);
        Stream<Integer> maxStream = getDataFromStorage().values().stream().map(Entity::getId);
        String nextItem = getNextCommand();
        String previousItem = getPreviousCommand();
        String photoPath = getPhotoPath();
        Entity entity = getDataFromStorage().get(currentId);
        if (validate(minStream, maxStream, nextItem, previousItem, photoPath, entity)) {
            return null;
        }
        return buildMessageBySpecificId(currentId, minStream, maxStream, nextItem, previousItem, photoPath, entity);
    }

    private boolean validate(Stream<Integer> minStream, Stream<Integer> maxStream, String nextItem, String previousItem, String photoPath, Entity entity) {
        return minStream == null || maxStream == null
                || nextItem == null || previousItem == null
                || photoPath == null || entity == null;
    }

    public abstract PartialBotApiMethod create(String message);
    protected abstract Map<Integer, E> getDataFromStorage();
    protected abstract String getPreviousCommand();
    protected abstract String getNextCommand();
    protected abstract String getPhotoPath();
}
