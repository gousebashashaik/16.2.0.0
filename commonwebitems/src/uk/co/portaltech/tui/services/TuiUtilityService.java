/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;
import java.util.Map;

import uk.co.tui.exception.ServiceInputValidationException;

/**
 * @author bhuvanvikram.s
 *
 */
public interface TuiUtilityService
{

   /**
    * This method gives product's basic information by product code.
    *
    * @param code
    * @return ProductData
    * @throws ServiceInputValidationException
    */
   ProductData getProductByCode(String code) throws ServiceInputValidationException;

   List<String> getSiteReleventBrands();

   List<String> getSiteReleventBrandPks();

   String getSiteBrand();

   /**
    * This method gives inventory result page for product range type.
    *
    * @param productRangeTypeCode
    * @return
    */
   String getProductPageInventoryLink(String productRangeTypeCode);

   /**
    * @param nonGeoItem
    * @return List<Map<String,String>>
    */
   List<Map<String, String>> getItemMap(List<String> sectionList);

   /**
    * Returns brand for a given model.. following is the approach to find the brand 1. If the model
    * is not shared between brands, just return the brandType code 2. If the model is shared between
    * brands, find the brand of the cmssite model and return the site mode.
    *
    * @param item
    * @return brand
    */
   String getBrandForModel(ItemModel item);

   String getProductName(String uid);

   void removeSessionObjects(List<String> keys);

   /**
    * @return
    */
   List<String> getFlightRouteReleventBrands();

   Map<String, String> getValueMap(String section);

}
