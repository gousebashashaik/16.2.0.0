/**
 *
 */
package uk.co.portaltech.tui.web.view.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author l.furrer
 *
 */
public class FacilityViewData implements HasFeatures, Serializable
{

    private final Map<String, List<Object>> featureCodesAndValues;

    private String name;
    private String description;
    private String facilityType;
    private String parentFacilityType;


    private String parentFacilityTypeName;
    private String openingTimes;
    private Collection<MediaViewData> galleryImages;
    private String pdfMediaUrl;

    public FacilityViewData()
    {
        this.featureCodesAndValues = new HashMap<String, List<Object>>();
        this.galleryImages = new ArrayList<MediaViewData>();
    }

    /**
     *
     * @return the name of the facility
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name of the facility
     *
     * @param name
     *           the name of the facility
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     *
     * @return the description of the facility
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Sets the description of the facility
     *
     * @param description
     *           the description of the facility
     */
    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * @return the facilityType
     */
    public String getFacilityType()
    {
        return facilityType;
    }

    /**
     * @param facilityType
     *           the facilityType to set
     */
    public void setFacilityType(final String facilityType)
    {
        this.facilityType = facilityType;
    }

    /**
     * @return the openingTimes
     */
    public String getOpeningTimes()
    {
        return openingTimes;
    }

    /**
     * @param openingTimes
     *           the openingTimes to set
     */
    public void setOpeningTimes(final String openingTimes)
    {
        this.openingTimes = openingTimes;
    }



    /**
     * @return the galleryImages
     */
    public Collection<MediaViewData> getGalleryImages()
    {
        return galleryImages;
    }

    /**
     * @param galleryImages
     *           the galleryImages to set
     */
    public void setGalleryImages(final Collection<MediaViewData> galleryImages)
    {
        this.galleryImages = galleryImages;
    }

    /**
     * @return the pdfMediaUrl
     */
    public String getPdfMediaUrl()
    {
        return pdfMediaUrl;
    }

    /**
     * @param pdfMediaUrl
     *           the pdfMediaUrl to set
     */
    public void setPdfMediaUrl(final String pdfMediaUrl)
    {
        this.pdfMediaUrl = pdfMediaUrl;
    }

    @Override
    public Map<String, List<Object>> getFeatureCodesAndValues()
    {
        return this.featureCodesAndValues;
    }

    @Override
    public void putFeatureCodesAndValues(final Map<String, List<Object>> featureCodeAndValues)
    {
        this.featureCodesAndValues.putAll(featureCodeAndValues);
    }

    @Override
    public void putFeatureValue(final String featureCode, final List<Object> featureValues)
    {
        this.featureCodesAndValues.put(featureCode, featureValues);
    }

    @Override
    public List<Object> getFeatureValues(final String featureCode)
    {
        return this.featureCodesAndValues.get(featureCode);
    }

    /**
     * @return the parentFacilityType
     */
    public String getParentFacilityType()
    {
        return parentFacilityType;
    }

    /**
     * @param parentFacilityType
     *           the parentFacilityType to set
     */
    public void setParentFacilityType(final String parentFacilityType)
    {
        this.parentFacilityType = parentFacilityType;
    }

    /**
     * @return the parentFacilityTypeName
     */
    public String getParentFacilityTypeName()
    {
        return parentFacilityTypeName;
    }

    /**
     * @param parentFacilityTypeName
     *           the parentFacilityTypeName to set
     */
    public void setParentFacilityTypeName(final String parentFacilityTypeName)
    {
        this.parentFacilityTypeName = parentFacilityTypeName;
    }

}