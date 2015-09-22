/**
 *
 */
package uk.co.tui.book.view.data;

import java.math.BigDecimal;

/**
 * @author srikanth.bs
 *
 */
public class RmSltViewData {

        /** Holds room code*/
        private String roomCode;
        /** Holds room Description */
        private String roomDescription;
        /** Holds room price */
        private BigDecimal roomPrice;
        /** Holds room selling Code i.e. accom code */
        private String sellingCode;

        /**
         * @return the roomCode
         */
        public String getRoomCode() {
            return roomCode;
        }

        /**
         * @param roomCode the roomCode to set
         */
        public void setRoomCode(String roomCode) {
            this.roomCode = roomCode;
        }

        /**
         * @return the roomDescription
         */
        public String getRoomDescription() {
            return roomDescription;
        }

        /**
         * @param roomDescription the roomDescription to set
         */
        public void setRoomDescription(String roomDescription) {
            this.roomDescription = roomDescription;
        }

        /**
         * @return the roomPrice
         */
        public BigDecimal getRoomPrice() {
            return roomPrice;
        }

        /**
         * @param roomPrice the roomPrice to set
         */
        public void setRoomPrice(BigDecimal roomPrice) {
            this.roomPrice = roomPrice;
        }

        /**
         * @return the sellingCode
         */
        public String getSellingCode() {
            return sellingCode;
        }

        /**
         * @param sellingCode the sellingCode to set
         */
        public void setSellingCode(String sellingCode) {
            this.sellingCode = sellingCode;
        }

}
