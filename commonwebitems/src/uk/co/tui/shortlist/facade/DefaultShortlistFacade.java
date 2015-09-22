/**
 *
 */
package uk.co.tui.shortlist.facade;

import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import uk.co.tui.shortlist.account.exception.MaximumShortlistLimitExceededException;
import uk.co.tui.shortlist.view.data.SavedHolidayViewData;

import com.thoughtworks.xstream.converters.ConversionException;

/**
 * This class is used to perform add to shortlist functionality
 *
 * @author ramkishore.p
 *
 */

@Component
public interface DefaultShortlistFacade
{

   /**
    * This method is to read the cookie value based on brand.
    *
    * @param cookies
    * @param brandType
    * @return String
    */
   String getCookieValue(Cookie[] cookies, String brandType);

   /**
    * This method is to view Saved holiday List.
    *
    * @param wishlistId
    * @param sortBy
    */
   List<SavedHolidayViewData> viewShortlists(final String wishlistId) throws ConversionException;

   /**
    * This method is to delete wishlist entry.
    *
    * @param wishListEntryId
    */
   void deleteWishlistEntry(String wishListEntryId);

   /**
    * This method is to delete wishlist and its associated wishlist entries.
    *
    * @param wishListId
    */
   void deleteShortlist(String wishListId);

   /**
    * @param wishlistId
    * @return
    */
   int getShortlistCount(String wishlistId);

   /**
    * Removes all the shortlists for the user.
    *
    * @param wishListId
    */
   void clearAllShortlists(final String wishlistId);

   void populateShortListFlag(final SearchResultsViewData holidays, final String wishListId);

   /**
    * @param searchResultViewData
    * @param wishListId
    */
   void populateShortListFlag(SearchResultViewData searchResultViewData, String wishListId);

   /**
    * add to short list using the wishlistId.
    *
    * @param wishlistId
    * @param packageid
    * @throws MaximumShortlistLimitExceededException MaximumShortlistLimitExceededException
    */
   void addToShortlist(String wishlistId, String packageid)
      throws MaximumShortlistLimitExceededException;

   /**
    * generates the wishlistID.
    *
    * @return the string
    */
   String generatedWishlistId();

   /**
    * @param request
    * @param response
    * @param wishlistId
    */
   void createAnonymousShortlistCookie(HttpServletRequest request, HttpServletResponse response,
      String wishlistId, String brandType);

   /**
    * @param wishlistId
    */
   List getSelectedHolidayPackageIdList(String wishlistId);

   /**
    * @param wishlistId
    * @param packageid
    */
   void removeSavedHoliday(String wishlistId, String packageid);

   /**
    * @param request
    * @param response
    * @param holidayType
    * @param packageId
    * @return
    */
   void checkCookieAndAddToShortlist(HttpServletRequest request, HttpServletResponse response,
      String holidayType);

   /**
    * @param wishlistId
    * @param packageid
    * @return status
    */
   boolean removeSavedHolidayFromPim(String wishlistId, String packageid);

   /**
    * @param wishListId
    * @return set of package ids.
    */
   Set<String> getShortlistedPackageIds(String wishListId);

   /**
    * @param wishlistId
    * @return
    */
   Set<String> getPacakgeIdsFromCache(String wishlistId);
}
