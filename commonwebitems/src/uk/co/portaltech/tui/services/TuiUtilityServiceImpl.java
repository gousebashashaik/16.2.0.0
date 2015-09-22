/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Utilities;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.BenefitModel;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.MetaDataRuleModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.ProductUspModel;
import uk.co.portaltech.travel.model.SEOEditorialTemplateModel;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.tui.exception.ServiceInputValidationException;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * This class provides utility methods required for controllers/web layer.
 *
 * @author bhuvanvikram.s
 */
public class TuiUtilityServiceImpl implements TuiUtilityService
{

   private static final int TWO = 2;

   @Resource
   private ProductService productService;

   @Resource
   private ProductBasicPopulator productBasicPopulator;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private SessionService sessionService;

   @Override
   public ProductData getProductByCode(final String code) throws ServiceInputValidationException
   {
      if (StringUtils.isEmpty(code))
      {
         final Map<String, String> errorParams = new HashMap<String, String>(TWO);
         errorParams.put("$serviceName", "TuiUtilityServiceImpl");
         errorParams.put("$value", code);
         throw new ServiceInputValidationException("1001", errorParams);
      }
      final ProductModel productModel = productService.getProductForCode(code);
      final ProductData productData = new ProductData();
      productBasicPopulator.populate(productModel, productData);
      return productData;
   }

   @Override
   public String getProductPageInventoryLink(final String productRangeTypeCode)
   {
      String inventorySearchURL =
         "http://www.firstchoice.co.uk/holiday/packages?airports%5B%5D=LN&units%5B%5D={0}%3AProductRange&when={1}&until=&flexibility=true&flexibleDays=3&noOfAdults=2&noOfSeniors=0&noOfChildren=0&childrenAge=&duration=7&first=0&sp=true";
      final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

      final Calendar c = Utilities.getDefaultCalendar();
      c.add(Calendar.DATE, 1);

      inventorySearchURL =
         MessageFormat.format(inventorySearchURL, productRangeTypeCode, sdf.format(c.getTime()));
      return inventorySearchURL;

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.services.TuiUtilityService#getSiteReleventBrands()
    */
   @Override
   public List<String> getSiteReleventBrands()
   {
      final CMSSiteModel currentSiteModel = cmsSiteService.getCurrentSite();
      final List<String> releventBrands = new ArrayList<String>();

      if (sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS) != null
         && currentSiteModel != null)
      {
         // first session request

         final BrandDetails brandDetails =
            sessionService.getCurrentSession().getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         releventBrands.addAll(brandDetails.getRelevantBrandIds());
      }

      return releventBrands;
   }

   @Override
   public List<String> getSiteReleventBrandPks()
   {
      final CMSSiteModel currentSiteModel = cmsSiteService.getCurrentSite();
      final List<String> releventBrands = new ArrayList<String>();

      if (sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS) != null
         && currentSiteModel != null)
      {
         // first session request

         final BrandDetails brandDetails =
            sessionService.getCurrentSession().getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         releventBrands.addAll(brandDetails.getRelevantBrands());
      }

