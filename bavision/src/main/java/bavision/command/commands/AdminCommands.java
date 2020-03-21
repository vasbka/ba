package bavision.command.commands;

public enum AdminCommands  {
    CREATE_SERVICE("/create_service");

    private String adminCommand;

    AdminCommands(String adminCommand) {
        this.adminCommand = adminCommand;
    }

    public String getAdminCommand() {
        return adminCommand;
    }
}
