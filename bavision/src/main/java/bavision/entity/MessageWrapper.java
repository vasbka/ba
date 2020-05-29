package bavision.entity;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;

public class MessageWrapper extends Message {
    private Map<String, Object> attributes;

    public MessageWrapper() {
        attributes = new HashMap<>();
    }

    public void setAttribute(String attributeName, Object value) {
        attributes.put(attributeName, value);
    }

    public Object getAttribte(String attrName) {
        return attributes.get(attrName);
    }

}
