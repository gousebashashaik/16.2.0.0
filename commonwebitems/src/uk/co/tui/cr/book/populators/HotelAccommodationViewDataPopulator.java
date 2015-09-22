/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AniteRoomModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.accommodation.MainstreamAccommodationService;
import uk.co.portaltech.travel.services.room.RoomsService;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.PersonType;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.ItemCodeAlertService;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.cr.book.view.data.AccommodationViewData;
import uk.co.tui.cr.book.view.data.PackageViewData;
import uk.co.tui.cr.book.view.data.RoomViewData;
import uk.co.tui.cr.book.view.data.SummaryRoomViewData;

/**
 * The Class AccommodationViewDataPopulator.
 *
 * @author samantha.gd
 */
public class HotelAccommodationViewDataPopulator implements Populator<BasePackage, PackageViewData>
{

   /** The accommodation service. */
   @Resource
   private MainstreamAccommodationService accommodationService;

   /** The Product Service . */
   @Resource
   private ProductService productService;

   /** The Rooms Service. */
   @Resource
   private RoomsService roomsService;

   /** The cms site service. */
   @Resource
   private CMSSiteService cmsSiteService;

   /** The book accommodation basic populator. */
   @Resource(name = "crBookAccommodationBasicPopulator")
   private DefaultAccommodationBasicPopulator bookAccommodationBasicPopulator;

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   /** The country code. */
   private String countryName = StringUtils.EMPTY;

   /** The logger to be used. */
   private static final TUILogUtils LOG = new TUILogUtils("HotelAccommodationViewDataPopulator");

   /** The item code alert service. */
   @Resource
   private ItemCodeAlertService itemCodeAlertService;

   /** The board basis property. */
   @Resource
   private PropertyReader boardBasisProperty;

