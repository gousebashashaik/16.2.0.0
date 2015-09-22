/**
 *
 */
package uk.co.tui.shortlist.facade.impl;

import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.portaltech.travel.model.HolidayWishListModel;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.InclusivePackage;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.shortlist.account.populator.WishlistEntry;
import uk.co.tui.shortlist.account.services.CookieManagementService;
import uk.co.tui.shortlist.account.services.DefaultShortlistService;
import uk.co.tui.shortlist.data.ShortlistHolidayData;
import uk.co.tui.shortlist.data.ShortlistPaxDetails;
import uk.co.tui.shortlist.data.ShortlistRoomData;
import uk.co.tui.shortlist.facade.DefaultShortlistFacade;
import uk.co.tui.shortlist.populators.SavedHolidayViewDataPopulator;
import uk.co.tui.shortlist.populators.ShortlistHolidayPackagePopulator;
import uk.co.tui.shortlist.view.data.SavedHolidayViewData;

import com.thoughtworks.xstream.converters.ConversionException;

/**
 * This class is used to perform add to shortlist functionality
 *
 * @author ramkishore.p
 *
 */

@Component
public class DefaultShortlistFacadeImpl implements DefaultShortlistFacade
{

   private DefaultShortlistService defaultShortlistService;

   private SavedHolidayViewDataPopulator savedHolidayViewDataPopulator;

   private SessionService sessionService;

   private ShortlistHolidayPackagePopulator shortlistHolidayPackagePopulator;

   private CookieManagementService cookieManagementService;

   private static final Logger LOGGER = Logger.getLogger(DefaultShortlistFacadeImpl.class);

   @Resource
   private CacheUtil cacheUtil;

   /**
    * @return the sessionService
    */
   public SessionService getSessionService()
   {
      return sessionService;
   }

   /**
    * @param sessionService the sessionService to set
    */
   public void setSessionService(final SessionService sessionService)
   {
      this.sessionService = sessionService;
   }

   /**
    * @return the shortlistHolidayPackagePopulator
    */
   public ShortlistHolidayPackagePopulator getShortlistHolidayPackagePopulator()
   {
      return shortlistHolidayPackagePopulator;
   }

   /**
    * @param shortlistHolidayPackagePopulator the shortlistHolidayPackagePopulator to set
    */
   public void setShortlistHolidayPackagePopulator(
      final ShortlistHolidayPackagePopulator shortlistHolidayPackagePopulator)
   {
      this.shortlistHolidayPackagePopulator = shortlistHolidayPackagePopulator;
   }

   /**
    * @return the cookieManagementService
    */
   public CookieManagementService getCookieManagementService()
   {
      return cookieManagementService;
   }

   /**
    * @param cookieManagementService the cookieManagementService to set
    */
   public void setCookieManagementService(final CookieManagementService cookieManagementService)
   {
      this.cookieManagementService = cookieManagementService;
   }

   /**
    * @param defaultShortlistService the defaultShortlistService to set
    */
   public void setDefaultShortlistService(final DefaultShortlistService defaultShortlistService)
   {
      this.defaultShortlistService = defaultShortlistService;
   }

   /**
    * @param savedHolidayViewDataPopulator the savedHolidayViewDataPopulator to set
    */
   public void setSavedHolidayViewDataPopulator(
      final SavedHolidayViewDataPopulator savedHolidayViewDataPopulator)
   {
      this.savedHolidayViewDataPopulator = savedHolidayViewDataPopulator;
   }

   /**
    * This method is to read the cookie value based on brand.
    *
    * @param cookies
    * @param brandType
    * @return String
    */
   @Override
   public String getCookieValue(final Cookie[] cookies, final String brandType)
   {
      String cookieValue = null;
      if (cookies != null)
      {
         for (final Cookie cookie : cookies)
         {
            if (StringUtils.equalsIgnoreCase(cookie.getName(), brandType))
            {
               cookieValue = cookie.getValue();
               break;
            }
         }
      }
      return cookieValue;
   }

