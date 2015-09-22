/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.List;

/**
 * @author srikanth.bs
 * 
 */
public class WebAnalyticsData {

    /** Room Selection for web analytics */
    private List<CabinSltViewData> packageModelCabins;

    private List<RmSltViewData> packageModelRooms;

    /** Used for alternative flight options data capturing . */
    private WebAnalyticsALTFLTData webAnalyticsALTFLTData;

    private String limAvS;

    /**
     * @return the packageModelCabins
     */
    public List<CabinSltViewData> getPackageModelCabins() {
        return packageModelCabins;
    }

    /**
     * @param packageModelCabins
     *            the packageModelCabins to set
     */
    public void setPackageModelCabins(
            final List<CabinSltViewData> packageModelCabins) {
        this.packageModelCabins = packageModelCabins;
    }

    /**
     * @return the packageModelRooms
     */
    public List<RmSltViewData> getPackageModelRooms() {
        return packageModelRooms;
    }

    /**
     * @param packageModelRooms
     *            the packageModelRooms to set
     */
    public void setPackageModelRooms(final List<RmSltViewData> packageModelRooms) {
        this.packageModelRooms = packageModelRooms;
    }

    /**
     * @return the webAnalyticsALTFLTData
     */
    public WebAnalyticsALTFLTData getWebAnalyticsALTFLTData() {
        return webAnalyticsALTFLTData;
    }

    /**
     * @param webAnalyticsALTFLTData
     *            the webAnalyticsALTFLTData to set
     */
    public void setWebAnalyticsALTFLTData(
            final WebAnalyticsALTFLTData webAnalyticsALTFLTData) {
        this.webAnalyticsALTFLTData = webAnalyticsALTFLTData;
    }

    /**
     * @return the limAvS
     */
    public String getLimAvS() {
        return limAvS;
    }

    /**
     * @param limAvS
     *            the limAvS to set
     */
    public void setLimAvS(final String limAvS) {
        this.limAvS = limAvS;
    }

}
