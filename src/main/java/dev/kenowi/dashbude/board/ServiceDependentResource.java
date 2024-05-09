package dev.kenowi.dashbude.board;

import io.fabric8.kubernetes.api.model.*;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent
public class ServiceDependentResource extends CRUDKubernetesDependentResource<Service, Dashboard> {
    public ServiceDependentResource() {
        super(Service.class);
    }

    @Override
    protected Service desired(Dashboard dashboard, Context<Dashboard> context) {
        String serviceName = dashboard.getMetadata().getName();
        Service service = new ServiceBuilder()
                .withMetadata(new ObjectMetaBuilder()
                        .withName(serviceName)
                        .withNamespace(dashboard.getMetadata().getNamespace())
                        .build())
                .withSpec(new ServiceSpecBuilder()
                        .addToSelector("app", serviceName)
                        .withPorts(new ServicePortBuilder()
                                .withProtocol("TCP")
                                .withPort(80)
                                .withName("http")
                                .withTargetPort(new IntOrString("http"))
                                .build())
                        .build())
                .build();
        service.getMetadata().setName(dashboard.getMetadata().getName());
        service.getMetadata().setNamespace(dashboard.getMetadata().getNamespace());
        service.getSpec().getSelector().put("app", dashboard.getMetadata().getName());
        return service;
    }
}
