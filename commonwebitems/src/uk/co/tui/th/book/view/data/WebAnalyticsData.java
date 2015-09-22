/**
 *
 */
package uk.co.tui.th.book.view.data;

import java.util.List;

/**
 * @author srikanth.bs
 *
 */
public class WebAnalyticsData {

       /** Room Selection for web analytics */
        private List<RmSltViewData> packageModelRooms;

        /** Used for alternative flight options data capturing . */
        private WebAnalyticsALTFLTData webAnalyticsALTFLTData;

        private String limAvS;

        /**
         * @return the packageModelRooms
         */
        public List<RmSltViewData> getPackageModelRooms() {
            return packageModelRooms;
        }

        /**
         * @param packageModelRooms the packageModelRooms to set
         */
        public void setPackageModelRooms(List<RmSltViewData> packageModelRooms) {
            this.packageModelRooms = packageModelRooms;
        }

        /**
         * @return the webAnalyticsALTFLTData
         */
        public WebAnalyticsALTFLTData getWebAnalyticsALTFLTData() {
            return webAnalyticsALTFLTData;
        }

        /**
         * @param webAnalyticsALTFLTData the webAnalyticsALTFLTData to set
         */
        public void setWebAnalyticsALTFLTData(
                WebAnalyticsALTFLTData webAnalyticsALTFLTData) {
            this.webAnalyticsALTFLTData = webAnalyticsALTFLTData;
        }

        /**
         * @return the limAvS
         */
        public String getLimAvS() {
            return limAvS;
        }

        /**
         * @param limAvS the limAvS to set
         */
        public void setLimAvS(String limAvS) {
            this.limAvS = limAvS;
        }

}

