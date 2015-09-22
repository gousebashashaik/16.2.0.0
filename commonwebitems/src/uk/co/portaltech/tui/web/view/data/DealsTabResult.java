/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

public class DealsTabResult {

    private String dealsCategoryname;

    private String dealsCategorytitle;

    private  MerchandiserViewData merchandiserResult = new MerchandiserViewData();

    /**
     * @return the dealsCategoryname
     */
    public String getDealsCategoryname() {
        return dealsCategoryname;
    }

    /**
     * @param dealsCategoryname the dealsCategoryname to set
     */
    public void setDealsCategoryname(String dealsCategoryname) {
        this.dealsCategoryname = dealsCategoryname;
    }

    /**
     * @return the dealsCategorytitle
     */
    public String getDealsCategorytitle() {
        return dealsCategorytitle;
    }

    /**
     * @param dealsCategorytitle the dealsCategorytitle to set
     */
    public void setDealsCategorytitle(String dealsCategorytitle) {
        this.dealsCategorytitle = dealsCategorytitle;
    }

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




}
