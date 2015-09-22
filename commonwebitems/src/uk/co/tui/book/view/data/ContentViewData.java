/**
 *
 */
package uk.co.tui.book.view.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * @author siddharam.r
 *
 */
public class ContentViewData implements Serializable {

    private Map<Object, Object> contentMap = new HashMap<Object, Object>();

    /**
     * @return the contentMap
     */
    public Map<Object, Object> getContentMap() {
        if (MapUtils.isEmpty(contentMap)) {
            contentMap = new HashMap<Object, Object>();
        }
        return contentMap;
    }
    /**
     * @param contentMap
     *            the contentMap to set
     */
    public void setContentMap(final Map<Object, Object> contentMap) {
        this.contentMap = contentMap;
    }

}
