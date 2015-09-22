/**
 *
 */
package uk.co.tui.th.book.impex.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * The Class StaticContentImportContext.
 *
 * @author thyagaraju.e
 */
public class StaticContentImportContext
{

    /** The log. */
    private static final TUILogUtils LOGGER = new TUILogUtils("StaticContentImportContext");

    /** The Constant CVS_COL_ONE. */
    private static final int CVS_COL_ONE_UNIQUE_ID = 1;

    /** The Constant CVS_COL_TWO. */
    private static final int CVS_COL_TWO_TYPE = 2;

    /** The Constant CVS_COL_THREE. */
    private static final int CVS_COL_THREE_COMPONENT_DESC = 3;

    /** The Constant CVS_COL_FOUR. */
    private static final int CVS_COL_FOUR_VALUE = 4;

    /** The Constant CVS_COL_FIVE. */
    private static final int CVS_COL_FIVE_COMPONENT_ID = 5;

    /** The Constant KEY_SIX_CODE. */
    private static final int KEY_SIX_CODE = 6;

    /** The Constant KEY_SEVEN_CONTENT_ID. */
    private static final int KEY_SEVEN_CONTENT_ID = 7;

    /** The Constant KEY_EIGHT_TYPE. */
    private static final int KEY_EIGHT_TYPE = 8;

    /** The Constant KEY_NINE_COMP_DESC. */
    private static final int KEY_NINE_COMP_DESC = 9;

    /** The Constant KEY_TEN_VALUE. */
    private static final int KEY_TEN_VALUE = 10;

    /** The Constant KEY_TEN_COMP_ID. */
    private static final int KEY_TEN_COMP_ID = 11;

    /**
     * Instantiates a new static page content import context.
     */
    public StaticContentImportContext()
    {

    }

    /**
     * This method prepares Static Content Data.
     *
     * @param line
     *            the line
     */
    public void prepareStaticContentData(final Map<Integer, String> line)
    {

        final String uniqueId = getValue(line, CVS_COL_ONE_UNIQUE_ID);
        final String value = getValue(line, CVS_COL_FOUR_VALUE);
        final Map<String, String> duplicateFinder = new HashMap<String, String>();

        checkForDuplicateKey(uniqueId, duplicateFinder);

        if (StringUtil.notEmptyAndNotBlank(uniqueId) && StringUtil.notEmptyAndNotBlank(value))
        {

            LOGGER.debug("Adding static content unique Id " + uniqueId);

            line.put(getKey(KEY_SIX_CODE), uniqueId);
            line.put(getKey(KEY_SEVEN_CONTENT_ID), uniqueId);
            line.put(getKey(KEY_EIGHT_TYPE), getValue(line, CVS_COL_TWO_TYPE));
            line.put(getKey(KEY_NINE_COMP_DESC), getValue(line, CVS_COL_THREE_COMPONENT_DESC));
            line.put(getKey(KEY_TEN_VALUE), StringEscapeUtils.unescapeXml(StringEscapeUtils.unescapeHtml(value)));
            line.put(getKey(KEY_TEN_COMP_ID), getValue(line, CVS_COL_FIVE_COMPONENT_ID));
        }
        else
        {
            LOGGER.error(">> Data missing for : " + uniqueId);
            line.clear();
        }
    }

    /**
     * Check for duplicate key.
     *
     * @param uniqueId
     *            the unique id
     * @param duplicateFinder
     *            the duplicate finder
     */
    private void checkForDuplicateKey(final String uniqueId, final Map<String, String> duplicateFinder)
    {
        if (duplicateFinder.containsKey(uniqueId))
        {
            LOGGER.error("*********************************************************************");
            LOGGER.error("******Found the duplicate key while importing static Content data:::" + uniqueId);
            LOGGER.error("**********************************************************************");
        }
        else
        {
            duplicateFinder.put(uniqueId, uniqueId);
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
