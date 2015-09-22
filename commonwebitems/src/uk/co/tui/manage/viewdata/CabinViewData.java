/**
 *
 */
package uk.co.tui.manage.viewdata;

/**
 * @author premkumar.nd
 *
 */
public class CabinViewData extends RoomViewData
{
    private String cabinNum;
    private MediaViewData cabinImage;




    /**
     * @return the cabinNum
     */
    public String getCabinNum()
    {
        return cabinNum;
    }

    /**
     * @param cabinNum
     *           the cabinNum to set
     */
    public void setCabinNum(final String cabinNum)
    {
        this.cabinNum = cabinNum;
    }

    /**
     * @return the cabinImage
     */
    public MediaViewData getCabinImage()
    {
        return cabinImage;
    }

    /**
     * @param cabinImage
     *           the cabinImage to set
     */
    public void setCabinImage(final MediaViewData cabinImage)
    {
        this.cabinImage = cabinImage;
    }



}
