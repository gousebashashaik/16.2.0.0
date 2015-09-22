/**
 *
 */
package uk.co.portaltech.tui.resolvers;

import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.tui.constants.GlobalNavigationConstant;
import uk.co.portaltech.tui.facades.CrdToUrlMappingFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.tui.web.common.enums.LocationPageType;

/**
 * @author EXTLP1
 *
 */
public class CruiseProductUrlResolver
{
   private String pattern;

   private String defaultSubPageType;

   private String overrideSubPageType;

   private String context = "";

   @Resource
   private CrdToUrlMappingFacade crdToUrlMapFacade;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private TypeService typeService;

   public static final int ZERO = 0;

   public static final String SEARCH_RESULT_TYPE = "SearchResultType";

   public String resolve(final String code, final SearchResultType searchType)
   {

      String url = StringUtils.EMPTY;
      String searchResultType = StringUtils.EMPTY;

      searchResultType = searchResult(searchType);

      if (GlobalNavigationConstant.GLOBAL_HEADER_APPPLIED_BRANDLIST.contains(tuiUtilityService
         .getSiteBrand()))
      {

         url =
            crdToUrlMapFacade.getUrlForCRD(code, searchResultType,
               typeService.getEnumerationValue("BrandType", BrandType.CR.getCode()).getPk()
                  .toString());

         if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(overrideSubPageType))
         {
            url = getTabUrl(url, overrideSubPageType);
         }
         overrideSubPageType = null;
         return url;
      }

      return url;
   }

   /**
    *
    */
   private String searchResult(final SearchResultType searchType)
   {
      String searchResultType = StringUtils.EMPTY;

      if (searchType.compareTo(SearchResultType.LOCATION) == ZERO)
      {

         searchResultType =
            typeService
               .getEnumerationValue(SEARCH_RESULT_TYPE, SearchResultType.LOCATION.getCode())
               .getPk().toString();

      }
      else if (searchType.compareTo(SearchResultType.ACCOMMODATION) == ZERO)
      {

         searchResultType =
            typeService
               .getEnumerationValue(SEARCH_RESULT_TYPE, SearchResultType.ACCOMMODATION.getCode())
               .getPk().toString();
      }
      else if (searchType.compareTo(SearchResultType.CRUISEAREA) == ZERO)
      {

         searchResultType =
            typeService
               .getEnumerationValue(SEARCH_RESULT_TYPE, SearchResultType.CRUISEAREA.getCode())
               .getPk().toString();

      }
      checkForCruiseAndStay(searchType);
      return searchResultType;
   }

   private String checkForCruiseAndStay(final SearchResultType searchType)
   {
      String searchResultType = StringUtils.EMPTY;
      if (searchType.compareTo(SearchResultType.CRUISEANDSTAY) == ZERO)
      {

         searchResultType =
            typeService
               .getEnumerationValue(SEARCH_RESULT_TYPE, SearchResultType.CRUISEANDSTAY.getCode())
               .getPk().toString();
      }
      return searchResultType;
   }

   public String getTabUrl(final String sourceUrl, final String overrideSubPageType)
   {

      final StringBuilder targetURL = new StringBuilder();
      targetURL.append(sourceUrl);

      if (!(LocationPageType.OVERVIEW.getCode().equalsIgnoreCase(overrideSubPageType)))
      {

         targetURL.append(overrideSubPageType);
         targetURL.append("/");
      }

      return targetURL.toString();
   }

   /**
    * @return the pattern
    */
   public String getPattern()
   {
      return pattern;
   }

   /**
    * @param pattern the pattern to set
    */
   public void setPattern(final String pattern)
   {
      this.pattern = pattern;
   }

   /**
    * @return the defaultSubPageType
    */
   public String getDefaultSubPageType()
   {
      return defaultSubPageType;
   }

   /**
    * @param defaultSubPageType the defaultSubPageType to set
    */
   public void setDefaultSubPageType(final String defaultSubPageType)
   {
      this.defaultSubPageType = defaultSubPageType;
   }

   /**
    * @return the overrideSubPageType
    */
   public String getOverrideSubPageType()
   {
      return overrideSubPageType;
   }

   /**
    * @param overrideSubPageType the overrideSubPageType to set
    */
   public void setOverrideSubPageType(final String overrideSubPageType)
   {
      this.overrideSubPageType = overrideSubPageType;
   }

   /**
    * @return the context
    */
   public String getContext()
   {
      return context;
   }

   /**
    * @param context the context to set
    */
   public void setContext(final String context)
   {
      this.context = context;
   }

   /**
    * @return the crdToUrlMapFacade
    */
   public CrdToUrlMappingFacade getCrdToUrlMapFacade()
   {
      return crdToUrlMapFacade;
   }

   /**
    * @param crdToUrlMapFacade the crdToUrlMapFacade to set
    */
   public void setCrdToUrlMapFacade(final CrdToUrlMappingFacade crdToUrlMapFacade)
   {
      this.crdToUrlMapFacade = crdToUrlMapFacade;
   }

   /**
    * @return the tuiUtilityService
    */
   public TuiUtilityService getTuiUtilityService()
   {
      return tuiUtilityService;
   }

   /**
    * @param tuiUtilityService the tuiUtilityService to set
    */
   public void setTuiUtilityService(final TuiUtilityService tuiUtilityService)
   {
      this.tuiUtilityService = tuiUtilityService;
   }
}
