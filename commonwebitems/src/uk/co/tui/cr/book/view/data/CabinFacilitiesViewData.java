/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.List;
import java.util.Map;

/**
 * The Class CabinFacilitiesViewData.
 *
 * @author ramkishore.p
 */
public class CabinFacilitiesViewData {

    /** The name. */
    private String name;

    /** The code. */
    private String code;

    /** The type code. */
    private String typeCode;

    /** The rooms list. */
    private List<String> roomsList;

    /** The usps. */
    private Map<String, List<String>> usps;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code.
     *
     * @param code
     *            the new code
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * Gets the type code.
     *
     * @return the type code
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * Sets the type code.
     *
     * @param typeCode
     *            the new type code
     */
    public void setTypeCode(final String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * Gets the rooms list.
     *
     * @return the rooms list
     */
    public List<String> getRoomsList() {
        return roomsList;
    }

    /**
     * Sets the rooms list.
     *
     * @param roomsList
     *            the new rooms list
     */
    public void setRoomsList(final List<String> roomsList) {
        this.roomsList = roomsList;
    }

    /**
     * Gets the usps.
     *
     * @return the usps
     */
    public Map<String, List<String>> getUsps() {
        return usps;
    }

    /**
     * Sets the usps.
     *
     * @param usps
     *            the usps to set
     */
    public void setUsps(final Map<String, List<String>> usps) {
        this.usps = usps;
    }

}
