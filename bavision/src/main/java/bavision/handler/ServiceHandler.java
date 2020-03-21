package bavision.handler;

import bavision.command.commands.CommandType;
import bavision.entity.Service;
import bavision.storage.Storage;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.Map;

public class ServiceHandler extends AbstractHandler<Service> {

    public ServiceHandler(Storage storage) {
        super(storage);
    }

    @Override
    protected Map<Integer, Service> getDataFromStorage() {
        return this.storage.getServices();
    }

    @Override
    protected String getPreviousCommand() {
        return CommandType.PREVIOUS_SERVICE.getCommand();
    }

    @Override
    protected String getNextCommand() {
        return CommandType.NEXT_SERVICE.getCommand();
    }

    @Override
    protected String getPhotoPath() {
        return "service/";
    }

    @Override
    public PartialBotApiMethod create(String message) {
        System.out.println(Arrays.toString(message.split("\n")));
        Service service = new Service();
        service.setName(message.split("\n")[1]);
        service.setDescription(message.split("\n")[2]);
        this.storage.addService(service);
        return new SendMessage().setText("service " + service + " sucessfully created");
    }
}
