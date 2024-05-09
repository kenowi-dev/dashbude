package dev.kenowi.dashbude.board;

import io.javaoperatorsdk.operator.api.ObservedGenerationAwareStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class DashboardStatus extends ObservedGenerationAwareStatus {

    public enum State {
        PENDING,
        CREATED,
        ERROR,
    }

    private State state = State.PENDING;
    private String message = "";

}
