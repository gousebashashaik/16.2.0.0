/**
 *
 */
package uk.co.tui.manage.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.domain.lite.Memo;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.Room;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.book.services.ISOCountryCodeService;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.common.DateUtils;
import uk.co.tui.manage.viewdata.AddressViewData;
import uk.co.tui.manage.viewdata.AvailableAccommodationViewData;
import uk.co.tui.manage.viewdata.BoardBasisViewData;
import uk.co.tui.manage.viewdata.ErrataViewData;
import uk.co.tui.manage.viewdata.PackageViewData;
import uk.co.tui.manage.viewdata.PassengerViewData;
import uk.co.tui.manage.viewdata.PriceViewData;
import uk.co.tui.manage.viewdata.RoomViewData;
import uk.co.tui.manage.viewdata.SummaryRoomViewData;

/**
 * @author premkumar.nd
 * 
 */
public class AccommodationViewDataPopulator implements Populator<Stay, PackageViewData>
{

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   private boolean readOnly;

   private static final String MIN_OCCUPANCY = "minOccupancy";

   private static final String MAX_OCCUPANCY = "maxOccupancy";

   @Resource
   private ISOCountryCodeService iSOCountryCodeService;

   /** The board basis property. */
   @Resource
   private PropertyReader boardBasisProperty;

   @Resource
   private CurrencyResolver currencyResolver;

   private static final int TWO = 2;

   /**
    * @param availableAccommodation
    * @param target
    */

   @Override
   public void populate(final Stay source, final PackageViewData target) throws ConversionException
   {

      populateAccommodationData(source, target);
      updatePassengersForAccom(target.getPassenger(), target.getAvailableAccommodationViewData()
         .getRoomViewData());
   }

   private void populateAccommodationData(final Stay source, final PackageViewData target)
   {

      final AvailableAccommodationViewData accommodationViewData =
         new AvailableAccommodationViewData();
      accommodationViewData.setWelfare(readOnly);
      accommodationViewData.setAccommStartDate(DateUtils.newDateFormat(new DateTime(source
         .getStartDate())));
      accommodationViewData.setAccommEndDate(DateUtils.newDateFormat(new DateTime(source
         .getEndDate())));
      accommodationViewData.setAccomCode(source.getCode());
      accommodationViewData.setAccomName(source.getName());
      populateErrataData(source, accommodationViewData);
      accommodationViewData.setDuration(String.valueOf(DateUtils.subtractDates(
         new DateTime(source.getEndDate()), new DateTime(source.getStartDate()))));
      populateRoomViewData(source.getRooms(), accommodationViewData);

      target.setAvailableAccommodationViewData(accommodationViewData);
      final PriceViewData boardBasisPrice = new PriceViewData();

      populateBoardBasisData(source, boardBasisPrice);

      if (target.getAvailableAccommodationViewData() != null && boardBasisPrice.getAmount() != null)
      {
         target.getAvailableAccommodationViewData().getBoardBasisViewData()
            .setPrice(boardBasisPrice);
      }

      populateAccommodationAddress(source, accommodationViewData);
   }

   /**
    * @param source
    * @param accommodationViewData
    */
   private void populateAccommodationAddress(final Stay source,
      final AvailableAccommodationViewData accommodationViewData)
   {
      final AddressViewData addressView = new AddressViewData();

      if (source.getAddress() != null)
      {
         addressView.setCity(source.getAddress().getTown());

         addressView.setCountry(source.getAddress().getCounty());
         addressView.setEmail(source.getAddress().getEmail());
         addressView.setHouseNumber(source.getAddress().getHouseNumber());
         addressView.setPhone(source.getAddress().getPhone1());
         addressView.setPostalcode(source.getAddress().getPostalcode());
         addressView.setPostbox(source.getAddress().getPobox());
         addressView.setStreetName(source.getAddress().getStreetname());
         addressView.setCountry(source.getAddress().getCounty());

         if (source.getAddress().getCounty() != null && !source.getAddress().getCounty().isEmpty())
         {

            addressView.setCountry(iSOCountryCodeService.getCountryByAlpha2Code(
               source.getAddress().getCounty()).getShortName());
         }

         accommodationViewData.setAddressViewData(addressView);
      }

   }

