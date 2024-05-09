package dev.kenowi.dashbude.app;

import dev.kenowi.dashbude.shared.DashboardItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppSpec extends DashboardItem {
    private String name;
    private String url;
    private String icon;
}
