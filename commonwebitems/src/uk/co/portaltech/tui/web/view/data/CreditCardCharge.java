/**
 * bean file for credit card charges
 */
package uk.co.portaltech.tui.web.view.data;


public class CreditCardCharge {
    private String creditCardPercentage;
    private String deposit;
    private String total;
    /**
     * @return the creditCardPercentage
     */
    public String getCreditCardPercentage() {
        return creditCardPercentage;
    }
    /**
     * @param creditCardPercentage the creditCardPercentage to set
     */
    public void setCreditCardPercentage(String creditCardPercentage) {
        this.creditCardPercentage = creditCardPercentage;
    }
    /**
     * @return the deposit
     */
    public String getDeposit() {
        return deposit;
    }
    /**
     * @param deposit the deposit to set
     */
    public void setDeposit(String deposit) {
        this.deposit = deposit;
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

}