   /**
    * @param source
    * @param boardBasisPrice
    */
   private void populateBoardBasisData(final Stay source, final PriceViewData boardBasisPrice)
   {
      for (final Room room : source.getRooms())
      {

         if (room.getBoardBasis().getPrice() != null)
         {
            boardBasisPrice.setAmount(room.getBoardBasis().getPrice());
            boardBasisPrice.setCode(room.getBoardBasis().getPrice().toString());
            boardBasisPrice.setDescription(room.getBoardBasis().getPrice().toString());

         }
      }
   }

   /**
    * @param source
    * @param accommodationViewData
    */
   private void populateErrataData(final Stay source,
      final AvailableAccommodationViewData accommodationViewData)
   {
      if (CollectionUtils.isNotEmpty(source.getErrataMemo()))
      {
         final List<Memo> memos = source.getErrataMemo();
         final List<ErrataViewData> errataList = new ArrayList<ErrataViewData>();
         final ErrataViewData errViewData = new ErrataViewData();
         for (final Memo memo : memos)
         {
            errViewData.setCode(memo.getCode());
            errViewData.setName(memo.getName());
            errViewData.setDescription(memo.getDescription());
            errViewData.setMemoType(memo.getMemoType());
            errataList.add(errViewData);
         }
         accommodationViewData.setErrataViewData(errataList);
      }
   }

   /**
    * @param rooms
    * @param accommodationViewData
    */
   private void populateRoomViewData(final List<Room> source,
      final AvailableAccommodationViewData target)
   {

      // final AvailableAccommodationViewData availableAccommodationViewData =

      // final List<SummaryRoomViewData> summaryViewDatalist = new

      final List<RoomViewData> roomList = new ArrayList<RoomViewData>();
      if (!source.isEmpty())
      {
         // uk.co.portaltech.tui.enums
         final BoardBasisViewData boardBasisViewData = new BoardBasisViewData();

         if (null != source.get(0).getBoardBasis().getCode())
         {
            boardBasisViewData.setBoardBasisCode(source.get(0).getBoardBasis().getCode());
            boardBasisViewData.setBoardBasisName((boardBasisProperty.getValue(source.get(0)
               .getBoardBasis().getCode()
               + ".value")));
            target.setBoardBasisViewData(boardBasisViewData);
         }

      }

      for (final Room room : source)
      {
         final RoomViewData roomViewData = new RoomViewData();
         final Map<String, Integer> occupancy = new HashMap<String, Integer>();
         occupancy.put(MIN_OCCUPANCY, room.getRoomOccupancy().getMinOccupancy());
         occupancy.put(MAX_OCCUPANCY, room.getRoomOccupancy().getMaxOccupancy());
         roomViewData.setRoomCode(room.getCode());
         roomViewData.setRoomDesc(room.getRoomTitle());
         roomViewData.setQuantity(room.getQuantity());
         roomViewData.setOccupancy(occupancy);
         populatePassengerData(room.getPassgengers(), roomViewData);
         populatePriceData(room.getRoomUpgradePrice(), roomViewData);
         roomList.add(roomViewData);

      }

      target.setRoomViewData(roomList);
      populateSummaryViewData(target.getRoomViewData(), target.getSummaryRoomViewData());

   }

   private List<SummaryRoomViewData> populateSummaryView(
      final List<SummaryRoomViewData> summaryViewDataList, final RoomViewData roomViewData)
   {
      final SummaryRoomViewData summaryView = new SummaryRoomViewData();
      summaryView.setRoomCode(roomViewData.getRoomCode());
      summaryView.setDescription(getDescription("EXTRADESCRIPTION", roomViewData.getRoomDesc(),
         Integer.toString(roomViewData.getQuantity())));
      summaryView.setQuantity(roomViewData.getQuantity());
      summaryView.setIncluded(roomViewData.isIncluded());
      for (final PriceViewData priceViewData : roomViewData.getPriceList())
      {
         summaryView.setPrice(summaryView.getPrice().add(priceViewData.getAmount().getAmount()));
      }
      summaryView.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(currencyResolver
         .getSiteCurrency()) + summaryView.getPrice().setScale(TWO, RoundingMode.HALF_UP));

