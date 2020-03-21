package bavision.command;

public interface MessageHandler<E> {
    E call(String message);
}
