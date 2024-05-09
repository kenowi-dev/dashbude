package dev.kenowi.dashbude.app;

import dev.kenowi.dashbude.shared.DashboardItemStatus;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("dashbude.kenowi.dev")
@Version("v1alpha1")
public class App extends CustomResource<AppSpec, DashboardItemStatus> implements Namespaced {


}
