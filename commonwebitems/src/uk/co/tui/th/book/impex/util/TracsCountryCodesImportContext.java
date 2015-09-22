/**
 *
 */
package uk.co.tui.th.book.impex.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author raghavendra.dm
 */
public class TracsCountryCodesImportContext
{


    /** The log. */
    private static final TUILogUtils LOGGER = new TUILogUtils("TracsCountryCodesImportContext");

    /** The Constant CVS_COL_ONE. */
    private static final int CVS_COL_ONE = 1;

    /** The Constant CVS_COL_TWO. */
    private static final int CVS_COL_TWO = 2;

    /** The Constant CVS_COL_THREE. */
    private static final int CVS_COL_THREE = 3;

    /** The Constant CVS_COL_FOUR. */
    private static final int CVS_COL_FOUR = 4;

    /** The Constant KEY_ELEVEN. */
    private static final int KEY_ELEVEN = 11;

    /** The Constant KEY_TWELVE. */
    private static final int KEY_TWELVE = 12;

    /** The Constant KEY_THIRTEEN. */
    private static final int KEY_THIRTEEN = 13;

    /** The Constant KEY_FOURTEEN. */
    private static final int KEY_FOURTEEN = 14;

    /**
     * Instantiates a new tracs Country code import context.
     */
    public TracsCountryCodesImportContext()
    {
    }


    /**
     * Prepare tracs country codes.
     *
     * @param line
     *            the line
     */
    public void prepareTracsCountryCodes(final Map<Integer, String> line)
    {
        final String countryCode = StringUtils.trimToEmpty(getValue(line, CVS_COL_TWO));

        final Map<String, String> duplicateFinder = new HashMap<String, String>();
        if (duplicateFinder.containsKey(countryCode))
        {
            LOGGER.error("*******************************************");
            LOGGER.error("******Found the duplicate key while importing ISO country codes:::" + countryCode);
            LOGGER.error("*******************************************");
        }
        else
        {
            duplicateFinder.put(countryCode, countryCode);
        }

        if (StringUtil.notEmptyAndNotBlank(countryCode))
        {
            LOGGER.debug("Adding TRACS CountryCodes with code " + countryCode);

            line.put(getKey(KEY_FOURTEEN), getValue(line, CVS_COL_FOUR));
            line.put(getKey(KEY_TWELVE), countryCode);
            line.put(getKey(KEY_THIRTEEN), getValue(line, CVS_COL_THREE));
            line.put(getKey(KEY_ELEVEN), getValue(line, CVS_COL_ONE));
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
