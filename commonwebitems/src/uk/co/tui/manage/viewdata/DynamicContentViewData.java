/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;


/**
 * @author veena.pn
 *
 */
public class DynamicContentViewData
{


    private Map<Object, Object> contentMap = new HashMap<Object, Object>();

    /**
     * @return the contentMap
     */
    public Map<Object, Object> getContentMap()
    {
        if (MapUtils.isEmpty(contentMap))
        {
            contentMap = new HashMap<Object, Object>();
        }
        return contentMap;
    }

    /**
     * @param contentMap
     *           the contentMap to set
     */
    public void setContentMap(final Map<Object, Object> contentMap)
    {
        this.contentMap = contentMap;
    }


}
