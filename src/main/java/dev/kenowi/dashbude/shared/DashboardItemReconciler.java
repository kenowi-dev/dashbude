package dev.kenowi.dashbude.shared;

import dev.kenowi.dashbude.app.App;
import dev.kenowi.dashbude.bookmark.Bookmark;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.CustomResource;
import io.javaoperatorsdk.operator.api.reconciler.*;

import static dev.kenowi.dashbude.shared.DashboardItem.DASHBOARD_SELECTOR_FIELD;
import static dev.kenowi.dashbude.shared.DashboardItemStatus.State.ERROR;
import static dev.kenowi.dashbude.shared.DashboardItemStatus.State.READY;

public abstract class DashboardItemReconciler<I extends CustomResource<? extends DashboardItem, DashboardItemStatus>> implements Reconciler<I>, ErrorStatusHandler<I> {

    @Override
    public UpdateControl<I> reconcile(I resource, Context<I> context) throws Exception {

        if (resource.getSpec().getDashboardSelector() == null) {
            throw new IllegalArgumentException(DASHBOARD_SELECTOR_FIELD + " cannot be null.");
        }

        resource.setStatus(new DashboardItemStatus().setState(READY));
        return UpdateControl.patchStatus(resource);
    }

    @Override
    public ErrorStatusUpdateControl<I> updateErrorStatus(I resource, Context<I> context, Exception e) {
        DashboardItemStatus status = new DashboardItemStatus()
                .setState(ERROR)
                .setMessage("Error: " + e.getMessage());
        resource.setStatus(status);
        return ErrorStatusUpdateControl.patchStatus(resource);
    }
}