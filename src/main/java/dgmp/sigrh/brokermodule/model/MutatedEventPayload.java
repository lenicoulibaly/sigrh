package dgmp.sigrh.brokermodule.model;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MutatedEventPayload<EventType, Obj>
{
    protected EventType eventType;
    protected Obj newlyMutatedObject;
    protected EventActorIdentifier eventIdentifier;
}
