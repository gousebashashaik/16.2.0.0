/**
 *
 */
package uk.co.tui.manage.viewdata;

/**
 * @author naresh.gls
 *
 */
public class BookingSummaryPageViewData
{

    private PackageViewData packageViewData;
    private AgentViewData agentViewData;


    private ManageHomePageStaticContentViewData manageHomePageContentViewData;

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
     * @return the agentViewData
     */
    public AgentViewData getAgentViewData()
    {
        return agentViewData;
    }

    /**
     * @param agentViewData
     *           the agentViewData to set
     */
    public void setAgentViewData(final AgentViewData agentViewData)
    {
        this.agentViewData = agentViewData;
    }

    /**
     * @return the homePageContentViewData
     */
    public ManageHomePageStaticContentViewData getManageHomePageContentViewData()
    {
        return manageHomePageContentViewData;
    }

    /**
     * @param manageHomePageContentViewData
     *           the manageHomePageContentViewData to set
     */
    public void setManageHomePageContentViewData(final ManageHomePageStaticContentViewData manageHomePageContentViewData)
    {
        this.manageHomePageContentViewData = manageHomePageContentViewData;
    }



}
