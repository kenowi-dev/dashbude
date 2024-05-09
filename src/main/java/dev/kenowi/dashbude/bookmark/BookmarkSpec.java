package dev.kenowi.dashbude.bookmark;


import dev.kenowi.dashbude.shared.DashboardItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkSpec extends DashboardItem {
    private String category;
    private String name;
    private String url;
}
