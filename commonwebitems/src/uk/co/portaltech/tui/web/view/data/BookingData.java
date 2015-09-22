/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.io.Serializable;


/**
 * @author pts
 *
 */
public class BookingData implements Serializable
{
    private String componentUid;
    private String productCode;
    private String categoryCode;
    private String domainName;
    private String siteUrl;

    /**
     * @return the componentUid
     */
    public String getComponentUid()
    {
        return componentUid;
    }

    /**
     * @param componentUid
     *           the componentUid to set
     */
    public void setComponentUid(final String componentUid)
    {
        this.componentUid = componentUid;
    }

    /**
     * @return the productCode
     */
    public String getProductCode()
    {
        return productCode;
    }

    /**
     * @param productCode
     *           the productCode to set
     */
    public void setProductCode(final String productCode)
    {
        this.productCode = productCode;
    }

    /**
     * @return the categoryCode
     */
    public String getCategoryCode()
    {
        return categoryCode;
    }

    /**
     * @param categoryCode
     *           the categoryCode to set
     */
    public void setCategoryCode(final String categoryCode)
    {
        this.categoryCode = categoryCode;
    }

    /**
     * @return the domainName
     */
    public String getDomainName()
    {
        return domainName;
    }

    /**
     * @param domainName
     *           the domainName to set
     */
    public void setDomainName(final String domainName)
    {
        this.domainName = domainName;
    }

    /**
     * @return the siteUrl
     */
    public String getSiteUrl()
    {
        return siteUrl;
    }

    /**
     * @param siteUrl
     *           the siteUrl to set
     */
    public void setSiteUrl(final String siteUrl)
    {
        this.siteUrl = siteUrl;
    }

}
