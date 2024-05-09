package dev.kenowi.dashbude.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DashboardItem {
    public static final String DASHBOARD_SELECTOR_FIELD = "dashboardSelector";
    private String dashboardSelector;
}
