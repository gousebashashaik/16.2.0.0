/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;

/**
 * @author gagan
 *
 */
public class CMSLinkComponentViewData {

    private ProductData  product;
    private CategoryData categoryFrontend;
    private LinkTargets  target;
    private String       linkName;
    private CategoryData category;
    private String       url;
    private ProductData  productFrontend;
    private boolean      external;
    private String      uid;

    /**
     * @return the product
     */
    public ProductData getProduct() {
        return product;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(ProductData product) {
        this.product = product;
    }

    /**
     * @return the categoryFrontend
     */
    public CategoryData getCategoryFrontend() {
        return categoryFrontend;
    }

    /**
     * @param categoryFrontend
     *            the categoryFrontend to set
     */
    public void setCategoryFrontend(CategoryData categoryFrontend) {
        this.categoryFrontend = categoryFrontend;
    }

    /**
     * @return the target
     */
    public LinkTargets getTarget() {
        return target;
    }

    /**
     * @param target
     *            the target to set
     */
    public void setTarget(LinkTargets target) {
        this.target = target;
    }

    /**
     * @return the linkName
     */
    public String getLinkName() {
        return linkName;
    }

    /**
     * @param linkName
     *            the linkName to set
     */
    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    /**
     * @return the category
     */
    public CategoryData getCategory() {
        return category;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(CategoryData category) {
        this.category = category;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the productFrontend
     */
    public ProductData getProductFrontend() {
        return productFrontend;
    }

    /**
     * @param productFrontend
     *            the productFrontend to set
     */
    public void setProductFrontend(ProductData productFrontend) {
        this.productFrontend = productFrontend;
    }

    /**
     * @return the external
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * @param external
     *            the external to set
     */
    public void setExternal(boolean external) {
        this.external = external;
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }


}
