/**
 *
 */
package uk.co.tui.book.impex.util;

import java.util.HashMap;
import java.util.Map;

import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * The Class TracsExtrasMasterImportContext.
 *
 * @author amaresh.d
 */
public class TracsExtrasMasterImportContext {

    /** The log. */
    // private static final Logger LOGGER=

    private static final TUILogUtils LOGGER = new TUILogUtils(
            "TracsExtrasMasterImportContext");

    /** The Constant CVS_COL_ONE. */
    private static final int CVS_COL_ONE = 1;

    /** The Constant CVS_COL_TWO. */
    private static final int CVS_COL_TWO = 2;

    /** The Constant CVS_COL_THREE. */
    private static final int CVS_COL_THREE = 3;

    /** The Constant CVS_COL_FOUR. */
    private static final int CVS_COL_FOUR = 4;

    /** The Constant CVS_COL_FIVE. */
    private static final int CVS_COL_FIVE = 5;

    /** The Constant CVS_COL_SIX. */
    private static final int CVS_COL_SIX = 6;

    /** The Constant CVS_COL_SEVEN. */
    private static final int CVS_COL_SEVEN = 7;

    /** The Constant CVS_COL_EIGHT. */
    private static final int CVS_COL_EIGHT = 8;

    /** The Constant CVS_COL_EIGHT. */
    private static final int CVS_COL_NINE = 9;

    /** The Constant CVS_COL_EIGHT. */
    private static final int CVS_COL_TEN = 10;

    /** The Constant CVS_COL_ELEVEN. */
    private static final int CVS_COL_ELEVEN = 11;

    /** The Constant CVS_COL_TWELVE. */
    private static final int CVS_COL_TWELVE = 12;

    /** The Constant KEY_FIFTEEN. */
    private static final int KEY_FIFTEEN = 15;

    /** The Constant KEY_SIXTEEN. */
    private static final int KEY_SIXTEEN = 16;

    /** The Constant KEY_SEVENTEEN. */
    private static final int KEY_SEVENTEEN = 17;

    /** The Constant KEY_EIGHTEEN. */
    private static final int KEY_EIGHTEEN = 18;

    /** The Constant KEY_NINETEEN. */
    private static final int KEY_NINETEEN = 19;

    private static final int KEY_TWENTY = 20;

    /** The Constant KEY_TWENTYONE. */
    private static final int KEY_TWENTYONE = 21;

    /** The Constant KEY_TWENTYTWO. */
    private static final int KEY_TWENTYTWO = 22;

    /** The Constant KEY_TWENTYTWO. */
    private static final int KEY_TWENTYTHREE = 23;

    /** The Constant KEY_TWENTYFOUR. */
    private static final int KEY_TWENTYFOUR = 24;

    /** The Constant KEY_TWENTYFIVE. */
    private static final int KEY_TWENTYFIVE = 25;

    /** The Constant KEY_TWENTYSIX. */
    private static final int KEY_TWENTYSIX = 26;

    /** The Constant KEY_TWENTYSEVEN. */
    private static final int KEY_TWENTYSEVEN = 27;

    /** The Constant TRACSEXTRAS **/
    private static final String TRACSEXTRAS = "tracsExtras";

    /** The Constant UNDERSCORE **/
    private static final String UNDERSCORE = "_";
    /**
     * Instantiates a new tracs extras master import context.
     */
    public TracsExtrasMasterImportContext() {

    }

    /**
     * This method creates MediaContainer for a TouristBoard
     *
     * @param line
     */
    public void prepareTracsExtrasMaster(final Map<Integer, String> line) {

        final String extraItemCode = getValue(line, CVS_COL_ONE);

        final Map<String, String> duplicateFinder = new HashMap<String, String>();

        if (duplicateFinder.containsKey(extraItemCode)) {
            LOGGER.error("*********************************************************************");
            LOGGER.error("******Found the duplicate key while importing inventory master data:::"
                    + extraItemCode);
            LOGGER.error("**********************************************************************");
        } else {
            duplicateFinder.put(extraItemCode, extraItemCode);
        }

        if (StringUtil.notEmptyAndNotBlank(extraItemCode)) {
            LOGGER.debug("Adding TRACS extra master with code " + extraItemCode);
            line.put(getKey(KEY_FIFTEEN), extraItemCode);
            line.put(getKey(KEY_SIXTEEN), getValue(line, CVS_COL_TWO));
            line.put(getKey(KEY_SEVENTEEN), getValue(line, CVS_COL_THREE));

            line.put(getKey(KEY_EIGHTEEN), getValue(line, CVS_COL_FOUR));
            line.put(getKey(KEY_NINETEEN), getValue(line, CVS_COL_FIVE));
            line.put(getKey(KEY_TWENTY), getValue(line, CVS_COL_SIX));
            line.put(getKey(KEY_TWENTYTWO), getValue(line, CVS_COL_SEVEN));
            line.put(getKey(KEY_TWENTYTHREE), getValue(line, CVS_COL_EIGHT));
            line.put(getKey(KEY_TWENTYONE),
                    getUniqueCategoryCode(extraItemCode));
            line.put(getKey(KEY_TWENTYFOUR), getValue(line, CVS_COL_NINE));
            line.put(getKey(KEY_TWENTYFIVE), getValue(line, CVS_COL_TEN));
            line.put(getKey(KEY_TWENTYSIX), getValue(line, CVS_COL_ELEVEN));
            line.put(getKey(KEY_TWENTYSEVEN), getValue(line, CVS_COL_TWELVE));
        } else {
            LOGGER.error(">> Data missing for : " + extraItemCode);
            line.clear();
        }
    }
    /**
     * This method would construct unique category code
     *
     * @param extraItemCode
     * @return String
     */
    private String getUniqueCategoryCode(final String extraItemCode) {
        return StringUtil.append(TRACSEXTRAS, UNDERSCORE, extraItemCode);
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
