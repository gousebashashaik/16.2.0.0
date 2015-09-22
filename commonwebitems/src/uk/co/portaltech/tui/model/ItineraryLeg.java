/**
 *
 */
package uk.co.portaltech.tui.model;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.web.view.data.HasFeatures;

/**
 * @author
 *
 */
public class ItineraryLeg extends ProductData implements HasFeatures, Serializable
{
    private String title;

    private String image;

    private String seq;

    private String duration;

    private String arrivalDay;

    private String arrivalTime;

    private String days;

    private Map<String, List<Object>> featureCodesAndValues;

    private String legType;

    private boolean isVideoPresent;

    private boolean nonBookable;

    /**
     *
     */
    public ItineraryLeg() {

        this.featureCodesAndValues = new HashMap<String, List<Object>>();
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the seq
     */
    public String getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(String seq) {
        this.seq = seq;
    }

    /**
     * @return the duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return the arrivalDay
     */
    public String getArrivalDay() {
        return arrivalDay;
    }

    /**
     * @param arrivalDay the arrivalDay to set
     */
    public void setArrivalDay(String arrivalDay) {
        this.arrivalDay = arrivalDay;
    }

    /**
     * @return the arrivalTime
     */
    public String getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @param arrivalTime the arrivalTime to set
     */
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * @return the days
     */
    public String getDays() {
        return days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(String days) {
        this.days = days;
    }

    @Override
    public Map<String, List<Object>> getFeatureCodesAndValues() {
        return featureCodesAndValues;
    }

    @Override
    public void putFeatureCodesAndValues(Map<String, List<Object>> featureCodesAndValues) {
        this.featureCodesAndValues.putAll(featureCodesAndValues);
    }

    @Override
    public void putFeatureValue(String featureCode, List<Object> featureValues) {
        featureCodesAndValues.put(featureCode, featureValues);
    }

    public void putFeatureValueNonObjectType(String featureCode, List<? extends Object> featureValues) {
        featureCodesAndValues.put(featureCode, (List<Object>) featureValues);
    }

    @Override
    public List<Object> getFeatureValues(String featureCode) {
        return featureCodesAndValues.get(featureCode);
    }

    public String getFeatureValue(String featureCode)
    {
        if(MapUtils.isNotEmpty(featureCodesAndValues) && CollectionUtils.isNotEmpty(featureCodesAndValues.get(featureCode)))
        {
            return (String) featureCodesAndValues.get(featureCode).get(0);
        }
        else
        {
            return StringUtils.EMPTY;
        }

    }

    /**
     * @return the legType
     */
    public String getLegType() {
        return legType;
    }

    /**
     * @param legType the legType to set
     */
    public void setLegType(String legType) {
        this.legType = legType;
    }

    /**
     * @return the isVideoPresent
     */
    public boolean isVideoPresent() {
        return isVideoPresent;
    }

    /**
     * @param isVideoPresent the isVideoPresent to set
     */
    public void setVideoPresent(boolean isVideoPresent) {
        this.isVideoPresent = isVideoPresent;
    }

    /**
     * @param nonBookable the nonBookable to set
     */
    public void setNonBookable(boolean nonBookable) {
        this.nonBookable = nonBookable;
    }

    /**
     * @return the nonBookable
     */
    public boolean isNonBookable() {
        return nonBookable;
    }
}