   /**
    * To get update Package model entries for Anonymous user
    *
    */
   @Override
   public List<SavedHolidayViewData> viewShortlists(final String wishlistId)
      throws ConversionException
   {
      WishlistEntry wishlistEntry;
      List<SavedHolidayViewData> packageViewDataList;
      wishlistEntry =
         defaultShortlistService.viewShortlists(wishlistId, getWishlistsFromExistCache(wishlistId),
            getWishlistsFromRemoveCache(wishlistId));

      try
      {
         packageViewDataList = getPackageViewDataList(wishlistEntry);
      }
      catch (final ConversionException e)
      {
         LOGGER.error("Conversion exception" + e);

         throw new ConversionException(e.getMessage());
      }
      return packageViewDataList;
   }

   /**
    * This method is to delete wishlist entry.
    *
    * @param wishListEnrtyId
    */
   @Override
   public void deleteWishlistEntry(final String wishListEnrtyId)
   {
      defaultShortlistService.deleteWishlistEntry(wishListEnrtyId);

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.customer.facade.DefaultShortlistFacade#deleteShortlist(java .lang.String)
    */
   @Override
   public void deleteShortlist(final String wishListId)
   {
      final HolidayWishListModel holidayWishListModel =
         defaultShortlistService.getWishlists(wishListId);
      defaultShortlistService.removeHolidayWishListEntry(holidayWishListModel, wishListId);

   }

   /**
    * Helper method for each each updated PackaeModel to get the PackageViewData
    *
    * @param wishlistEntry
    * @param sortBy
    * @return List
    */
   private List<SavedHolidayViewData> getPackageViewDataList(final WishlistEntry wishlistEntry)
   {
      List<SavedHolidayViewData> savedHolidayViewData = new ArrayList<SavedHolidayViewData>();
      final boolean isSoldout = false;
      final boolean isDatePassed = false;
      List<BigDecimal> priceDiff = new ArrayList<BigDecimal>();
      final List<BasePackage> packageModel = wishlistEntry.getPackageModel();
      final String wishlistId = wishlistEntry.getWishlistId();
      final String createdDate = wishlistEntry.getCreatedTime();
      if (CollectionUtils.isNotEmpty(packageModel))
      {
         final List<String> wishlistEntryId = wishlistEntry.getWishlistEntryId();
         priceDiff = wishlistEntry.getPriceDifference();

         savedHolidayViewData =
            iterateAllPackageModel(packageModel, wishlistEntryId, wishlistId, isSoldout,
               isDatePassed, priceDiff, createdDate, wishlistEntry);

      }

      populateSoldOutPackages(savedHolidayViewData, wishlistEntry, wishlistId);

      populateDatePassedPackages(savedHolidayViewData, wishlistEntry, wishlistId);

      return savedHolidayViewData;
   }

   /**
    * @param savedHolidayViewData
    * @param wishlistEntry
    * @param wishlistId
    */
   private void populateDatePassedPackages(final List<SavedHolidayViewData> savedHolidayViewData,
      final WishlistEntry wishlistEntry, final String wishlistId)
   {
      final List<BasePackage> datePassedPackageModel = wishlistEntry.getDatePassedPackageList();
      final List<BigDecimal> priceDiff = new ArrayList<BigDecimal>();
      final boolean isSoldout = false;
      boolean isDatePassed = true;

      if (CollectionUtils.isNotEmpty(datePassedPackageModel))
      {
         isDatePassed = true;
         final List<String> datePassedAvilableEntryId =
            wishlistEntry.getDatePassedWishlistEntryId();
         List<SavedHolidayViewData> datePassedViewData;
         datePassedViewData =
            iterateAllPackageModel(datePassedPackageModel, datePassedAvilableEntryId, wishlistId,
               isSoldout, isDatePassed, priceDiff, wishlistEntry.getCreatedTime(), wishlistEntry);
         final Iterator datePassedViewIterator = datePassedViewData.iterator();
         while (datePassedViewIterator.hasNext())
         {
            savedHolidayViewData.add((SavedHolidayViewData) datePassedViewIterator.next());
         }
      }

   }

   /**
    * @param savedHolidayViewData
    * @param wishlistEntry
    */
   private void populateSoldOutPackages(final List<SavedHolidayViewData> savedHolidayViewData,
      final WishlistEntry wishlistEntry, final String wishlistId)
   {
      final List<BasePackage> soldOutpackageModel = wishlistEntry.getNolongerAvailablePackageList();
      final List<BigDecimal> priceDiff = new ArrayList<BigDecimal>();
      final boolean isSoldout = true;
      final boolean isDatePassed = false;
      if (CollectionUtils.isNotEmpty(soldOutpackageModel))
      {
         final List<String> noLongerAvilableEntryId =
            wishlistEntry.getNolongerAvilableWishlistEntryId();

         List<SavedHolidayViewData> noLongerAvilableViewData;
         noLongerAvilableViewData =
            iterateAllPackageModel(soldOutpackageModel, noLongerAvilableEntryId, wishlistId,
               isSoldout, isDatePassed, priceDiff, wishlistEntry.getCreatedTime(), wishlistEntry);
         final Iterator noLongerAvilableViewIterator = noLongerAvilableViewData.iterator();
         while (noLongerAvilableViewIterator.hasNext())
         {
            savedHolidayViewData.add((SavedHolidayViewData) noLongerAvilableViewIterator.next());
         }
      }

   }

   private List<SavedHolidayViewData> iterateAllPackageModel(final List<BasePackage> packageModel,
      final List<String> wishlistEntryId, final String wishlistId, final boolean isSoldout,
      final boolean isDatePassed, final List<BigDecimal> priceDiff, final String createdDate,
      final WishlistEntry wishlistEntry)
   {
      final List<SavedHolidayViewData> packageViewDataList = new ArrayList<SavedHolidayViewData>();
      int i = 0;

      for (final BasePackage updatedPackageModel : packageModel)
      {
         BigDecimal priceDifference = null;
         if (i < priceDiff.size())
         {
            priceDifference = priceDiff.get(i);
         }
         try
         {
            packageViewDataList.add(renderPackageViewData(updatedPackageModel,
               wishlistEntryId.get(i), wishlistId, isSoldout, isDatePassed, priceDifference,
               createdDate, wishlistEntry));
         }
         catch (final ConversionException e)
         {
            LOGGER.error("Conversion exception" + e);

            throw new ConversionException(e.getMessage());
         }
         i = i + 1;
      }
      return packageViewDataList;
   }

   /**
    * Helper method to convert BasePackage object to PackageViewData object
    *
    * @param packageModel
    * @return packageViewData
    */
   private SavedHolidayViewData renderPackageViewData(final BasePackage packageModel,
      final String wishlistEntryId, final String wishlistId, final boolean isSoldout,
      final boolean isDatePassed, final BigDecimal priceDiff, final String createdDate,
      final WishlistEntry wishlistEntry) throws ConversionException
   {
      final SavedHolidayViewData savedHolidayViewData = new SavedHolidayViewData();
      try
      {
         savedHolidayViewDataPopulator.populate(packageModel, savedHolidayViewData, priceDiff,
            wishlistEntry);

         if (isSoldout)
         {
            savedHolidayViewData.setSoldoutWishlistEntryId(wishlistEntryId);
         }
         else if (isDatePassed)
         {
            savedHolidayViewData.setDatePassedWishlistEntryId(wishlistEntryId);
         }
         else
         {
            savedHolidayViewData.setWishlistEntryId(wishlistEntryId);
         }
         if (StringUtils.isNotBlank(createdDate))
         {
            savedHolidayViewData.setCreatedTime(createdDate);
         }

         savedHolidayViewData.setWishlistId(wishlistId);
      }
      catch (final ConversionException e)
      {
         LOGGER.error("Conversion exception" + e);

         throw new ConversionException(e.getMessage());
      }

      return savedHolidayViewData;
   }

   /**
    * To get shortlist count for Anonymous user
    *
    *
    */
   @Override
   public int getShortlistCount(final String wishlistId)
   {
      int shortlistCount = 0;
      shortlistCount = getShortlistedPackageIds(wishlistId).size();
      return shortlistCount;
   }

   /**
    * removes all the shortlists for Anonymous user
    *
    *
    */
   @Override
   public void clearAllShortlists(final String wishlistId)
   {
      defaultShortlistService.clearAllShortlists(wishlistId);

   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.tui.shortlist.facade.DefaultShortlistFacade#populateShortListFlag(uk.co.portaltech.tui
    * .web.view.data. SearchResultsViewData, java.lang.String)
    */
   @Override
   public void populateShortListFlag(final SearchResultsViewData searchResultsViewData,
      final String wishListId)
   {
      final Set<String> shortlistedHolidayIds = getShortlistedPackageIds(wishListId);
      if (shortlistedHolidayIds != null && searchResultsViewData.getHolidays() != null)
      {
         for (final SearchResultViewData holiday : searchResultsViewData.getHolidays())
         {
            if (shortlistedHolidayIds.contains(holiday.getPackageId()))
            {
               holiday.setShortlisted(true);
            }
         }
      }

   }

   @Override
   public void populateShortListFlag(final SearchResultViewData searchResultViewData,
      final String wishListId)
   {
      final Set<String> shortlistedHolidayIds = getShortlistedPackageIds(wishListId);

      if (shortlistedHolidayIds != null
         && shortlistedHolidayIds.contains(searchResultViewData.getPackageId()))
      {
         searchResultViewData.setShortlisted(true);
      }

   }

   /**
    * check whether the SAVEHOLIDAY_FC cookie is there or not. If there it returns cookieValue.
    *
    *
    */
   @Override
   public void checkCookieAndAddToShortlist(final HttpServletRequest request,
      final HttpServletResponse response, final String brandType)
   {
      String cookieValue = null;
      String wishlistId = null;
      final String packageId = request.getParameter("packageId");
      final Cookie[] cookies = request.getCookies();
      if (cookies != null)
      {
         for (final Cookie cookie : cookies)
         {
            if (StringUtils.equalsIgnoreCase(cookie.getName(), brandType))
            {
               cookieValue = cookie.getValue();
               break;
            }
         }
      }

      if (StringUtils.isNotBlank(cookieValue))
      {
         addToShortlist(cookieValue, packageId);
      }
      else
      {
         wishlistId = generatedWishlistId();
         addToShortlist(wishlistId, packageId);
         createAnonymousShortlistCookie(request, response, wishlistId, brandType);
      }
   }

   /**
    * add to shortlist method
    *
    * @param wishlistId
    */
   @Override
   public void addToShortlist(final String wishlistId, final String packageid)
   {
      ShortlistHolidayData sHoliday = new ShortlistHolidayData();

      final Map holidayMap = (Map) sessionService.getAttribute("holidayResult");

      if (holidayMap != null)
      {
         sHoliday = (ShortlistHolidayData) holidayMap.get(packageid);
      }

      // this logic has to be moved to a add queue
      final BasePackage inclusivePackage = new InclusivePackage();
      shortlistHolidayPackagePopulator.populate(sHoliday, inclusivePackage);
      inclusivePackage.setWishListEntryId(packageid);
      addToShortlistCache(inclusivePackage, wishlistId, packageid);
      updateInclusivePackageWithChildAges(inclusivePackage, sHoliday);
   }

   /**
    * This method populate child and infant ages if they are present but not available in
    * inclusivePackage.
    *
    * @param inclusivePackage
    * @param holiday
    */
   private void updateInclusivePackageWithChildAges(final BasePackage inclusivePackage,
      final ShortlistHolidayData holiday)
   {
      for (final Passenger passenger : inclusivePackage.getPassengers())
      {
         if (passenger.getAge() == null)
         {
            for (final ShortlistRoomData room : holiday.getAccomodation().getRoomDetails())
            {
               for (final ShortlistPaxDetails pax : room.getOccupancy().getPaxDetail())
               {
                  if (pax.getId() == passenger.getId().intValue())
                  {
                     passenger.setAge(pax.getAge());
                  }
               }
            }
         }
      }

   }

   /**
    * @param packageModel
    * @param wishlistId
    *
    *           This method will add the shortlisted holiday into the add cache and exist cache and
    *           removes it form remove cache based on the package ID and Wishlist Id
    *
    */
   private void addToShortlistCache(final BasePackage packageModel, final String wishlistId,
      final String packageid)
   {
      // code to fetch the cache for addition and removal of shortlisted holiday.
      final CacheWrapper<String, Map<String, BasePackage>> shortListAddCache =
         cacheUtil.getAddShortlistCacheWrapper();
      final CacheWrapper<String, Set<String>> shortListRemoveCache =
         cacheUtil.getRemoveShortlistCacheWrapper();
      // after the retrieving the cache we will call the below three methods to push and remove the
      // entried from cache.
      defaultShortlistService.addShortlistToAddCache(packageModel, wishlistId, packageid,
         shortListAddCache);
      defaultShortlistService.addShortlistToExistCache(packageModel, wishlistId, packageid);
      defaultShortlistService.removeFromRemoveCache(wishlistId, packageid, shortListRemoveCache);

   }

   /**
    * returns the generated wishlist id
    *
    * @return string
    */
   @Override
   public String generatedWishlistId()
   {
      return defaultShortlistService.generateWishListId();
   }

   /**
    * creating cookie for saved holiday for FC.
    *
    * @param request request
    * @param response response
    */
   @Override
   public void createAnonymousShortlistCookie(final HttpServletRequest request,
      final HttpServletResponse response, final String wishlistId, final String brandType)
   {
      cookieManagementService.createAnonymousShortlistCookie(request, response, wishlistId,
         brandType);

   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.customer.facade.DefaultShortlistFacade#getWishlists(java.lang.String)
    */
   @Override
   public List getSelectedHolidayPackageIdList(final String wishlistId)

   {
      List packageIdList = null;
      final HolidayWishListModel wishlistModel = defaultShortlistService.getWishlists(wishlistId);
      if (wishlistModel != null)
      {
         packageIdList = defaultShortlistService.getSelectedHolidayPackageIdList(wishlistModel);

      }

      return packageIdList;

   }

   /**
    *
    *
    * @param wishlistId wishlistId
    * @param packageId packageId
    *
    *           This method will remove shortlisted from the add and exist cache and add it to the
    *           remove cache.
    */
   @Override
   public void removeSavedHoliday(final String wishlistId, final String packageId)
   {
      defaultShortlistService.removeShortlistFromCache(wishlistId, packageId,
         cacheUtil.getAddShortlistCacheWrapper(), cacheUtil.getRemoveShortlistCacheWrapper());
   }

   /*
    * Remove the wishlist entries from pim for the given wishlist Id.
    */
   @Override
   public boolean removeSavedHolidayFromPim(final String wishlistId, final String packageid)
   {
      boolean status = false;
      // gets the holiday wish list entry model for the wishlist entry.
      final HolidayWishListModel holidayWishListModel =
         defaultShortlistService.getWishlists(wishlistId);

      if (holidayWishListModel != null)
      {
         // removes the wish list entries from PIm for the given wishlist entry.
         status =
            defaultShortlistService.removeHolidayWishListEntryFromPim(holidayWishListModel,
               packageid);

      }
      return status;
   }

   @Override
   public Set<String> getShortlistedPackageIds(final String wishListId)
   {
      return getPacakgeIdsFromCache(wishListId);
   }

   private Map<String, BasePackage> getWishlistsFromExistCache(final String wishlistId)
   {
      final DefaultShortlistService.MutableSessionMapWrapper wrapper =
         (DefaultShortlistService.MutableSessionMapWrapper) sessionService.getAttribute(wishlistId);
      if (SyntacticSugar.isNotNull(wrapper))
      {
         return wrapper.getMap();
      }
      return null;
   }

   private Set<String> getPackageIdsFromRemoveCache(final String wishlistId)
   {

      final CacheWrapper<String, Set<String>> removeCacheWrapper =
         cacheUtil.getRemoveShortlistCacheWrapper();

      if (removeCacheWrapper.get(wishlistId) != null)
      {

         return removeCacheWrapper.get(wishlistId);
      }

      return new HashSet<String>();

   }

   @Override
   public Set<String> getPacakgeIdsFromCache(final String wishlistId)
   {
      final Map<String, BasePackage> packageIdsFromExistCache =
         getWishlistsFromExistCache(wishlistId);
      final Set<String> removeCacheEntries = getPackageIdsFromRemoveCache(wishlistId);

      final Set<String> packageIdsFromPIM =
         new HashSet<String>(defaultShortlistService.getWishListEntryIdsFromPIM(wishlistId));
      if (packageIdsFromExistCache != null)
      {
         packageIdsFromPIM.addAll(packageIdsFromExistCache.keySet());
      }

      if (removeCacheEntries != null)
      {
         for (final String removeCacheEntry : removeCacheEntries)
         {
            packageIdsFromPIM.remove(removeCacheEntry);
         }
      }
      return packageIdsFromPIM;
   }

   private Set<String> getWishlistsFromRemoveCache(final String wishlistId)
   {

      final CacheWrapper<String, Set<String>> removeCacheWrapper =
         cacheUtil.getRemoveShortlistCacheWrapper();

      if (removeCacheWrapper.get(wishlistId) != null)
      {

         return removeCacheWrapper.get(wishlistId);
      }

      return new HashSet<String>();

   }

}
