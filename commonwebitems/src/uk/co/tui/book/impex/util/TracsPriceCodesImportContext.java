/**
 *
 */
package uk.co.tui.book.impex.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * The Class TracsPriceCodeImportContext.
 *
 * @author amaresh.d
 */
public class TracsPriceCodesImportContext
{

    /** The log. */

    private static final TUILogUtils LOGGER = new TUILogUtils(
            "TracsPriceCodesImportContext");

    /** The Constant CVS_COL_ONE. */
    private static final int CVS_COL_ONE = 1;

    /** The Constant CVS_COL_TWO. */
    private static final int CVS_COL_TWO = 2;

    /** The Constant CVS_COL_THREE. */
    private static final int CVS_COL_THREE = 3;

    /** The Constant KEY_ELEVEN. */
    private static final int KEY_ELEVEN = 11;

    /** The Constant KEY_TWELVE. */
    private static final int KEY_TWELVE = 12;

    /** The Constant KEY_THIRTEEN. */
    private static final int KEY_THIRTEEN = 13;

    /** The Constant KEY_THIRTEEN. */
    private static final int KEY_TWENTYONE = 21;

    /** The Constant TRACSEXTRAS **/
    private static final String TRACSPRICECODE ="priceCodes";

    /** The Constant UNDERSCORE **/
    private static final String UNDERSCORE = "_";


    /**
     * Instantiates a new tracs price code import context.
     */
    public TracsPriceCodesImportContext()
    {
    }


    /**
     * Prepare tracs price codes.
     *
     * @param line the line
     */
    public void prepareTracsPriceCodes(final Map<Integer, String> line) {
        final String priceCode = StringUtils.trimToEmpty(getValue(line,
                CVS_COL_TWO));

        final Map<String, String> duplicateFinder = new HashMap<String, String>();
        if(duplicateFinder.containsKey(priceCode))
        {
            LOGGER.error("*******************************************");
            LOGGER.error("******Found the duplicate key while importing inventory price codes:::"+ priceCode);
            LOGGER.error("*******************************************");
        }
        else
        {
            duplicateFinder.put(priceCode, priceCode);
        }

        if (StringUtil.notEmptyAndNotBlank(priceCode))
        {
                LOGGER.debug("Adding TRACS PricingCodes with code " + priceCode);
            line.put(getKey(KEY_ELEVEN), priceCode);
            line.put(getKey(KEY_TWELVE), getValue(line, CVS_COL_ONE));
            line.put(getKey(KEY_THIRTEEN), getValue(line, CVS_COL_THREE));
            line.put(getKey(KEY_TWENTYONE), getUniqueCategoryCode(priceCode));
        } else {
            LOGGER.error(">> Data missing for : " + priceCode);
            line.clear();
        }
    }

    /**
     * This method would construct unique category code
     * @param priceCode
     * @return String
     */
    private String getUniqueCategoryCode(final String priceCode) {
        return StringUtil.append(TRACSPRICECODE, UNDERSCORE, priceCode);
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
    private String getValue(final Map<Integer, String> line, final int index) {
        return line.get(Integer.valueOf(index));
    }

    /**
     * Gets the key.
     *
     * @param index
     *            the index
     * @return the key
     */
    private Integer getKey(final int index) {
        return Integer.valueOf(index);
    }
}