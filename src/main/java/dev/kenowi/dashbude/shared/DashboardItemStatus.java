package dev.kenowi.dashbude.shared;

import dev.kenowi.dashbude.app.App;
import io.javaoperatorsdk.operator.api.ObservedGenerationAwareStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class DashboardItemStatus extends ObservedGenerationAwareStatus {
    public enum State {
        ERROR,
        READY
    }


    private State state;
    private String message = "";
}
