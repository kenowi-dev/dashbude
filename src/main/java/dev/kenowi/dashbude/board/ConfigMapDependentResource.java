package dev.kenowi.dashbude.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.kenowi.dashbude.app.App;
import dev.kenowi.dashbude.app.AppSpec;
import dev.kenowi.dashbude.bookmark.Bookmark;
import dev.kenowi.dashbude.bookmark.BookmarkSpec;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.CustomResource;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@KubernetesDependent
public class ConfigMapDependentResource extends CRUDKubernetesDependentResource<ConfigMap, Dashboard> {

    private final ObjectWriter jsonWriter;

    public ConfigMapDependentResource() {
        super(ConfigMap.class);
        ObjectMapper objectMapper = new ObjectMapper();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultIndenter defaultIndenter = new DefaultIndenter("  ", "\n");
        prettyPrinter.indentArraysWith(defaultIndenter);
        prettyPrinter.indentObjectsWith(defaultIndenter);

        this.jsonWriter = objectMapper.writer(); //objectMapper.writer(prettyPrinter);
    }

    @Override
    protected ConfigMap desired(Dashboard dashboard, Context<Dashboard> context) {

        List<AppJson> apps = context
                .getClient()
                .resources(App.class)
                //.withField(DASHBOARD_SELECTOR_FIELD, dashboard.getSpec().getSelectorName())
                .list()
                .getItems()
                .stream()
                .filter(app -> dashboard.getSpec().getSelectorName().equals(app.getSpec().getDashboardSelector()))
                .map(CustomResource::getSpec)
                .map(AppJson::from)
                .toList();

        List<BookmarkJson> bookmarks = context
                .getClient()
                .resources(Bookmark.class)
                //.withField(DASHBOARD_SELECTOR_FIELD, dashboard.getSpec().getSelectorName())
                .list()
                .getItems()
                .stream()
                .filter(app -> dashboard.getSpec().getSelectorName().equals(app.getSpec().getDashboardSelector()))
                .map(CustomResource::getSpec)
                .collect(Collectors.groupingBy(BookmarkSpec::getCategory))
                .entrySet()
                .stream()
                .map(entry -> new BookmarkJson(entry.getKey(), entry.getValue()
                        .stream()
                        .map(LinkJson::from)
                        .toList()))
                .toList();



        AppsJson appsJson = new AppsJson(apps);
        BookmarksJson bookmarksJson = new BookmarksJson(bookmarks);

        String appsString;
        String bookmarksString;
        try {
            appsString = jsonWriter.writeValueAsString(appsJson);
            bookmarksString = jsonWriter.writeValueAsString(bookmarksJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("apps.json", appsString);
        data.put("links.json", bookmarksString);

        return new ConfigMapBuilder()
                .withMetadata(
                        new ObjectMetaBuilder()
                                .withName(dashboard.getMetadata().getName())
                                .withNamespace(dashboard.getMetadata().getNamespace())
                                .build())
                .withData(data)
                .build();
    }


    private record AppsJson(List<AppJson> apps) {}
    private record AppJson(String name, String url, String icon) {
        public static AppJson from(AppSpec spec) {
            return new AppJson(spec.getName(), spec.getUrl(), spec.getIcon());
        }
    }

    private record BookmarksJson(List<BookmarkJson> bookmarks) { }
    private record BookmarkJson(String category, List<LinkJson> links) {}
    private record LinkJson(String name, String url) {
        public static LinkJson from(BookmarkSpec spec) {
            return new LinkJson(spec.getName(), spec.getUrl());
        }
    }
}
