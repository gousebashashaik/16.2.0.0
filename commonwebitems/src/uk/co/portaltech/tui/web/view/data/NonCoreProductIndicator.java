/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

public class NonCoreProductIndicator {

    private boolean singleStay;
    private boolean multipleStay;
    private boolean multiStay;

    /**
     * @return the singleStay
     */
    public boolean isSingleStay() {
        return singleStay;
    }

    /**
     * @param singleStay
     *            the singleStay to set
     */
    public void setSingleStay(boolean singleStay) {
        this.singleStay = singleStay;
    }

    /**
     * @return the multiStay
     */
    public boolean isMultiStay() {
        return multiStay;
    }

    /**
     * @param multiStay
     *            the multiStay to set
     */
    public void setMultiStay(boolean multiStay) {
        this.multiStay = multiStay;
    }

    /**
     * @return the multipleStay
     */
    public boolean isMultipleStay() {
        return multipleStay;
    }

    /**
     * @param multipleStay
     *            the multipleStay to set
     */
    public void setMultipleStay(boolean multipleStay) {
        this.multipleStay = multipleStay;
    }

}
