/**
 *
 */
package uk.co.portaltech.tui.enums;

/**
 * @author ext
 *
 */
public enum IscapeBrandLookUp
{
    TH ("THOMSON"),
    FC ("FCSUN");

    private final String brand;

    /**
     * custom
     */
    private IscapeBrandLookUp(String brand) {

        this.brand = brand;
    }

    /**
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }
}


