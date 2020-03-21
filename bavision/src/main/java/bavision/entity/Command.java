package bavision.entity;

import java.util.Objects;

public class Command {
    private String commandType;
    private String commandText;

    public Command() {
    }

    public Command(String commandType, String commandText) {
        this.commandType = commandType;
        this.commandText = commandText;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommand(String commandType) {
        this.commandType = commandType;
    }

    public String getCommandText() {
        return commandText;
    }

    public void setCommandText(String commandText) {
        this.commandText = commandText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return commandType == command.commandType &&
                Objects.equals(commandText, command.commandText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, commandText);
    }

    @Override
    public String toString() {
        return "Command{" +
                "commandType=" + commandType +
                ", commandText='" + commandText + '\'' +
                '}';
    }
}
