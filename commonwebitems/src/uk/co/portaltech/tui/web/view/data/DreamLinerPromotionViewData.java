/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;


/**
 * @author vinodkumar.g
 *
 */
public class DreamLinerPromotionViewData
{

    private String displayName;
    private String strapline;
    private List<String> pointsOnDreamLiner;

    /**
     * @return the displayName
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * @param displayName
     *           the displayName to set
     */
    public void setDisplayName(final String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * @return the strapline
     */
    public String getStrapline()
    {
        return strapline;
    }

    /**
     * @param strapline
     *           the strapline to set
     */
    public void setStrapline(final String strapline)
    {
        this.strapline = strapline;
    }

    /**
     * @return the pointsOnDreamLiner
     */
    public List<String> getPointsOnDreamLiner()
    {
        return pointsOnDreamLiner;
    }

    /**
     * @param pointsOnDreamLiner
     *           the pointsOnDreamLiner to set
     */
    public void setPointsOnDreamLiner(final List<String> pointsOnDreamLiner)
    {
        this.pointsOnDreamLiner = pointsOnDreamLiner;
    }

}
