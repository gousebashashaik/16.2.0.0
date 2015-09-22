/**
 *
 */
package uk.co.portaltech.tui.web.view.data;


public class FlightCarrier {

    private String name;
    private String code;

    private String carrierCode;

    /**
     * @return the carrierCode
     */
    public String getCarrierCode() {
        return carrierCode;
    }

    /**
     * @param carrierCode the carrierCode to set
     */
    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    /**
     * @return the carrier
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param carrier
     *           the carrier to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @param code
     *           the code to set
     */
    public void setCode(final String code)
    {
        this.code = code;
    }

}
