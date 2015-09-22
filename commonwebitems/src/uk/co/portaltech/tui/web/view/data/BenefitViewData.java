/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import de.hybris.platform.commercefacades.product.data.ImageData;

import java.io.Serializable;
import java.util.List;


/**
 * @author omonikhide
 *
 */
public class BenefitViewData implements Serializable
{

    private String name;
    private String description;
    private String thunbnailUrl;
    private List<ImageData> list;

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public String getThunbnailUrl()
    {
        return thunbnailUrl;
    }

    public void setThunbnailUrl(final String thunbnailUrl)
    {
        this.thunbnailUrl = thunbnailUrl;
    }

    public List<ImageData> getList()
    {
        return list;
    }

    public void setList(final List<ImageData> list)
    {
        this.list = list;
    }

}
