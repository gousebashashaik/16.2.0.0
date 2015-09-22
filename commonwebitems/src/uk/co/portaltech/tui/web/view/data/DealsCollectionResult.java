/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;



public class DealsCollectionResult
{

    private MerchandiserRequest merchandiserRequest = new MerchandiserRequest();

    private boolean categorizedPage;

    private List<DealsTabResult> tabbedDealsList = new ArrayList<DealsTabResult>();


    /**
     * @return the tabbedDealsList
     */
    public List<DealsTabResult> getTabbedDealsList() {
        return tabbedDealsList;
    }
    /**
     * @param tabbedDealsList the tabbedDealsList to set
     */
    public void setTabbedDealsList(List<DealsTabResult> tabbedDealsList) {
        this.tabbedDealsList = tabbedDealsList;
    }
    /**
     * @return the categorizedPage
     */
    public boolean isCategorizedPage() {
        return categorizedPage;
    }
    /**
     * @param categorizedPage the categorizedPage to set
     */
    public void setCategorizedPage(boolean categorizedPage) {
        this.categorizedPage = categorizedPage;
    }
    /**
     * @return the merchandiserRequest
     */
    public MerchandiserRequest getMerchandiserRequest() {
        return merchandiserRequest;
    }
    /**
     * @param merchandiserRequest the merchandiserRequest to set
     */
    public void setMerchandiserRequest(MerchandiserRequest merchandiserRequest) {
        this.merchandiserRequest = merchandiserRequest;
    }



    }
