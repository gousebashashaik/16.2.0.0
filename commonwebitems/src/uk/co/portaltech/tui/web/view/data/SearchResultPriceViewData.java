package uk.co.portaltech.tui.web.view.data;

/**
 *
 */
public class SearchResultPriceViewData {

    /**
     *
     */
    private String perPerson;
    /**
     *
     */
    private String totalParty;
    /**
     *
     */
    private String discount;

    private String discountPP;
    /**
     *
     */
    private String promotionalOffer;
    /**
     *
     */
    private String depositAmount;

    private String depositAmountPP;


    private String lowDeposit;

    private boolean lowDepositExists;

    private boolean depositExists;

    private String childPrice;

    /**
     * @return the perPerson
     */
    public String getPerPerson() {
        return perPerson;
    }
    /**
     * @return the totalParty
     */
    public String getTotalParty() {
        return totalParty;
    }
    /**
     * @return the discount
     */
    public String getDiscount() {
        return discount;
    }
    /**
     * @return the promotionalOffer
     */
    public String getPromotionalOffer() {
        return promotionalOffer;
    }
    /**
     * @return the depositAmount
     */
    public String getDepositAmount() {
        return depositAmount;
    }
    /**
     * @param perPerson the perPerson to set
     */
    public void setPerPerson(String perPerson) {
        this.perPerson = perPerson;
    }
    /**
     * @param totalParty the totalParty to set
     */
    public void setTotalParty(String totalParty) {
        this.totalParty = totalParty;
    }
    /**
     * @param discount the discount to set
     */
    public void setDiscount(String discount) {
        this.discount = discount;
    }
    /**
     * @param promotionalOffer the promotionalOffer to set
     */
    public void setPromotionalOffer(String promotionalOffer) {
        this.promotionalOffer = promotionalOffer;
    }
    /**
     * @param depositAmount the depositAmount to set
     */
    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    /**
     * @return the lowDeposit
     */
    public String getLowDeposit() {
        return lowDeposit;
    }
    /**
     * @param lowDeposit the lowDeposit to set
     */
    public void setLowDeposit(String lowDeposit) {
        this.lowDeposit = lowDeposit;
    }
    /**
     * @return the lowDepositExits
     */
    public boolean isLowDepositExists() {
        return lowDepositExists;
    }
    /**
     * @param lowDepositExists the lowDepositExists to set
     */
    public void setLowDepositExists(boolean lowDepositExists) {
        this.lowDepositExists = lowDepositExists;
    }
    /**
     * @return the depositAmountPP
     */
    public String getDepositAmountPP() {
        return depositAmountPP;
    }
    /**
     * @param depositAmountPP the depositAmountPP to set
     */
    public void setDepositAmountPP(String depositAmountPP) {
        this.depositAmountPP = depositAmountPP;
    }
    /**
     * @return the discountPP
     */
    public String getDiscountPP() {
        return discountPP;
    }
    /**
     * @param discountPP the discountPP to set
     */
    public void setDiscountPP(String discountPP) {
        this.discountPP = discountPP;
    }
    /**
     * @return the depositExists
     */
    public boolean isDepositExists() {
        return depositExists;
    }
    /**
     * @param depositExists the depositExists to set
     */
    public void setDepositExists(boolean depositExists) {
        this.depositExists = depositExists;
    }
    /**
     * @return the childPrice
     */
    public String getChildPrice() {
        return childPrice;
    }
    /**
     * @param childPrice the childPrice to set
     */
    public void setChildPrice(String childPrice) {
        this.childPrice = childPrice;
    }

}