      summaryViewDataList.add(summaryView);
      return summaryViewDataList;
   }

   /**
    * @param passgengers
    * @param roomViewData
    */
   private void populatePassengerData(final Collection<Passenger> passgengers,
      final RoomViewData target)
   {

      final List<PassengerViewData> passengerViewList = new ArrayList<PassengerViewData>();

      for (final Passenger passenger : passgengers)
      {
         final PassengerViewData passengerViewData = new PassengerViewData();

         passengerViewData.setIdentifier(passenger.getId());
         passengerViewData.setType(passenger.getType().toString());
         passengerViewList.add(passengerViewData);

      }

      target.setPassengerViewDataList(passengerViewList);
   }

   /*
    * @param passenger
    * 
    * @param roomViewData
    */
   private void updatePassengersForAccom(final List<PassengerViewData> passengers,
      final List<uk.co.tui.manage.viewdata.RoomViewData> roomViewDatas)
   {
      for (final RoomViewData roomViewData : roomViewDatas)
      {
         int index = 0;
         for (final PassengerViewData passengerViewData : roomViewData.getPassengerViewDataList())
         {

            for (final PassengerViewData passengerViewData1 : passengers)
            {

               if (passengerViewData1.getIdentifier().equals(passengerViewData.getIdentifier()))
               {
                  roomViewData.getPassengerViewDataList().set(index, passengerViewData1);

                  index++;
               }

            }

         }
      }

   }

   /**
    * @param price
    * @param target
    */
   private void populatePriceData(final Price price, final RoomViewData target)
   {

      final List<PriceViewData> priceViewList = new ArrayList<PriceViewData>();

      if (price != null)
      {
         final PriceViewData priceViewData = new PriceViewData();
         priceViewData.setCode(price.getCode());
         if (price.getPriceType() != null)
         {
            priceViewData.setCodeType(price.getPriceType().toString());
         }
         priceViewData.setDescription(price.getDescription());
         final Money money = new Money();
         money.setAmount(price.getAmount().getAmount());
         money.setCurrency(price.getAmount().getCurrency());
         priceViewData.setAmount(money);
         priceViewData.setQuantity(price.getQuantity());
         target.setIncluded(Boolean.FALSE.booleanValue());
         priceViewList.add(priceViewData);
      }
      else
      {
         target.setIncluded(Boolean.TRUE.booleanValue());
      }
      target.setPriceList(priceViewList);

   }

   /**
    * Populate summary view data.
    * 
    * @param roomViewDataList the room view data list
    * @param summaryViewDataList the summary view data list
    */
   private List<SummaryRoomViewData> populateSummaryViewData(
      final List<RoomViewData> roomViewDataList, final List<SummaryRoomViewData> summaryViewDataList)
   {
      boolean present = false;
      List<SummaryRoomViewData> summaryViewDatas = new ArrayList<SummaryRoomViewData>();
      for (final RoomViewData roomViewData : roomViewDataList)
      {
         present = false;
         for (final SummaryRoomViewData summaryViewData : summaryViewDataList)
         {
            present = populateSummaryIfRoomMatches(roomViewData, summaryViewData);
         }
         if (!present)
         {
            summaryViewDatas = populateSummaryView(summaryViewDataList, roomViewData);
         }
      }
      return summaryViewDatas;
   }

   /**
    * To populate summary for room if room matches.
    * 
    * @param roomViewData
    * @param summaryViewData
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
            roomViewData.getRoomDesc(), Integer.toString(summaryViewData.getQuantity())));
         populatePrice(roomViewData, summaryViewData);
         return true;
      }
      return false;
   }

   /**
    * To populate the price from room view data.
    * 
    * @param roomViewData
    * @param summaryViewData
    */
   private void populatePrice(final RoomViewData roomViewData,
      final SummaryRoomViewData summaryViewData)
   {
      if (null != summaryViewData.getPrice() && null != roomViewData.getPriceList())
      {
         for (final PriceViewData priceViewData : roomViewData.getPriceList())
         {
            summaryViewData.setPrice(summaryViewData.getPrice().add(
               priceViewData.getAmount().getAmount()));
         }

         summaryViewData.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(currencyResolver
            .getSiteCurrency()) + summaryViewData.getPrice().setScale(TWO, RoundingMode.HALF_UP));
      }
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
}
