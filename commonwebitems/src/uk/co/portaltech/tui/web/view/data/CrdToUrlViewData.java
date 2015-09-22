/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import uk.co.portaltech.travel.enums.BrandType;


/**
 * @author EXTNM1
 *
 */
public class CrdToUrlViewData
{
    private String url;
    private BrandType brandType;

    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param url
     *           the url to set
     */
    public void setUrl(final String url)
    {
        this.url = url;
    }

    /**
     * @return the brandType
     */
    public BrandType getBrandType()
    {
        return brandType;
    }

    /**
     * @param brandType
     *           the brandType to set
     */
    public void setBrandType(final BrandType brandType)
    {
        this.brandType = brandType;
    }


}
