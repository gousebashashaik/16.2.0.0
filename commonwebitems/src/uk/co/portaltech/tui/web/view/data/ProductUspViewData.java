/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author s.consolino
 *
 */
public class ProductUspViewData extends ViewData implements HasFeatures
{

    private String code;
    private String name;
    private String description;
    private String thumbnailUrl;
    private String pictureUrl;
    private final Map<String, List<Object>> featureCodesAndValues;

    public ProductUspViewData()
    {
        super();
        this.featureCodesAndValues = new HashMap<String, List<Object>>();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("\n");
        stringBuffer.append("code: ");
        stringBuffer.append(code);
        stringBuffer.append("\n");
        stringBuffer.append("name: ");
        stringBuffer.append(name);
        stringBuffer.append("\n");
        stringBuffer.append("description: ");
        stringBuffer.append(description);
        stringBuffer.append("\n");
        stringBuffer.append("thumbnailUrl: ");
        stringBuffer.append(thumbnailUrl);
        return stringBuffer.toString();
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     *           the code to set
     */
    public void setCode(final String code)
    {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *           the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *           the description to set
     */
    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * @return the thumbnailUrl
     */
    public String getThumbnailUrl()
    {
        return thumbnailUrl;
    }

    /**
     * @param thumbnailUrl
     *           the thumbnailUrl to set
     */
    public void setThumbnailUrl(final String thumbnailUrl)
    {
        this.thumbnailUrl = thumbnailUrl;
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

    public String getPictureUrl()
    {
        return pictureUrl;
    }

    public void setPictureUrl(final String pictureUrl)
    {
        this.pictureUrl = pictureUrl;
    }
}
