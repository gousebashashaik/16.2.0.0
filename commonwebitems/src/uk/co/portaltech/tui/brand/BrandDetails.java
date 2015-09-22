/**
 *
 */
package uk.co.portaltech.tui.brand;

import java.util.List;


/**
 * @author Kandipr
 *
 *         This class holds the brand details which would be set in session. These details are required to render the
 *         site specific CSS & query PIM for the brand specific content.
 *
 */
public class BrandDetails
{

    private String siteBrand;
    private List<String> relevantBrands;
    private String siteName;

    private String siteUid;

    private List<String> relevantBrandIds;
    private List<String> crossBrandCodes;
    private List<String> crossBrandPks;
    private List<String> indexBrandCodes;


    /**
     * @return the relevantBrandIds
     */
    public List<String> getRelevantBrandIds()
    {
        return relevantBrandIds;
    }

    /**
     * @param relevantBrandIds
     *           the relevantBrandIds to set
     */
    public void setRelevantBrandIds(final List<String> relevantBrandIds)
    {
        this.relevantBrandIds = relevantBrandIds;
    }

    /**
     * Returns the site brand PK
     *
     * @return the siteBrand
     */
    public String getSiteBrand()
    {
        return siteBrand;
    }

    /**
     * @param siteBrand
     *           the siteBrand to set
     */
    public void setSiteBrand(final String siteBrand)
    {
        this.siteBrand = siteBrand;
    }

    /**
     * A list of Brand PKs relevant to the site will be returned.
     *
     * @return the relevantBrands
     */
    public List<String> getRelevantBrands()
    {
        return relevantBrands;
    }

    /**
     * Set brand PKs. Dont set the brand names like TH, FC etc.,
     *
     * @param relevantBrands
     *           the relevantBrands to set
     */
    public void setRelevantBrands(final List<String> relevantBrands)
    {
        this.relevantBrands = relevantBrands;
    }

    /**
     * @return the siteName
     */
    public String getSiteName()
    {
        return siteName;
    }

    /**
     * @param siteName
     *           the siteNmae to set
     */
    public void setSiteName(final String siteName)
    {
        this.siteName = siteName;
    }

    /**
     * @return the siteUid
     */
    public String getSiteUid()
    {
        return siteUid;
    }

    /**
     * @param siteUid
     *           the siteUid to set
     */
    public void setSiteUid(final String siteUid)
    {
        this.siteUid = siteUid;
    }

    /**
     * @return the crossBrandCodes
     */
    public List<String> getCrossBrandCodes()
    {
        return crossBrandCodes;
    }

    /**
     * @param crossBrandCodes
     *           the crossBrandCodes to set
     */
    public void setCrossBrandCodes(final List<String> crossBrandCodes)
    {
        this.crossBrandCodes = crossBrandCodes;
    }

    /**
     * @return the crossBrandPks
     */
    public List<String> getCrossBrandPks()
    {
        return crossBrandPks;
    }

    /**
     * @param crossBrandPks
     *           the crossBrandPks to set
     */
    public void setCrossBrandPks(final List<String> crossBrandPks)
    {
        this.crossBrandPks = crossBrandPks;
    }

    /**
     * @return the indexBrandCodes
     */
    public List<String> getIndexBrandCodes()
    {
        return indexBrandCodes;
    }

    /**
     * @param indexBrandCodes the indexBrandCodes to set
     */
    public void setIndexBrandCodes(List<String> indexBrandCodes)
    {
        this.indexBrandCodes = indexBrandCodes;
    }
}
