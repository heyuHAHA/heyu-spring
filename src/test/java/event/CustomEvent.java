package event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

public class CustomEvent extends ApplicationEvent {

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public CustomEvent(ApplicationContext source) {
        super(source);
    }
}
