/**
 *
 */
package uk.co.tui.book.impex.util;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author pradeep.as
 *
 */
public class DynamicContentConfigImportContext
{


    /** The log. */

    private static final TUILogUtils LOGGER = new TUILogUtils("DynamicContentConfigImportContext");

    /** The Constant CVS_COL_ONE. */
    private static final int CVS_COL_ONE_EPIC = 1;

    /** The Constant CVS_COL_TWO. */
    private static final int CVS_COL_TWO_CODE = 2;

    /** The Constant CVS_COL_THREE. */
    private static final int CVS_COL_THREE_IMAGE_QUALIFIER = 3;

    /** The Constant CVS_COL_FOUR_ALIASS. */
    private static final int CVS_COL_FOUR_ALIASS = 4;

    /** The Constant KEY_TWELVE. */
    private static final int KEY_TWELVE_EPIC_CODE = 12;

    /** The Constant KEY_THIRTEEN. */
    private static final int KEY_THIRTEEN_CODE_TYPE = 13;

    /** The Constant KEY_FOURTEEN. */
    private static final int KEY_FOURTEEN_IMAGE_QUALIFIER = 14;

    /** The Constant KEY_FIFTEEN_ALIASS. */
    private static final int KEY_FIFTEEN_ALIASS = 15;

    public DynamicContentConfigImportContext()
    {

    }


    /**
     * @param line
     */
    public void prepareDynamicContentConfig(final Map<Integer, String> line)
    {

        final String epicCode = getValue(line, CVS_COL_ONE_EPIC);
        final String extraCodeOrType = getValue(line, CVS_COL_TWO_CODE);
        if (StringUtil.notEmptyAndNotBlank(epicCode) && StringUtil.notEmptyAndNotBlank(extraCodeOrType))
        {
            line.put(getKey(KEY_TWELVE_EPIC_CODE), epicCode);
            line.put(getKey(KEY_THIRTEEN_CODE_TYPE), extraCodeOrType);
            line.put(getKey(KEY_FOURTEEN_IMAGE_QUALIFIER), getValue(line, CVS_COL_THREE_IMAGE_QUALIFIER));
            line.put(getKey(KEY_FIFTEEN_ALIASS), StringUtils.defaultIfEmpty(getValue(line, CVS_COL_FOUR_ALIASS), "Alias_Not_found"));
        }
        else
        {
            LOGGER.error(">> Data missing for : epic Code " + epicCode + "extraCodeOrType" + extraCodeOrType);
            line.clear();
        }

    }


    /**
     * Gets the value.
     *
     * @param line
     *            the line
     * @param index
     *            the index
     * @return the value
     */
    private String getValue(final Map<Integer, String> line, final int index)
    {
        return line.get(Integer.valueOf(index));
    }

    /**
     * Gets the key.
     *
     * @param index
     *            the index
     * @return the key
     */
    private Integer getKey(final int index)
    {
        return Integer.valueOf(index);
    }

}
