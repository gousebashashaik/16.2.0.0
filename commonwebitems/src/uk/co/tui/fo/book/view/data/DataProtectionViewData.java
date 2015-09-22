/**
 *
 */
package uk.co.tui.fo.book.view.data;

import java.util.HashMap;
import java.util.Map;


/**
 * @author sreevidhya.r
 *
 */
public class DataProtectionViewData
{
    private Map<Object, Object> dataProtectionMap = new HashMap<Object, Object>();

    /**
     * @return the dataProtectionMap
     */
    public Map<Object, Object> getDataProtectionMap()
    {
        return dataProtectionMap;
    }

    /**
     * @param dataProtectionMap
     *           the dataProtectionMap to set
     */
    public void setDataProtectionMap(final Map<Object, Object> dataProtectionMap)
    {
        this.dataProtectionMap = dataProtectionMap;
    }

}
