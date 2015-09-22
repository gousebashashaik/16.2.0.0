/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Class DeckPlanOverlayViewData.
 *
 * @author ramkishore.p
 */
public class DeckPlanViewData {

    /** The ship code. */
    private String shipCode;

    /** The deck no. */
    private int deckNo;

    /** The svg image url. */
    private String svgImageUrl;

    /** The gif image url. */
    private String gifImageUrl;

    /** The facilities. */
    private List<String> facilities;

    /** The cabin type map. */
    private Map<String, List<String>> cabinTypeMap;

    /** The cabin facilities view data. */
    private final List<CabinFacilitiesViewData> cabinFacilitiesViewData = new ArrayList<CabinFacilitiesViewData>();

    /**
     * Gets the ship code.
     *
     * @return the ship code
     */
    public String getShipCode() {
        return shipCode;
    }

    /**
     * Sets the ship code.
     *
     * @param shipCode
     *            the new ship code
     */
    public void setShipCode(final String shipCode) {
        this.shipCode = shipCode;
    }

    /**
     * Gets the deck no.
     *
     * @return the deck no
     */
    public int getDeckNo() {
        return deckNo;
    }

    /**
     * Sets the deck no.
     *
     * @param deckNo
     *            the new deck no
     */
    public void setDeckNo(final int deckNo) {
        this.deckNo = deckNo;
    }

    /**
     * Gets the svg image url.
     *
     * @return the svg image url
     */
    public String getSvgImageUrl() {
        return svgImageUrl;
    }

    /**
     * Sets the svg image url.
     *
     * @param svgImageUrl
     *            the new svg image url
     */
    public void setSvgImageUrl(final String svgImageUrl) {
        this.svgImageUrl = svgImageUrl;
    }

    /**
     * Gets the gif image url.
     *
     * @return the gif image url
     */
    public String getGifImageUrl() {
        return gifImageUrl;
    }

    /**
     * Sets the gif image url.
     *
     * @param gifImageUrl
     *            the new gif image url
     */
    public void setGifImageUrl(final String gifImageUrl) {
        this.gifImageUrl = gifImageUrl;
    }

    /**
     * Gets the facilities.
     *
     * @return the facilities
     */
    public List<String> getFacilities() {
        return facilities;
    }

    /**
     * Sets the facilities.
     *
     * @param facilities
     *            the new facilities
     */
    public void setFacilities(final List<String> facilities) {
        this.facilities = facilities;
    }

    /**
     * Gets the cabin type map.
     *
     * @return the cabin type map
     */
    public Map<String, List<String>> getCabinTypeMap() {
        return cabinTypeMap;
    }

    /**
     * Sets the cabin type map.
     *
     * @param cabinTypeMap
     *            the cabin type map
     */
    public void setCabinTypeMap(final Map<String, List<String>> cabinTypeMap) {
        this.cabinTypeMap = cabinTypeMap;
    }

    /**
     * Gets the cabin facilities view data.
     *
     * @return the cabin facilities view data
     */
    public List<CabinFacilitiesViewData> getCabinFacilitiesViewData() {
        return cabinFacilitiesViewData;
    }

}