   /** The currency resolver. */
   @Resource
   private CurrencyResolver currencyResolver;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   private static final int TWO = 2;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final BasePackage source, final PackageViewData target)
      throws ConversionException
   {
      final Stay stay = packageComponentService.getHotel(source);
      if (null != stay)
      {
         final AccommodationViewData accomViewData = new AccommodationViewData();
         populateAccomViewData(stay, accomViewData);
         target.getAccomViewData().add(accomViewData);
      }
   }

   /**
    * Populate accom view data.
    *
    * @param availableaccomm the availableaccomm
    * @param target the target
    */
   private void populateAccomViewData(final Stay availableaccomm, final AccommodationViewData target)
   {
      final List<Room> rooms = availableaccomm.getRooms();
      final AccommodationModel accommodationModel =
         accommodationService.getAccomodationByCodeAndCatalogVersion(availableaccomm.getCode(),
            cmsSiteService.getCurrentCatalogVersion());
      final List<CategoryModel> categories = new ArrayList(accommodationModel.getSupercategories());
      if (CollectionUtils.isNotEmpty(categories))
      {
         populateLocationViewData(target, accommodationModel, categories);
      }
      populateAccomImageUrl(target, accommodationModel);
      target.setAccomName(accommodationModel.getName());
      target.setBoardBasisName(boardBasisProperty.getValue(rooms.get(0).getBoardBasis().getCode()
         + ".value"));
      // some Accommodation/villas may not have official rating.
      if (availableaccomm.getOfficialRating() != null)
      {
         target.setRating(availableaccomm.getOfficialRating().getValue());
      }
      target.setAccomCode(accommodationModel.getCode());
      target.setDuration(availableaccomm.getDuration());
      populateRoomViewData(availableaccomm, target);
   }

   /**
    * Populate accom image url.
    *
    * @param target the target
    * @param accommodationModel the accommodation model
    */
   private void populateAccomImageUrl(final AccommodationViewData target,
      final AccommodationModel accommodationModel)
   {
      if (accommodationModel.getThumbnail() != null)
      {
         target.setAccomImageUrl(accommodationModel.getThumbnail().getURL());
      }
   }

   /**
    * Populate Location view data.
    *
    * @param accommViewData the accomm view data
    * @param accommodationModel the accommodation model
    * @param categories the categories
    */
   private void populateLocationViewData(final AccommodationViewData accommViewData,
      final AccommodationModel accommodationModel, final List<CategoryModel> categories)
   {

      boolean flag = false;
      for (final CategoryModel category : categories)
      {
         if (category.getClass() == LocationModel.class)
         {
            final LocationModel location = (LocationModel) category;
            updateResortName(accommViewData, accommodationModel, location);
            if (!flag)
            {
               flag = updateDestinationName(accommViewData, location);
            }
         }
         updateIfProductRange(accommViewData, accommodationModel, category);
      }
      accommViewData.setCountryName(getCountryName(categories));
      accommViewData.setEmptyCountryName(isEmptyCountryName(accommViewData));
   }

   /**
    * Checks if is empty country name.
    *
    * @param accommViewData the accomm view data
    * @return true, if is empty country name
    */
   private boolean isEmptyCountryName(final AccommodationViewData accommViewData)
   {
      if (StringUtils.isEmpty(accommViewData.getCountryName())
         || (StringUtils.isNotEmpty(accommViewData.getResortName()) && StringUtils
            .isNotEmpty(accommViewData.getDestinationName())))
      {
         return true;
      }
      return false;
   }

   /**
    * Set the destination name.
    *
    * @param accommViewData the accomm view data
    * @param location the location
    * @return true if location type matches
    */
   private boolean updateDestinationName(final AccommodationViewData accommViewData,
      final LocationModel location)
   {
      if (LocationType.DESTINATION.equals(location.getType()))
      {
         accommViewData.setDestinationName(location.getName());
         return true;
      }
      else if (LocationType.REGION.equals(location.getType()))
      {
         accommViewData.setDestinationName(location.getName());
         return true;
      }
      else if (LocationType.COUNTRY.equals(location.getType()))
      {
         accommViewData.setDestinationName(location.getName());
         return true;
      }
      return false;
   }

   /**
    * Set the resort name.
    *
    * @param accommViewData the accomm view data
    * @param accommodationModel the accommodation model
    * @param location the location
    */
   private void updateResortName(final AccommodationViewData accommViewData,
      final AccommodationModel accommodationModel, final LocationModel location)
   {
      if (LocationType.RESORT.equals(location.getType()))
      {
         accommViewData.setResortName(location.getName());
         final List<CategoryModel> subCategories = location.getSupercategories();
         populateLocationViewData(accommViewData, accommodationModel, subCategories);
      }
   }

   /**
    * Populate the accommodation view data w.r.t product range.
    *
    * @param accommViewData the accomm view data
    * @param accommodationModel the accommodation model
    * @param category the category
    */
   private void updateIfProductRange(final AccommodationViewData accommViewData,
      final AccommodationModel accommodationModel, final CategoryModel category)
   {
      bookAccommodationBasicPopulator.populate(accommodationModel, accommViewData);
      if (category instanceof ProductRangeModel)
      {
         accommViewData.setDifferentiatedProduct(true);
      }
   }

   /**
    * Method to return AccommodationModel from accommodation Code from PIM.
    *
    * @param accomCode the accom code
    * @return AccommodationModel
    */
   private AccommodationModel getAccommodationModel(final String accomCode)
   {
      return (AccommodationModel) productService.getProductForCode(accomCode);
   }

   /**
    * Gets the country name.
    *
    * @param categories the categories
    * @return the country name
    */
   private String getCountryName(final List<CategoryModel> categories)
   {
      if (CollectionUtils.isNotEmpty(categories))
      {
         for (final CategoryModel category : categories)
         {
            if (category.getClass() == LocationModel.class)
            {
               countryName = setCountryName(category);
            }
         }
      }
      return countryName;
   }

   /**
    * Sets the country name.
    *
    * @param category the category
    * @return the string
    */
   private String setCountryName(final CategoryModel category)
   {
      final LocationModel location = (LocationModel) category;
      if (LocationType.COUNTRY == location.getType())
      {
         countryName = location.getName();
         return countryName;
      }
      else
      {
         return getCountryName(location.getSupercategories());
      }
   }

   /**
    * Populate room view data.
    *
    * @param availableaccomm the source
    * @param target the target
    */
   @SuppressWarnings("boxing")
   private void populateRoomViewData(final Stay availableaccomm, final AccommodationViewData target)
   {
      final AccommodationModel accomModel = getAccommodationModel(availableaccomm.getCode());
      for (final Room room : availableaccomm.getRooms())
      {
         final RoomViewData roomViewData = new RoomViewData();
         final AniteRoomModel aniteRoomModel =
            roomsService.getAniteRoomForCodeAndAccomCode(room.getCode(), availableaccomm
               .getSupplierProductDetails().getSellingCode(), cmsSiteService
               .getCurrentCatalogVersion());
         populateRoomDescription(room, roomViewData, accomModel, aniteRoomModel);
         // What the price we are getting is not a expected value.
         // When ever you change some thing here, please check the rmslt
         // webanalytics param also.
         if (room.getDefaultRoom().booleanValue())
         {
            populatePrice(room, roomViewData);
         }
         else
         {
            roomViewData.setPrice(room.getRoomUpgradePrice().getAmount().getAmount());
            roomViewData.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(currencyResolver
               .getSiteCurrency())
               + room.getRoomUpgradePrice().getAmount().getAmount()
                  .setScale(TWO, RoundingMode.HALF_UP));
         }
         roomViewData.setQuantity(1);
         roomViewData.setIncluded(room.getDefaultRoom().booleanValue());
         roomViewData.setRoomCode(room.getCode());
         // getting the count of each passenger type in each room -- done for
         // viewdata
         final Collection<Passenger> allocatedPassengers = room.getPassgengers();
         roomViewData.setNoOfAdults(PassengerUtils.getPersonTypeCountFromPassengers(
            allocatedPassengers,
            EnumSet.of(PersonType.ADULT, PersonType.SUPERSENIOR, PersonType.SENIOR)));
         roomViewData.setNoOfChildren(PassengerUtils.getPersonTypeCountFromPassengers(
            allocatedPassengers, EnumSet.of(PersonType.CHILD)));
         roomViewData.setNoOfInfants(PassengerUtils.getPersonTypeCountFromPassengers(
            allocatedPassengers, EnumSet.of(PersonType.INFANT)));
         roomViewData.setTotalChildrenCount(PassengerUtils.getPersonTypeCountFromPassengers(
            allocatedPassengers, EnumSet.of(PersonType.CHILD, PersonType.INFANT)));
         roomViewData.setChildAges(PassengerUtils.getChildAges(allocatedPassengers));
         target.getRoomViewData().add(roomViewData);
      }

      populateSummaryViewData(target.getRoomViewData(), target.getSummaryRoomViewData());
   }

   /**
    * To populate the room price from room model.
    *
    * @param room model
    * @param roomViewData the room view data
    */
   private void populatePrice(final Room room, final RoomViewData roomViewData)
   {
      if (null != room.getPrice() && null != room.getPrice().getAmount())
      {
         roomViewData.setPrice(room.getPrice().getAmount().getAmount());
         roomViewData.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(currencyResolver
            .getSiteCurrency())
            + room.getPrice().getAmount().getAmount().setScale(TWO, RoundingMode.HALF_UP));
      }
   }

   /**
    * Populate summary view data.
    *
    * @param roomViewDataList the room view data list
    * @param summaryViewDataList the summary view data list
    */
   private void populateSummaryViewData(final List<RoomViewData> roomViewDataList,
      final List<SummaryRoomViewData> summaryViewDataList)
   {
      boolean present = false;
      for (final RoomViewData roomViewData : roomViewDataList)
      {
         present = false;
         present = iterateSummaryViewDataList(summaryViewDataList, present, roomViewData);
         if (!present)
         {
            populateSummaryView(summaryViewDataList, roomViewData);
         }
      }
   }

   /**
    * @param summaryViewDataList
    * @param boolValue
    * @param roomViewData
    * @return boolean
    */
   private boolean iterateSummaryViewDataList(final List<SummaryRoomViewData> summaryViewDataList,
      final boolean boolValue, final RoomViewData roomViewData)
   {
      boolean present = boolValue;
      for (final SummaryRoomViewData summaryViewData : summaryViewDataList)
      {
         present = populateSummaryIfRoomMatches(roomViewData, summaryViewData);
         if (present)
         {
            break;
         }
      }
      return present;
   }

   /**
    * To populate summary for room if room matches.
    *
    * @param roomViewData the room view data
    * @param summaryViewData the summary view data
    * @return true if room matches
    */
   private boolean populateSummaryIfRoomMatches(final RoomViewData roomViewData,
      final SummaryRoomViewData summaryViewData)
   {
      if (StringUtils.equalsIgnoreCase(summaryViewData.getRoomCode(), roomViewData.getRoomCode())
         && (summaryViewData.isIncluded() == roomViewData.isIncluded()))
      {
         summaryViewData.setQuantity(summaryViewData.getQuantity() + 1);

         summaryViewData.setDescription(getDescription("EXTRADESCRIPTION",
            roomViewData.getDescription(), Integer.toString(summaryViewData.getQuantity())));
         populatePrice(roomViewData, summaryViewData);
         return true;
      }
      return false;
   }

   /**
    * To populate the price from room view data.
    *
    * @param roomViewData the room view data
    * @param summaryViewData the summary view data
    */
   private void populatePrice(final RoomViewData roomViewData,
      final SummaryRoomViewData summaryViewData)
   {
      if (null != summaryViewData.getPrice() && null != roomViewData.getPrice())
      {
         summaryViewData.setPrice(summaryViewData.getPrice().add(roomViewData.getPrice()));
         summaryViewData.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(currencyResolver
            .getSiteCurrency()) + summaryViewData.getPrice().setScale(TWO, RoundingMode.HALF_UP));
      }
   }

   /**
    * Populate summary view.
    *
    * @param summaryViewDataList the summary view data list
    * @param roomViewData the room view data
    */
   private void populateSummaryView(final List<SummaryRoomViewData> summaryViewDataList,
      final RoomViewData roomViewData)
   {
      final SummaryRoomViewData summaryView = new SummaryRoomViewData();
      summaryView.setRoomCode(roomViewData.getRoomCode());
      summaryView.setDescription(getDescription("EXTRADESCRIPTION", roomViewData.getDescription(),
         Integer.toString(roomViewData.getQuantity())));
      summaryView.setQuantity(roomViewData.getQuantity());
      summaryView.setIncluded(roomViewData.isIncluded());
      summaryView.setPrice(roomViewData.getPrice());
      summaryView.setCurrencyAppendedPrice(roomViewData.getCurrencyAppendedPrice());

      summaryViewDataList.add(summaryView);
   }

   /**
    * Gets the description from content csv.
    *
    * @param key the key
    * @param value the value
    * @return the description
    */
   private String getDescription(final String key, final String... value)
   {
      final Map<String, String> summaryContentMap = staticContentServ.getSummaryContents();

      if (StringUtils.isNotEmpty(key))
      {
         final String summaryContent = summaryContentMap.get(key);
         if (SyntacticSugar.isNotNull(summaryContent))
         {
            if (SyntacticSugar.isEmpty(value))
            {
               return summaryContent;
            }
            return StringUtil.substitute(summaryContent, value);
         }
      }
      return StringUtils.EMPTY;
   }

   /**
    * This method populates the RoomDescription.
    *
    * @param room the room
    * @param roomViewData the room view data
    * @param accomModel the accom model
    * @param aniteRoomModel the anite room model
    */
   private void populateRoomDescription(final Room room, final RoomViewData roomViewData,
      final AccommodationModel accomModel, final AniteRoomModel aniteRoomModel)
   {
      if (StringUtils.isNotEmpty(room.getRoomTitle()))
      {
         roomViewData.setDescription(room.getRoomTitle());
      }
      else if (aniteRoomModel != null)
      {
         roomViewData.setDescription(aniteRoomModel.getShortRoomTitle());
      }
      else
      {
         itemCodeAlertService.updateRoomsMissingContent(accomModel.getCode(), room.getCode());
         LOG.debug("************ No roomModel returned for this accommodation "
            + accomModel.getCode() + " *********");
      }
   }

}
