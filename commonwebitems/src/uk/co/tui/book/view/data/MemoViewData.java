/**
 *
 */
package uk.co.tui.book.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author siddharam.r
 *
 */
public class MemoViewData {

    /**
     * Represents the availability of memo.
     */
    private boolean available;

    /**
     * Represents the description of memo.
     */
    private List<String> description=new ArrayList<String>();

    /**
     * @return the available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * @param available
     *
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * @return the description
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(List<String> description) {
        this.description = description;
    }


}
