/**
 *
 */
package uk.co.portaltech.tui.web.view.data;


public class BoardBasisType {

    private String boardbasisCode;
    private String name;
    private String totalPrice;
    private boolean defaultBoardBasis;
    private String priceDiffrence;
    private String totalPricePP;
    /* Added for accommodation board type price value */
    private String accomdboardpriceDiffrence;
    /* End Added for accommodation board type price value */


    /**
     * @return the defaultBoardBasis
     */
    public boolean isDefaultBoardBasis() {
        return defaultBoardBasis;
    }
    /**
     * @return the accomdboardpriceDiffrence
     */
    public String getAccomdboardpriceDiffrence() {
        return accomdboardpriceDiffrence;
    }
    /**
     * @param accomdboardpriceDiffrence the accomdboardpriceDiffrence to set
     */
    public void setAccomdboardpriceDiffrence(String accomdboardpriceDiffrence) {
        this.accomdboardpriceDiffrence = accomdboardpriceDiffrence;
    }
    /**
     * @param defaultBoardBasis the defaultBoardBasis to set
     */
    public void setDefaultBoardBasis(boolean defaultBoardBasis) {
        this.defaultBoardBasis = defaultBoardBasis;
    }
    /**
     * @return the totalPricePP
     */
    public String getTotalPricePP() {
        return totalPricePP;
    }
    /**
     * @param totalPricePP the totalPricePP to set
     */
    public void setTotalPricePP(String totalPricePP) {
        this.totalPricePP = totalPricePP;
    }
    /**
     * @return the boardbasisCode
     */
    public String getBoardbasisCode() {
        return boardbasisCode;
    }
    /**
     * @param boardbasisCode the boardbasisCode to set
     */
    public void setBoardbasisCode(String boardbasisCode) {
        this.boardbasisCode = boardbasisCode;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the totalPrice
     */
    public String getTotalPrice() {
        return totalPrice;
    }
    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
    /**
     * @return the isDefaultBoardBasis
     */

    /**
     * @return the priceDiffrence
     */
    public String getPriceDiffrence() {
        return priceDiffrence;
    }
    /**
     * @param priceDiffrence the priceDiffrence to set
     */
    public void setPriceDiffrence(String priceDiffrence) {
        this.priceDiffrence = priceDiffrence;
    }

}
