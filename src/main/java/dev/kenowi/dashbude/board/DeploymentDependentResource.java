package dev.kenowi.dashbude.board;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpecBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

import java.util.List;
import java.util.Map;

@KubernetesDependent
public class DeploymentDependentResource extends CRUDKubernetesDependentResource<Deployment, Dashboard> {


    public DeploymentDependentResource() {
        super(Deployment.class);
    }



    @Override
    protected Deployment desired(Dashboard dashboard, Context<Dashboard> context) {
        /*ConfigMap configMap = context.getClient()
                .configMaps()
                .inNamespace(dashboard.getMetadata().getNamespace())
                .withName(dashboard.getMetadata().getName())
                .item();

         */


        ConfigMap configMap = context.getSecondaryResource(ConfigMap.class).orElseThrow();

        String appsJson = configMap.getData().get("apps.json");
        String bookmarkJson = configMap.getData().get("links.json");

        String deploymentName = dashboard.getMetadata().getName();
        Deployment deployment = new DeploymentBuilder()
                .withMetadata(new ObjectMetaBuilder()
                        .withName(deploymentName)
                        .withNamespace(dashboard.getMetadata().getNamespace())
                        .build()
                )
                .withSpec(new DeploymentSpecBuilder()
                        .withSelector(new LabelSelectorBuilder()
                                .addToMatchLabels("app", deploymentName)
                                .build())
                        .withReplicas(1)
                        .withTemplate(new PodTemplateSpecBuilder()
                                .withMetadata(new ObjectMetaBuilder()
                                        .addToLabels("app", deploymentName)
                                        .withAnnotations(Map.of("apps", appsJson, "bookmarks", bookmarkJson))
                                        .build())
                                .withSpec(new PodSpecBuilder()
                                        .addToContainers(
                                                new ContainerBuilder()
                                                        .withName(deploymentName)
                                                        .withImage("griefed/sui:latest")
                                                        .withImagePullPolicy("IfNotPresent")
                                                        .withPorts(new ContainerPortBuilder()
                                                                .withContainerPort(80)
                                                                .withName("http")
                                                                .build())
                                                        .addToEnv(new EnvVar("PROTOCOL", "http", null))
                                                        .withVolumeMounts(List.of(
                                                                new VolumeMountBuilder()
                                                                        .withName("config-volume")
                                                                        .withMountPath("defaults/apps.json")
                                                                        .withSubPath("apps.json")
                                                                        .build(),
                                                                new VolumeMountBuilder()
                                                                        .withName("config-volume")
                                                                        .withMountPath("defaults/links.json")
                                                                        .withSubPath("links.json")
                                                                        .build()))
                                                        .build())
                                        .withVolumes(new VolumeBuilder()
                                                .withName("config-volume")
                                                .withConfigMap(new ConfigMapVolumeSourceBuilder()
                                                        .withName(deploymentName)
                                                        .build())
                                                .build())
                                         .build())
                                .build())
                        .build())
                .build();
        deployment.getMetadata().setName(deploymentName);
        deployment.getMetadata().setNamespace(dashboard.getMetadata().getNamespace());
        deployment.getSpec().getSelector().getMatchLabels().put("app", deploymentName);

        deployment.getSpec().getTemplate().getMetadata().getLabels().put("app", deploymentName);
        return deployment;
    }
}
