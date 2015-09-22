/**
 *
 */
package uk.co.tui.cr.book.populators.contents;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.cr.book.store.PackageExtraFacilityStore;
import uk.co.tui.cr.book.view.data.ContentViewData;
import uk.co.tui.domain.model.DynamicContentConfigModel;

/**
 * @author amaresh.d
 *
 */
public class CabinOptionsContentViewDataPopulator implements Populator<Object, ContentViewData>
{

   /** The generic content service. */
   @Resource
   private GenericContentService genericContentService;

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /**
    * Populates the room options content
    */
   @Override
   public void populate(final Object source, final ContentViewData target)
      throws ConversionException
   {
      final BasePackage packageModel = packageCartService.getBasePackage();
      final PackageExtraFacilityStore packageExtraFacilityStore =
         (PackageExtraFacilityStore) sessionService.getAttribute("PackageExtraFacilityStore");
      // Checking if store is null since Item Search will not be triggered in
      // Cabin page if Cruise extras is not applicable (ClassActWorkshop and StageAcademy)
      if (packageExtraFacilityStore != null)
      {
         final List<ExtraFacilityCategory> categoryList =
            packageExtraFacilityStore.getExtraFacilityLite(packageModel.getId());
         cruiseExtraFacility(target, categoryList);
      }
      populateReserveCabinContent(target);
      populateSpecialAssistanceContent(target);
      populatePremierServiceContent(target);
   }

   /**
    * @param target
    * @param categoryList
    */
   private void cruiseExtraFacility(final ContentViewData target,
      final List<ExtraFacilityCategory> categoryList)
   {
      if (CollectionUtils.isNotEmpty(categoryList))
      {
         for (final ExtraFacilityCategory category : categoryList)
         {
            checkCategoryCode(target, category);
         }
      }
   }

   /**
    * @param target
    * @param category
    */
   private void checkCategoryCode(final ContentViewData target, final ExtraFacilityCategory category)
   {
      if (StringUtils.equalsIgnoreCase(category.getCode(), ExtraFacilityConstants.STAGESCHOOL)
         || StringUtils.equalsIgnoreCase(category.getCode(),
            ExtraFacilityConstants.CLASS_ACT_WORKSHOP_OPTION_CATEGORY))
      {
         populateCruiseContentMap(category, target);
      }
   }

   /**
    * Populate cruise content map.
    *
    * @param category the category
    * @param target the target
    */
   private void populateCruiseContentMap(final ExtraFacilityCategory category,
      final ContentViewData target)
   {
      for (final ExtraFacility extra : category.getExtraFacilities())
      {
         final List<DynamicContentConfigModel> dynamicContents =
            genericContentService.getDynamicContentConfig(category.getInventoryCode(),
               extra.getExtraFacilityCode(), extra.getCorporateCode());
         populateContentMap(target, dynamicContents);
      }

   }

   /**
    * Populates the Content Map
    *
    * @param target
    * @param dynamicContents
    */
   private void populateContentMap(final ContentViewData target,
      final List<DynamicContentConfigModel> dynamicContents)
   {
      for (final DynamicContentConfigModel dynamicContent : dynamicContents)
      {
         target.getContentMap().putAll(
            genericContentService.getGenericDynamicContentValue(dynamicContent));
      }
   }

   /**
    * populate Reserve a cabin content
    *
    * @param target
    */

   private void populateReserveCabinContent(final ContentViewData target)
   {
      final List<DynamicContentConfigModel> dynamicContents =
         genericContentService.getDynamicContentConfig("B217FD33DEFE59D6", StringUtils.EMPTY,
            StringUtils.EMPTY);
      populateContentMap(target, dynamicContents);
   }

   /**
    * populate Cruise special assistance content
    *
    * @param target
    */
   private void populateSpecialAssistanceContent(final ContentViewData target)
   {
      final List<DynamicContentConfigModel> dynamicContents =
         genericContentService.getDynamicContentConfig("B17EC353C25432E9", StringUtils.EMPTY,
            StringUtils.EMPTY);
      populateContentMap(target, dynamicContents);
   }

   /**
    * populate Cabin Premier Service content.
    *
    * @param target
    */
   private void populatePremierServiceContent(final ContentViewData target)
   {
      final List<DynamicContentConfigModel> dynamicContents =
         genericContentService.getDynamicContentConfig("8760564980A3C6B9", StringUtils.EMPTY,
            StringUtils.EMPTY);
      populateContentMap(target, dynamicContents);
   }

}
