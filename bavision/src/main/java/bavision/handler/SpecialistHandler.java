package bavision.handler;

import bavision.command.commands.CommandType;
import bavision.entity.Specialist;
import bavision.storage.Storage;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.util.Map;

public class SpecialistHandler extends AbstractHandler<Specialist> {

    public SpecialistHandler(Storage storage) {
        super(storage);
    }

    @Override
    protected Map<Integer, Specialist> getDataFromStorage() {
        return this.storage.getSpecialists();
    }

    @Override
    protected String getPreviousCommand() {
        return CommandType.PREVIOUS_SPECIALIST.getCommand();
    }

    @Override
    protected String getNextCommand() {
        return CommandType.NEXT_SPECIALIST.getCommand();
    }

    @Override
    protected String getPhotoPath() {
        return "specialist/";
    }

    @Override
    public PartialBotApiMethod create(String message) {
        return null;
    }
}
