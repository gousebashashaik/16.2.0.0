/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.SEOEditorialTemplateModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.facades.CrdToUrlMappingFacade;
import uk.co.portaltech.tui.facades.SEOEditorialTemplateFacade;
import uk.co.portaltech.tui.web.view.data.SEOEditorialTemplateViewData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author l.furrer
 *
 */
public class DefaultSEOEditorialTemplateFacade implements SEOEditorialTemplateFacade
{

   @Resource
   private FeatureService featureService;

   @Resource
   private CrdToUrlMappingFacade crdToUrlMapFacade;

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private TypeService typeService;

   @Override
   public SEOEditorialTemplateViewData resolveLocationNameForTemplate(
      final SEOEditorialTemplateModel seoTemplate, final LocationModel location,
      final String siteUid)
   {

      String countryName = StringUtils.EMPTY;
      String regionName = StringUtils.EMPTY;
      String destinationName = StringUtils.EMPTY;
      String resortName = StringUtils.EMPTY;
      final String name = "name";
      final String brand = brandUtils.getFeatureServiceBrand(location.getBrands());
      if ("COUNTRY".equals(location.getType().getCode()))
      {
         countryName =
            featureService.getFirstFeatureValueAsString(name, location, new Date(), brand);
         countryName = checkName(countryName);
      }
      else if ("REGION".equals(location.getType().getCode()))
      {
         countryName =
            featureService.getFirstFeatureValueAsString(name, getSuperLocation(location),
               new Date(), brand);
         countryName = checkName(countryName);
         regionName =
            featureService.getFirstFeatureValueAsString(name, location, new Date(), brand);
         regionName = checkName(regionName);
      }
      else if ("DESTINATION".equals(location.getType().getCode()))
      {
         countryName =
            featureService.getFirstFeatureValueAsString(name,
               getSuperLocation(getSuperLocation(location)), new Date(), brand);
         countryName = checkName(countryName);
         regionName =
            featureService.getFirstFeatureValueAsString(name, getSuperLocation(location),
               new Date(), brand);
         regionName = checkName(regionName);
         destinationName =
            featureService.getFirstFeatureValueAsString(name, location, new Date(), brand);
         destinationName = checkName(destinationName);
      }
      else if ("RESORT".equals(location.getType().getCode()))
      {
         countryName =
            featureService.getFirstFeatureValueAsString(name,
               getSuperLocation(getSuperLocation(getSuperLocation(location))), new Date(), brand);
         countryName = checkName(countryName);
         regionName =
            featureService.getFirstFeatureValueAsString(name,
               getSuperLocation(getSuperLocation(location)), new Date(), brand);
         regionName = checkName(regionName);
         destinationName =
            featureService.getFirstFeatureValueAsString(name, getSuperLocation(location),
               new Date(), brand);
         destinationName = checkName(destinationName);
         resortName =
            featureService.getFirstFeatureValueAsString(name, location, new Date(), brand);
         resortName = checkName(resortName);
      }

      final SEOEditorialTemplateViewData viewData = new SEOEditorialTemplateViewData();
      if (seoTemplate != null)
      {
         viewData.setSeoTitle(replaceText(seoTemplate.getSeoTitle(), countryName, regionName,
            destinationName, resortName, location.getCode(), siteUid));
         viewData.setSeoSubtitle(replaceText(seoTemplate.getSeoSubtitle(), countryName, regionName,
            destinationName, resortName, location.getCode(), siteUid));
         viewData.setSeoBody1(replaceText(seoTemplate.getSeoBody1(), countryName, regionName,
            destinationName, resortName, location.getCode(), siteUid));
         viewData.setSeoBody2(replaceText(seoTemplate.getSeoBody2(), countryName, regionName,
            destinationName, resortName, location.getCode(), siteUid));

         viewData.setMetaKeywords(replaceText(seoTemplate.getMetaKeywords(), countryName,
            regionName, destinationName, resortName, location.getCode(), siteUid));
         viewData.setMetaTitle(replaceText(seoTemplate.getMetaTitle(), countryName, regionName,
            destinationName, resortName, location.getCode(), siteUid));
         viewData.setMetaDescription(replaceText(seoTemplate.getMetaDescription(), countryName,
            regionName, destinationName, resortName, location.getCode(), siteUid));
      }
      return viewData;
   }

   /**
    * Gets the refference url.
    *
    * @param siteName the site name
    * @param code the code
    * @param location the location
    * @return the refference url
    */
   private String getRefferenceUrl(final String siteUid, final String code,
      final String... location)
   {
      String refferenceUrl = null;
      if (StringUtils.equalsIgnoreCase(siteUid, "TH"))
      {

         refferenceUrl =
            crdToUrlMapFacade.getUrlForCRD(
               code,
               typeService
                  .getEnumerationValue("SearchResultType", SearchResultType.LOCATION.getCode())
                  .getPk().toString(),
               typeService.getEnumerationValue("BrandType", BrandType.valueOf(siteUid).getCode())
                  .getPk().toString());

      }
      else if (StringUtils.equalsIgnoreCase(siteUid, "FC"))
      {

         for (final String locations : location)
         {

            if (StringUtils.isNotEmpty(locations))
            {

               refferenceUrl =
                  Config.getString(siteUid + ".locationOverviewURL", " ") + locations + "-" + code;

            }
         }
      }

      return refferenceUrl;
   }

   /**
    * @param name
    * @return name This method checks if the mail is null or not.
    *
    */
   private String checkName(final String name)
   {
      if (name == null)
      {
         return StringUtils.EMPTY;
      }
      else
      {
         return name;
      }

   }

   private String replaceText(final String originalText, final String countryName,
      final String regionName, final String destinationName, final String resortName,
      final String epicCode, final String siteUid)
   {

      String original = originalText;
      if (StringUtils.isNotBlank(original))
      {
         original = original.replace("[country]", countryName);
         original = original.replace("[region]", regionName);
         original = original.replace("[destination]", destinationName);
         original = original.replace("[resort]", resortName);
         original = original.replace("[Epic Code]", epicCode);
         original = original.replace("[Country]", countryName);
         original = original.replace("[Region]", regionName);
         original = original.replace("[Destination]", destinationName);
         original = original.replace("[Resort]", resortName);
         original = original.replace("[BrandName]", Config.getString(siteUid + ".siteName", " "));
         original =
            original.replace(
               "[Refference]",
               getRefferenceUrl(siteUid, epicCode, countryName, regionName, destinationName,
                  resortName));

      }

      return original;
   }

   /**
    * Gets the superlocation for this one.<br/>
    * I cannot just use {@link LocationModel#getSupercategories()} because that would also return
    * the "utility categories" like 'regions'.
    *
    * @param location the location for which we want to know its father
    * @return the father {@link LocationModel} or the same Location if there is no father
    */
   private LocationModel getSuperLocation(final LocationModel location)
   {
      final List<CategoryModel> supercategories = location.getSupercategories();
      for (final CategoryModel supercategory : supercategories)
      {
         if (supercategory instanceof LocationModel)
         {
            return (LocationModel) supercategory;
         }
      }
      return location;
   }

}
