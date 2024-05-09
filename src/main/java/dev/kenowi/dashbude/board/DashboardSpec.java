package dev.kenowi.dashbude.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardSpec {
    
    private String title;

    /**
     * The selector use, when looking for Dashboard Items.
     */
    private String selectorName;
}
