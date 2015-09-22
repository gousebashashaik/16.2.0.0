/**
 *
 */
package uk.co.tui.manage.viewdata;

import uk.co.tui.book.domain.lite.EquipmentType;



/**
 * @author sunil.bd
 *
 */
public class FlightViewData
{

    private CarrierInformationViewData carrierInformation;

    private EquipmentType airEquipementType;

    private String equipementDescription;

    private CabinClassViewData cabinClass;

    /**
     * @return the carrierInformation
     */
    public CarrierInformationViewData getCarrierInformation()
    {
        return carrierInformation;
    }

    /**
     * @param carrierInformation
     *           the carrierInformation to set
     */
    public void setCarrierInformation(final CarrierInformationViewData carrierInformation)
    {
        this.carrierInformation = carrierInformation;
    }



    /**
     * @return the equipementDescription
     */
    public String getEquipementDescription()
    {
        return equipementDescription;
    }

    /**
     * @param equipementDescription
     *           the equipementDescription to set
     */
    public void setEquipementDescription(final String equipementDescription)
    {
        this.equipementDescription = equipementDescription;
    }

    /**
     * @return the cabinClass
     */
    public CabinClassViewData getCabinClass()
    {
        return cabinClass;
    }

    /**
     * @param cabinClass
     *           the cabinClass to set
     */
    public void setCabinClass(final CabinClassViewData cabinClass)
    {
        this.cabinClass = cabinClass;
    }

    /**
     * @return the airEquipementType
     */
    public EquipmentType getAirEquipementType()
    {
        return airEquipementType;
    }

    /**
     * @param airEquipementType
     *           the airEquipementType to set
     */
    public void setAirEquipementType(final EquipmentType airEquipementType)
    {
        this.airEquipementType = airEquipementType;
    }





}
