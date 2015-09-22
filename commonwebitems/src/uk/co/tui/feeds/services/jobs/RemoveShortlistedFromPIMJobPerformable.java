/**
 *
 */
package uk.co.tui.feeds.services.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.apache.log4j.Logger;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.tui.shortlist.facade.DefaultShortlistFacade;

/**
 * This class removes the wish list entry from DB for given trigger time and removes the cache entry
 * form remove cache for the successful removal.
 *
 * @author niranjani.r
 *
 */
public class RemoveShortlistedFromPIMJobPerformable extends AbstractJobPerformable<CronJobModel>
{
   private static final Logger LOG = Logger.getLogger(RemoveShortlistedFromPIMJobPerformable.class);

   /**
    * EhCache Manager
    */
   @Resource
   private CacheManager ehCacheManager;

   @Resource(name = "cacheUtil")
   private CacheUtil cacheUtil;

   @Resource
   private DefaultShortlistFacade defaultShortlistFacade;

   private static final String REMOVE_CACHE = "removeShortlistCache";

   /*
    * This Method takes all the short listed entries from remove cache and removes it from DB and
    * based on the success flag we delete the entry from remove Cache. This method is triggered in a
    * regular interval and the interval is configured using cron trigger expression.
    */
   @Override
   public PerformResult perform(final CronJobModel arg0)
   {
      // Start of the remove Job . Getting all the cookie Ids from remove cache and remove it from
      // Pim

      LOG.info("Initializing the RemoveShortlistedFromPIMJob ");
      final Ehcache ehCache = ehCacheManager.getEhcache(REMOVE_CACHE);
      final CacheWrapper<String, Set<String>> removeCacheWrapper =
         cacheUtil.getRemoveShortlistCacheWrapper();
      // This block will iterate through all the wishlist ids and removes it from PIM

      if (ehCache != null)
      {
         final List<String> wishListIds = ehCache.getKeys();
         for (final String wishList : wishListIds)
         {
            final Set<String> wishListEntries = removeCacheWrapper.get(wishList);
            final Iterator<String> wishListEntriesIterator = wishListEntries.iterator();

            removeShortListFromDB(wishListEntriesIterator, wishList);
         }
      }
      LOG.info("Finished the RemoveShortlistedFromPIMJob  Sucessfully");
      return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
   }

   /**
    * @param wishListEntriesSet
    * @param wishList This methods removes the short listed entry in remove cache from PIM and
    *           returns a status . Based on the status (success or failure ) the entry is removed
    *           from add cache.
    */
   private void removeShortListFromDB(final Iterator<String> wishListEntriesSet,
      final String wishList)
   {

      while (wishListEntriesSet.hasNext())
      {
         final String packageId = wishListEntriesSet.next();
         // remove the wish list entry from pim and returns success or failure status
         final boolean status =
            defaultShortlistFacade.removeSavedHolidayFromPim(wishList, packageId);
         if (status)
         {
            wishListEntriesSet.remove();
            LOG.info("Wishlist entry Id : " + packageId
               + "removed  sucessfully form remove   cache for  the wishlist Id : " + wishList);
         }
      }
   }

}
