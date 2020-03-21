package bavision.command.commands;

public enum CommandType {
    SHOW_FREE("/show_free"), HELP("/help"),
    SHOW_FREE_TODAY("/show_free_today"), START("/start"),
    SHOW_SPECIALIST("/show_specialist"), NEXT_SPECIALIST("/next_specialist"),
    PREVIOUS_SPECIALIST("/previous_specialist"), IGNORE("/ignore"),
    SHOW_SERVICES("/show_services"), NEXT_SERVICE("/next_service"),
    PREVIOUS_SERVICE("/previous_service");


    private String command;

    CommandType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

}
