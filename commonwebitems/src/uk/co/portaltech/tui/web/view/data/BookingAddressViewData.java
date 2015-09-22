/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;


/**
 * @author veena.pn
 *
 */
public class BookingAddressViewData
{
    /**
     * The address
     */
    private List<String> address;
    /**
     * The contact number
     */
    private String contactNumber;
    /**
     * The email address
     */
    private String emailAddress;

    /**
     * @return the address
     */
    public List<String> getAddress()
    {
        return address;
    }

    /**
     * @param address
     *           the address to set
     */
    public void setAddress(final List<String> address)
    {
        this.address = address;
    }

    /**
     * @return the contactNumber
     */
    public String getContactNumber()
    {
        return contactNumber;
    }

    /**
     * @param contactNumber
     *           the contactNumber to set
     */
    public void setContactNumber(final String contactNumber)
    {
        this.contactNumber = contactNumber;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress()
    {
        return emailAddress;
    }

    /**
     * @param emailAddress
     *           the emailAddress to set
     */
    public void setEmailAddress(final String emailAddress)
    {
        this.emailAddress = emailAddress;
    }


}
