package dev.kenowi.dashbude.board;

import dev.kenowi.dashbude.app.App;
import dev.kenowi.dashbude.bookmark.Bookmark;
import dev.kenowi.dashbude.shared.DashboardItem;
import dev.kenowi.dashbude.shared.exceptions.WorkflowReconcileException;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.javaoperatorsdk.operator.api.config.informer.InformerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import io.javaoperatorsdk.operator.processing.dependent.workflow.WorkflowReconcileResult;
import io.javaoperatorsdk.operator.processing.event.ResourceID;
import io.javaoperatorsdk.operator.processing.event.source.EventSource;
import io.javaoperatorsdk.operator.processing.event.source.informer.InformerEventSource;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static dev.kenowi.dashbude.board.DashboardStatus.State.*;


@SuppressWarnings("unused")
@ControllerConfiguration(
        dependents = {
                @Dependent(type = ConfigMapDependentResource.class, name = "configMap"),
                @Dependent(type = DeploymentDependentResource.class, name = "deployment", dependsOn = "configMap"),
                @Dependent(type = ServiceDependentResource.class, dependsOn = "deployment")
        },
        maxReconciliationInterval = @MaxReconciliationInterval(interval = 120, timeUnit = TimeUnit.SECONDS))
public class DashboardReconciler implements Reconciler<Dashboard>, EventSourceInitializer<Dashboard>, ErrorStatusHandler<Dashboard> {


    @Override
    public Map<String, EventSource> prepareEventSources(EventSourceContext<Dashboard> context) {

        InformerEventSource<App, Dashboard> apps = new InformerEventSource<>(InformerConfiguration
                .from(App.class, context)
                //.withGenericFilter(resource -> resource.getSpec().getDashboardSelector() != null)
                .withSecondaryToPrimaryMapper(app -> getDashboardResourceIDs(context.getClient(), app.getSpec()))
                .build(), context);

        InformerEventSource<Bookmark, Dashboard> bookmarks = new InformerEventSource<>(InformerConfiguration
                .from(Bookmark.class, context)
                .withSecondaryToPrimaryMapper(bookmark -> getDashboardResourceIDs(context.getClient(), bookmark.getSpec()))
                .build(), context);

        return EventSourceInitializer.nameEventSources(apps, bookmarks);

    }

    @Override
    public UpdateControl<Dashboard> reconcile(Dashboard d, Context<Dashboard> context) throws Exception {

        WorkflowReconcileResult wr = context.managedDependentResourceContext()
                .getWorkflowReconcileResult()
                .orElseThrow();

        if (wr.allDependentResourcesReady()) {
            d.setStatus(new DashboardStatus().setState(CREATED));
            return UpdateControl.patchStatus(d);
        }

        if (!wr.getErroredDependents().isEmpty()) {
            throw new WorkflowReconcileException(wr.getErroredDependents());
        }

        d.setStatus(new DashboardStatus().setState(PENDING));
        final var duration = Duration.ofSeconds(1);
        String retryMsg = "App %s is not ready yet, rescheduling reconciliation after %d s"
                .formatted(d.getMetadata().getName(), duration.toSeconds());
        return UpdateControl.<Dashboard>noUpdate().rescheduleAfter(duration);
    }

    @Override
    public ErrorStatusUpdateControl<Dashboard> updateErrorStatus(Dashboard resource, Context<Dashboard> context, Exception e) {
        DashboardStatus status = new DashboardStatus()
                .setState(ERROR)
                .setMessage("Error: " + e.getMessage());
        resource.setStatus(status);
        return ErrorStatusUpdateControl.updateStatus(resource);
    }

    private <I extends DashboardItem> Set<ResourceID> getDashboardResourceIDs(
            KubernetesClient client,
            I dashboardItem) {
        return client
                .resources(Dashboard.class)
                //.withField(SELECTOR_FIELD, this.getSpec().getDashboardSelector())
                .resources()
                .map(Resource::item)
                .filter(dashboard -> dashboard.getSpec() != null)
                .filter(dashboard -> dashboard.getSpec().getSelectorName() != null)
                .filter(dashboard -> dashboard.getSpec().getSelectorName().equals(dashboardItem.getDashboardSelector()))
                .map(ResourceID::fromResource)
                .collect(Collectors.toSet());


    }

}
