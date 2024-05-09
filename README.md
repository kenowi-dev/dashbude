# dashbude

Kubernetes operator for an adaptable dashboard. CRDs can be used to add Items to the dashboard.

Uses the [grieded/sui](https://hub.docker.com/r/griefed/sui) docker image as the dashboard, which uses the original [jeroenpardon/sui](https://github.com/jeroenpardon/sui) dashboard.

## CRDs

### Dashboard

The dashboard deployment with service.

### App

The apps displayed on the dashboard.

### Bookmark

 The bookmarks on the dashboard.

## Install
The application is not yet production ready. Installation details will follow, when it is ready.

## Running the application in dev mode

You can run your application in dev mode:

```shell script
./mvnw compile quarkus:dev
```

This will use your configured `kubectl` configuration to apply the crds and start the operator. 
