/**
 *
 */
package uk.co.portaltech.tui.web.view.data;



public class MerchandiserResponse {

    private  MerchandiserViewData merchandiserResult = new MerchandiserViewData();

    private MerchandiserRequest merchandiserRequest = new MerchandiserRequest();

    /**
     * @return the merchandiserResult
     */
    public MerchandiserViewData getMerchandiserResult() {
        return merchandiserResult;
    }

    /**
     * @param merchandiserResult the merchandiserResult to set
     */
    public void setMerchandiserResult(MerchandiserViewData merchandiserResult) {
        this.merchandiserResult = merchandiserResult;
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
