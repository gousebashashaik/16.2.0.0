/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author deepakkumar.k
 *
 */
public class AlternateBoardBasisPriceViewData {



    private String boardbasisCode;
    private String name;
    private boolean isDefaultBoardBasis;
    private String packagePrice;
    private String total;
    private String subTotal;
    private CreditCardCharge creditCardData;
    private String totalPricePP;
    /* Added for accommodation board type price value */
    private String accomdboardpriceDiffrence;
    /* End Added for accommodation board type price value */





    /**
     * @return the totalPricePP
     */
    public String getTotalPricePP() {
        return totalPricePP;
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
     * @return the isDefaultBoardBasis
     */
    public boolean isDefaultBoardBasis() {
        return isDefaultBoardBasis;
    }
    /**
     * @param isDefaultBoardBasis the isDefaultBoardBasis to set
     */
    public void setDefaultBoardBasis(boolean isDefaultBoardBasis) {
        this.isDefaultBoardBasis = isDefaultBoardBasis;
    }
    /**
     * @return the packagePrice
     */
    public String getPackagePrice() {
        return packagePrice;
    }
    /**
     * @param packagePrice the packagePrice to set
     */
    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }
    /**
     * @return the total
     */
    public String getTotal() {
        return total;
    }
    /**
     * @param total the total to set
     */
    public void setTotal(String total) {
        this.total = total;
    }
    /**
     * @return the subTotal
     */
    public String getSubTotal() {
        return subTotal;
    }
    /**
     * @param subTotal the subTotal to set
     */
    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }
    /**
     * @return the creditCardData
     */
    public CreditCardCharge getCreditCardData() {
        return creditCardData;
    }
    /**
     * @param creditCardData the creditCardData to set
     */
    public void setCreditCardData(CreditCardCharge creditCardData) {
        this.creditCardData = creditCardData;
    }













}
