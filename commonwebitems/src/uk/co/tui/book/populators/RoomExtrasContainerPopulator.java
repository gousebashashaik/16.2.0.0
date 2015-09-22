/**
 *
 */
package uk.co.tui.book.populators;

import static uk.co.tui.book.constants.BookFlowConstants.BOXING;

import de.hybris.platform.commerceservices.converter.Populator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.services.UpdateLateCheckoutService;
import uk.co.tui.book.services.inventory.PackageExtrasService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.ExtraFacilityUtils;
import uk.co.tui.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.book.view.data.ExtraFacilityViewData;
import uk.co.tui.book.view.data.ExtraFacilityViewDataContainer;

/**
 * @author pradeep.as
 *
 */
public class RoomExtrasContainerPopulator implements
   Populator<List<ExtraFacility>, ExtraFacilityViewDataContainer>
{

   /** Package Cart Service. */
   @Resource
   private PackageCartService packageCartService;

   /** The update late checkout service. */
   @Resource
   private UpdateLateCheckoutService updateLateCheckoutService;

   /** The package extras service. */
   @Resource
   private PackageExtrasService packageExtrasServiceLite;

   /** The static content utils. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   /** The Constant ROOMS_LA_TEXT. */
   private static final String ROOMS_LA_TEXT = "Rooms_LA_Text";

   private static final int TWO = 2;

   @Resource
   private PackageComponentService packageComponentService;

   /**
    * The populator method to populate the ExtraFacilityViewDataContainer value required for page.
    *
    * @param source the source
    * @param target the target
    */
   @Override
   public void populate(final List<ExtraFacility> source,
      final ExtraFacilityViewDataContainer target)
   {
      for (final ExtraFacility extras : source)
      {
         populateLateCheckoutOptions(extras, target);
      }

   }

   /**
    * Populate late checkout options.
    *
    * @param extras the extras
    * @param target the target
    */
   private void populateLateCheckoutOptions(final ExtraFacility extras,
      final ExtraFacilityViewDataContainer target)
   {
      if (StringUtils.equalsIgnoreCase(extras.getExtraFacilityCode(), ExtraFacilityConstants.LCD))
      {
         populateLateCheckout(extras, target);
      }

   }

   /**
    * Populates the extraFacilityModel to ExtraFacilityViewDataContainer.
    *
    * @param extraFacilityModel the extra facility model
    * @param target the target
    */
   @SuppressWarnings(BOXING)
   private void populateLateCheckout(final ExtraFacility extraFacilityModel,
      final ExtraFacilityViewDataContainer target)
   {

      final BasePackage packageModel = getPackageModel();

      final List<Room> roomModelList = packageComponentService.getStay(packageModel).getRooms();
      final int roomModelSize = roomModelList.size();
      int noOfSelectedRooms = getQuantity(extraFacilityModel);
      final int noOfAvailableRooms = getNoOfAvlRooms(extraFacilityModel, roomModelSize);

      final List<ExtraFacility> pkgLCD =
         (List<ExtraFacility>) ExtraFacilityUtils.getExtrasForCategoryAndCode("Accommodation",
            "LCD", packageModel.getExtraFacilityCategories());
      if (CollectionUtils.isNotEmpty(pkgLCD)
         && pkgLCD.get(0).getPrices().get(0).getQuantity() > noOfAvailableRooms)
      {
         updateLateCheckoutService.addLateCheckout(packageModel, pkgLCD.get(0),
            String.valueOf(noOfAvailableRooms));
         noOfSelectedRooms = noOfAvailableRooms;
      }

      final ExtraFacilityCategoryViewData lateChkotCategoryViewData = target.getLateCheckOut();
      lateChkotCategoryViewData.setExtraFacilityCategoryCode(extraFacilityModel
         .getExtraFacilityCategory().getCode());
      lateChkotCategoryViewData.setExtraFacilityGroupCode(ExtraFacilityConstants.LCD);

      final ExtraFacilityViewData extraViewData = new ExtraFacilityViewData();
      extraViewData.setSelected(extraFacilityModel.isSelected());
      extraViewData.setCode(extraFacilityModel.getExtraFacilityCode());
      extraViewData.setGroupCode(extraFacilityModel.getExtraFacilityGroup().toString());
      extraViewData.setAvailableQuantity(extraFacilityModel.getQuantity());
      final Currency currency = extraFacilityModel.getPrices().get(0).getRate().getCurrency();
      final BigDecimal unitPrice = extraFacilityModel.getPrices().get(0).getRate().getAmount();
      extraViewData.setCurrencyAppendedPerPersonPrice(getCurrencyAppendedPrice(
         unitPrice.setScale(TWO, RoundingMode.HALF_UP), currency));

      final BigDecimal selectedQuantity = new BigDecimal(noOfSelectedRooms);
      final BigDecimal totalPrice = selectedQuantity.multiply(unitPrice);
      extraViewData.setPrice(totalPrice);
      extraViewData.setCurrencyAppendedPrice(getCurrencyAppendedPrice(
         totalPrice.setScale(TWO, RoundingMode.HALF_UP), currency));
      extraViewData.setDescription(extraFacilityModel.getDescription());
      extraViewData.setQuantity(noOfAvailableRooms);
      extraViewData.setSelectedQuantity(noOfSelectedRooms);

      populateLCDLAI(packageExtrasServiceLite.getLateCheckOutThresoldValues(), extraViewData);

      lateChkotCategoryViewData.getExtraFacilityViewData().add(extraViewData);
      lateChkotCategoryViewData.setAvailable(true);
      target.setLateCheckOut(lateChkotCategoryViewData);
   }

   /**
    * Populate late checkout lai.
    *
    * @param lateCheckOutThreshold the late check out threshold
    * @param extraViewData the extra view data
    */
   private void populateLCDLAI(final int lateCheckOutThreshold,
      final ExtraFacilityViewData extraViewData)
   {
      if (extraViewData.getAvailableQuantity() <= lateCheckOutThreshold)
      {
         extraViewData.setLimitedAvailability(true);
         extraViewData.setLimitedAvailabilityText(fetchStaticContent().get(ROOMS_LA_TEXT)
            .toString());
      }
   }

   /**
    * Fetch static content.
    *
    * @return the map
    */
   private Map<String, String> fetchStaticContent()
   {
      return staticContentServ.getRoomContents();
   }

   /**
    * The method fetches the package model from the cart.
    *
    * @return BasePackage
    */
   private BasePackage getPackageModel()
   {
      return packageCartService.getBasePackage();
   }

   /**
    * Gets the no of avl rooms.
    *
    * @param extraFacilityModel the extra facility model
    * @param roomModelSize the roomModelSize
    * @return the no of avl rooms
    */
   @SuppressWarnings(BOXING)
   private int getNoOfAvlRooms(final ExtraFacility extraFacilityModel, final int roomModelSize)
   {
      if (extraFacilityModel.getQuantity() < roomModelSize)
      {
         return extraFacilityModel.getQuantity();
      }
      return roomModelSize;

   }

   /**
    * To get the currency appended price.
    *
    * @param price the price
    * @param currency the currency
    * @return the currency appended price
    */
   private String getCurrencyAppendedPrice(final BigDecimal price, final Currency currency)
   {
      return currency.getSymbol() + price;
   }

   private int getQuantity(final ExtraFacility extraFacilityModel)
   {
      return extraFacilityModel.getPrices().get(0).getQuantity() != null ? extraFacilityModel
         .getPrices().get(0).getQuantity().intValue() : 0;
   }

}