      return releventBrands;
   }

   @Override
   public String getSiteBrand()
   {
      final CMSSiteModel currentSiteModel = cmsSiteService.getCurrentSite();
      String siteBrand = null;

      if (sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS) != null
         && currentSiteModel != null)
      {
         // first session request

         final BrandDetails brandDetails =
            sessionService.getCurrentSession().getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         siteBrand = brandDetails.getSiteUid();
      }
      return siteBrand;
   }

   public List<Map<String, String>> getItemMap(final List<String> sectionList)
   {
      final List<Map<String, String>> sectionMapList = new ArrayList<Map<String, String>>();
      String[] sectionArray = null;
      Map<String, String> sectionMap = null;
      sectionList.removeAll(Collections.singleton(null));
      for (final String section : sectionList)
      {
         sectionArray = section.split("\\|");
         if (!"".equals(sectionArray[0]) && !"null".equals(sectionArray[0]))
         {
            sectionMap = new HashMap<String, String>();
            for (int i = 0; i < sectionArray.length; i++)
            {
               sectionMap.put(String.valueOf(i), sectionArray[i]);
            }
            sectionMapList.add(sectionMap);
         }
      }
      return sectionMapList;

   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.services.TuiUtilityService#getBrandForModel(de.hybris.platform.core.model
    * .ItemModel)
    */
   @Override
   public String getBrandForModel(final ItemModel item)
   {

      if (isValidModel(item))
      {
         if (item instanceof AccommodationModel)
         {
            return getBrand(((AccommodationModel) item).getBrands());
         }
         else if (item instanceof LocationModel)
         {
            return getBrand(((LocationModel) item).getBrands());
         }
         else if (item instanceof MediaContainerModel)
         {
            return getBrand(((MediaContainerModel) item).getBrands());
         }
         else if (item instanceof ProductRangeModel)
         {
            return getBrand(((ProductRangeModel) item).getBrands());
         }
         else if (item instanceof BenefitModel)
         {
            return getBrand(((BenefitModel) item).getBrands());
         }
         else if (item instanceof FacilityModel)
         {
            return getBrand(((FacilityModel) item).getBrands());
         }
         else if (item instanceof ProductUspModel)
         {
            return getBrand(((ProductUspModel) item).getBrands());
         }
         else if (item instanceof SEOEditorialTemplateModel)
         {
            return getBrand(((SEOEditorialTemplateModel) item).getBrands());
         }
         else if (item instanceof MetaDataRuleModel)
         {
            return getBrand(((MetaDataRuleModel) item).getBrands());
         }
      }
      else
      {
         throw new IllegalArgumentException("Unsupported Item model.");
      }

      return StringUtils.EMPTY;
   }

   /**
    * @param brand
    * @return
    */
   private String getBrand(final List<BrandType> itemBrands)
   {
      if (itemBrands.size() > 1)
      {
         return getSiteBrand();
      }

      else
      {
         return itemBrands.get(0).getCode();
      }
   }

   /**
    * Retuns true for all the items which has brand type
    *
    * @param item
    * @return boolean
    */
   private boolean isValidModel(final ItemModel item)
   {
      return isAccomLocationCheck(item) || isBenefitOrFacitlity(item) || isSeoOrMetaCheck(item);
   }

   private boolean isAccomLocationCheck(final ItemModel item)
   {
      return item instanceof AccommodationModel || item instanceof LocationModel
         || item instanceof MediaContainerModel;
   }

   private boolean isBenefitOrFacitlity(final ItemModel item)
   {
      return item instanceof ProductRangeModel || item instanceof BenefitModel
         || item instanceof FacilityModel;
   }

   private boolean isSeoOrMetaCheck(final ItemModel item)
   {
      return item instanceof ProductUspModel || item instanceof SEOEditorialTemplateModel
         || item instanceof MetaDataRuleModel;
   }

   @Override
   public String getProductName(final String uid)
   {
      if ("FHV".equalsIgnoreCase(uid))
      {
         return "Holiday Village";
      }
      else if ("FMA".equalsIgnoreCase(uid))
      {
         return "Club Magic Life";
      }
      else if ("FPF".equalsIgnoreCase(uid))
      {
         return "Premier families";
      }
      else if ("FPR".equalsIgnoreCase(uid))
      {
         return "premier";
      }
      else if ("FSO".equalsIgnoreCase(uid))
      {
         return "SuneoClub";
      }
      else
      {
         return "SplashWorld";
      }

   }

   /**
    * A utility method to remove session attributes
    */
   @Override
   public void removeSessionObjects(final List<String> keys)
   {
      if (CollectionUtils.isNotEmpty(keys))
      {
         for (final String key : keys)
         {
            if (sessionService.getAttribute(key) != null)
            {
               sessionService.removeAttribute(key);

            }
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.services.TuiUtilityService#getSiteReleventBrands()
    */
   @Override
   public List<String> getFlightRouteReleventBrands()
   {
      final CMSSiteModel currentSiteModel = cmsSiteService.getCurrentSite();
      final List<String> releventBrands = new ArrayList<String>();
      if (sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS) != null
         && currentSiteModel != null)
      {
         final BrandDetails brandDetails =
            sessionService.getCurrentSession().getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         releventBrands.addAll(brandDetails.getIndexBrandCodes());
      }
      return releventBrands;
   }

   @Override
   public Map<String, String> getValueMap(final String section)
   {
      Map<String, String> sectionMap = null;
      final String[] sectionArray = section.split("\\|");
      if (!"".equals(sectionArray[0]) && !"null".equals(sectionArray[0]))
      {
         sectionMap = new HashMap<String, String>();
         for (int i = 0; i < sectionArray.length; i++)
         {
            sectionMap.put(String.valueOf(i), sectionArray[i]);
         }
      }
      return sectionMap;
   }
}