/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static uk.co.portaltech.tui.constants.NumberConstants.FIVE;
import static uk.co.portaltech.tui.constants.NumberConstants.FOUR;
import static uk.co.portaltech.tui.constants.NumberConstants.ONE;
import static uk.co.portaltech.tui.constants.NumberConstants.SIX;
import static uk.co.portaltech.tui.constants.NumberConstants.THREE;
import static uk.co.portaltech.tui.constants.NumberConstants.TWO;
import static uk.co.portaltech.tui.constants.NumberConstants.ZERO;

import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.travel.model.CRDToURLMapModel;
import uk.co.portaltech.travel.services.CRDToURLMapService;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.facades.CrdToUrlMappingFacade;

import com.googlecode.ehcache.annotations.Cacheable;

/**
 * @author yuvarani.v
 * 
 */
public class DefaultCrdToUrlMappingFacade implements CrdToUrlMappingFacade
{

   /**
    * 
    */
   private static final String DOT = ".";

   /**
    * 
    */
   private static final String CANONICAL_TAG_NOT_REQUIRED_CRDS_KEY =
      "canonicalTAG.NotRequired.crds";

   /**
    * 
    */
   private static final String CANONICAL_TAGGING_REQUIRED_FLAG_KEY = "canonicalTaggingRequired";

   private static final String URL_CACHE = "URL";

   private static final String HOLIDAY_STR = "holidays";

   @Resource
   private TypeService typeService;

   @Resource
   private CRDToURLMapService crdToURLMapService;

   @Resource
   private TUIConfigService tuiConfigService;

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.CrdToUrlMappingFacade#getCrdFromUrl(java .lang.String)
    */
   @Override
   @Cacheable(cacheName = URL_CACHE)
   public String getCrdFromUrl(final String url, final SearchResultType crdType)
   {

      String crdCode = StringUtils.EMPTY;

      CRDToURLMapModel urlModel = null;
      urlModel = crdToURLMapService.findCRDForURLOptimized(url, crdType);

      if (urlModel != null)
      {
         crdCode = urlModel.getCode();
      }

      return crdCode;
   }

   // Imp: This is added only for the purpose of getting non-core product pages
   // and this method is not supposed to be used by anyone.
   @Override
   public String getCrdFromUrlForNonCoreProductPage(final String url, final SearchResultType crdType)
   {
      String tempUrl = url;

      String crdCode = StringUtils.EMPTY;

      CRDToURLMapModel urlModel = null;
      urlModel = crdToURLMapService.findCRDForURL(tempUrl, crdType);

      if (urlModel != null)
      {
         crdCode = urlModel.getCode();
      }

      while (StringUtils.isEmpty(crdCode))
      {
         final String parentUrl = generateParentUrl(tempUrl);
         urlModel = crdToURLMapService.findCRDForURL(parentUrl, crdType);

         if (urlModel != null)
         {
            crdCode = urlModel.getCode();
         }

         if (StringUtils.isEmpty(crdCode))
         {
            tempUrl = parentUrl;
            if ("/destinations/destinations.html".equalsIgnoreCase(tempUrl))
            {
               break;
            }
            continue;
         }
      }
      return crdCode;
   }

   private String generateParentUrl(final String sourceUrl)
   {

      final StringBuilder targetURL = new StringBuilder();
      final String[] srcUlrArray = sourceUrl.split("/");
      final int size = srcUlrArray.length;
      if (size > FIVE)
      {

         for (int i = ZERO; i < size - ONE; i++)
         {

            if (!srcUlrArray[i].isEmpty())
            {
               if (i > size - THREE && i < size - ONE && size > FOUR)
               {

                  targetURL.append("/" + HOLIDAY_STR + "-" + srcUlrArray[size - THREE] + ".html");

               }
               else
               {

                  targetURL.append("/");
                  targetURL.append(srcUlrArray[i]);
               }
            }

         }

      }
      else if (size > THREE && size < SIX)
      {
         // to generate for country and destinations

         for (int i = ZERO; i < size - TWO; i++)
         {
            if (!srcUlrArray[i].isEmpty())
            {
               if (i == size - THREE)
               {

                  targetURL.append("/" + srcUlrArray[i] + "/" + srcUlrArray[i] + ".html");

               }
               else
               {

                  targetURL.append("/");
                  targetURL.append(srcUlrArray[i]);
               }
            }

         }

      }
      else
      {
         // incase if url is /destinations/destionations.html
         targetURL.append(sourceUrl);
      }
      return targetURL.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.CrdToUrlMappingFacade#getUrlForCRD(java. lang.String,
    * uk.co.portaltech.travel.enums.SearchResultType)
    */
   @Override
   public String getUrlForCRD(final String crd, final SearchResultType crdType)
   {

      CRDToURLMapModel urlModel = null;
      String url = null;
      urlModel = crdToURLMapService.findURLForCRD(crd, crdType);
      if (urlModel != null)
      {
         url = urlModel.getURL();
      }
      return url;
   }

   @Override
   public String getUrlForCRD(final String crd, final String crdType, final String brandPk)
   {
      CRDToURLMapModel urlModel = null;
      String url = StringUtils.EMPTY;
      urlModel = crdToURLMapService.findURLForCRD(crd, crdType, brandPk);
      if (urlModel != null)
      {
         url = urlModel.getURL();
      }
      return url;
   }

   /**
    * 
    * @param crd
    * @param crdType
    * @param brandCode
    * @param currentSiteBrand
    * @return
    * @see uk.co.portaltech.tui.facades.CrdToUrlMappingFacade#getCanonicalUrlForCRD(java.lang.String,
    *      java.lang.String, java.lang.String, java.lang.String,java.lang.String)
    */
   @Cacheable(cacheName = "canonicalTagCache")
   @Override
   public String getCanonicalUrlForCRD(final String crd, final String crdType,
      final String brandCode, final String currentSiteBrand, final String tab)
   {
      CRDToURLMapModel urlModel = null;
      String url = StringUtils.EMPTY;
      final String taggingRequiredKey =
         currentSiteBrand + DOT + CANONICAL_TAGGING_REQUIRED_FLAG_KEY;
      final String noTaggedCrdKey = currentSiteBrand + DOT + CANONICAL_TAG_NOT_REQUIRED_CRDS_KEY;
      if (crdToURLMapService.isCanonicalUrlRequired(crd, taggingRequiredKey, noTaggedCrdKey,
         tuiConfigService.getConfigValuesMap(Arrays.asList(taggingRequiredKey, noTaggedCrdKey)),
         ","))
      {
         urlModel =
            crdToURLMapService.findURLForCRD(crd,
               typeService.getEnumerationValue("SearchResultType", getCrdType(crdType)).getPk()
                  .toString(), typeService.getEnumerationValue("BrandType", brandCode).getPk()
                  .toString());
         if (urlModel != null)
         {
            url = urlModel.getURL();
            if (StringUtils.isNotEmpty(tab))
            {
               final StringBuilder targetUrl =
                  new StringBuilder(url.substring(0, url.lastIndexOf('.')));
               targetUrl.append("/").append(tab);
               url = targetUrl.toString();
            }
         }
      }
      return url;
   }

   /**
    * @param crdType
    * @return
    */
   private String getCrdType(final String crdType)
   {
      return SearchResultType.CRUISEANDSTAY.toString().equals(crdType) ? SearchResultType.LOCATION
         .toString() : crdType;
   }

}
