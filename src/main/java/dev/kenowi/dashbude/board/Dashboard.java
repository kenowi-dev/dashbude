package dev.kenowi.dashbude.board;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("dashbude.kenowi.dev")
@Version("v1alpha1")
public class Dashboard extends CustomResource<DashboardSpec, DashboardStatus> implements Namespaced {

}
