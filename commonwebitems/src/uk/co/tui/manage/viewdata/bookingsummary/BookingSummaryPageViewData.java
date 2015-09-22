/**
 *
 */
package uk.co.tui.manage.viewdata.bookingsummary;

import uk.co.tui.manage.criteria.BookingSearchCriteria;
import uk.co.tui.manage.viewdata.AgentViewData;
import uk.co.tui.manage.viewdata.ErrorViewData;
import uk.co.tui.manage.viewdata.ManageBookingSummaryPageStaticContentViewData;
import uk.co.tui.manage.viewdata.PackageViewData;


/**
 * @author veena.pn
 *
 */
public class BookingSummaryPageViewData
{

    private PackageViewData packageViewData;
    private AgentViewData agent;
    private ErrorViewData error;
    private BookingSearchCriteria bookingSearchCriteria;
    private ManageBookingSummaryPageStaticContentViewData manageBookingSummaryContentViewData;

    /**
     * @return the bookingSearchCriteria
     */
    public BookingSearchCriteria getBookingSearchCriteria()
    {
        return bookingSearchCriteria;
    }

    /**
     * @param bookingSearchCriteria
     *           the bookingSearchCriteria to set
     */
    public void setBookingSearchCriteria(final BookingSearchCriteria bookingSearchCriteria)
    {
        this.bookingSearchCriteria = bookingSearchCriteria;
    }

    /**
     * @return the agent
     */
    public AgentViewData getAgent()
    {
        return agent;
    }

    /**
     * @param agent
     *           the agent to set
     */
    public void setAgent(final AgentViewData agent)
    {
        this.agent = agent;
    }

    /**
     * @return the error
     */
    public ErrorViewData getError()
    {
        return error;
    }

    /**
     * @param error
     *           the error to set
     */
    public void setError(final ErrorViewData error)
    {
        this.error = error;
    }

    /**
     * @return the packageViewData
     */
    public PackageViewData getPackageViewData()
    {
        return packageViewData;
    }

    /**
     * @param packageViewData
     *           the packageViewData to set
     */
    public void setPackageViewData(final PackageViewData packageViewData)
    {
        this.packageViewData = packageViewData;
    }

    /**
     * @return the manageBookingSummaryContentViewData
     */
    public ManageBookingSummaryPageStaticContentViewData getManageBookingSummaryContentViewData()
    {
        return manageBookingSummaryContentViewData;
    }

    /**
     * @param manageBookingSummaryContentViewData
     *           the manageBookingSummaryContentViewData to set
     */
    public void setManageBookingSummaryContentViewData(
            final ManageBookingSummaryPageStaticContentViewData manageBookingSummaryContentViewData)
    {
        this.manageBookingSummaryContentViewData = manageBookingSummaryContentViewData;
    }




}
