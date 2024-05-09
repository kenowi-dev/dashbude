package dev.kenowi.dashbude.bookmark;

import dev.kenowi.dashbude.app.App;
import dev.kenowi.dashbude.board.Dashboard;
import dev.kenowi.dashbude.shared.DashboardItem;
import dev.kenowi.dashbude.shared.DashboardItemStatus;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import io.javaoperatorsdk.operator.api.ObservedGenerationAwareStatus;
import io.javaoperatorsdk.operator.processing.event.ResourceID;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Group("dashbude.kenowi.dev")
@Version("v1alpha1")
public class Bookmark extends CustomResource<BookmarkSpec, DashboardItemStatus> implements Namespaced {


}
