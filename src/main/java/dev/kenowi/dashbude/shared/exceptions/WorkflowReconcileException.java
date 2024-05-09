package dev.kenowi.dashbude.shared.exceptions;

import io.javaoperatorsdk.operator.api.reconciler.dependent.DependentResource;

import java.util.Map;
import java.util.stream.Collectors;

public class WorkflowReconcileException extends Exception {


    @SuppressWarnings("rawtypes")
    public WorkflowReconcileException(Map<DependentResource, Exception> exceptionMap) {
        super(exceptionMap
                .entrySet()
                .stream()
                .map(e -> "%s: %s"
                        .formatted(e.getKey().resourceType().getSimpleName(), e.getValue().getMessage()))
                .collect(Collectors.joining("  \n", "Dependent Errors: \n", "")));

    }
}
