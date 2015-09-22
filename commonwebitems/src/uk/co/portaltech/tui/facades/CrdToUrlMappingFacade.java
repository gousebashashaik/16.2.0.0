/**
 *
 */
package uk.co.portaltech.tui.facades;

import uk.co.portaltech.travel.enums.SearchResultType;

/**
 * @author yuvarani.v
 * 
 */
public interface CrdToUrlMappingFacade
{

   String getCrdFromUrl(String url, SearchResultType crdType);

   String getUrlForCRD(String crd, SearchResultType crdType);

   /**
    * 
    * @param url
    * @param crdType
    * @return
    */
   String getCrdFromUrlForNonCoreProductPage(String url, SearchResultType crdType);

   /**
    * 
    * @param crd
    * @param crdType
    * @param brandPk
    * @return
    */
   String getUrlForCRD(String crd, String crdType, String brandPk);

   /**
    * @description This method return canonical URL for a CRD code and type.
    * @param crd
    * @param crdType
    * @param brandType
    * @param currentSiteBrand
    * @return
    */
   String getCanonicalUrlForCRD(String crd, String crdType, String brandType,
      String currentSiteBrand, String tab);

}
