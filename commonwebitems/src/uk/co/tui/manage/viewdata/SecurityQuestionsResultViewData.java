/**
 *
 */
package uk.co.tui.manage.viewdata;

/**
 * @author chethanram.bv
 *
 */
public class SecurityQuestionsResultViewData
{

    private PassengerAddressViewData address;
    private ErrorViewData error;
    /**
     * @return the error
     */
    public ErrorViewData getError()
    {
        return error;
    }
    /**
     * @param error the error to set
     */
    public void setError(ErrorViewData error)
    {
        this.error = error;
    }
    /**
     * @return the address
     */
    public PassengerAddressViewData getAddress()
    {
        return address;
    }
    /**
     * @param address the address to set
     */
    public void setAddress(PassengerAddressViewData address)
    {
        this.address = address;
    }

}
