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
import java.util.Map;

import javax.annotation.Resource;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.apache.log4j.Logger;

import uk.co.portaltech.cache.CacheWrapper;
import uk.co.portaltech.tui.utils.CacheUtil;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.shortlist.account.services.DefaultShortlistService;

/**
 * This class adds the short listed packages into PIM from add cache which will be called based on
 * the trigger time.
 *
 * @author niranjani.r
 *
 */
public class AddShortlistedToPIMJobPerformable extends AbstractJobPerformable<CronJobModel>
{

   /**
    * EhCache Manager
    */
   @Resource
   private CacheManager ehCacheManager;

   @Resource(name = "cacheUtil")
   private CacheUtil cacheUtil;

   @Resource
   private DefaultShortlistService defaultShortlistService;

   private static final String ADD_CACHE = "addShortlistCache";

   private static final Logger LOG = Logger.getLogger(AddShortlistedToPIMJobPerformable.class);

   /*
    * This Method takes all the short listed add cache entries and pushes it into DB and based on
    * the success flag we delete the entry from Add Cache. This method is triggered in a regular
    * interval and the interval is configured using cron trigger expression.
    */
   @Override
   public PerformResult perform(final CronJobModel arg0)
   {
      // Start of the Add Job . Getting all the cookie Ids from add cache and pushing it to pim
      LOG.info("Initializing the AddShortlistedToPIMJob ");
      final Ehcache ehCache = ehCacheManager.getEhcache(ADD_CACHE);
      final CacheWrapper<String, Map<String, BasePackage>> addCacheWrapper =
         cacheUtil.getAddShortlistCacheWrapper();
      // This block will iterate through all the wishlist ids and pushes it into DB(PIM).
      if (ehCache != null)
      {
         final List<String> wishListIds = ehCache.getKeys();
         for (final String wishList : wishListIds)
         {
            final Map<String, BasePackage> wishListEntries = addCacheWrapper.get(wishList);
            final Iterator wishListEntriesIterator = wishListEntries.entrySet().iterator();
            addShortListToDB(wishListEntriesIterator, wishList);
         }
      }
      LOG.info("Finished the AddShortlistedToPIMJob  Sucessfully");
      return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
   }

   /**
    * @param wishListEntriesIterator
    * @param wishList This methods add the short listed entry from add cache to PIM and returns a
    *           status . Based on the status (success or failure ) the entry is removed from add
    *           cache.
    *
    */
   private void addShortListToDB(final Iterator wishListEntriesIterator, final String wishList)
   {

      while (wishListEntriesIterator.hasNext())
      {
         final Map.Entry mapEntry = (Map.Entry) wishListEntriesIterator.next();
         // add the wish list entry to pim and returns success or failure status
         final boolean status =
            defaultShortlistService.addShortlistToPim((BasePackage) mapEntry.getValue(), wishList,
               mapEntry.getKey().toString());
         if (status)
         {
            wishListEntriesIterator.remove();
            LOG.info("Wishlist entry Id : " + mapEntry.getKey().toString()
               + "removed  sucessfully form add  cache for  the wishlist Id : " + wishList);
         }
      }
   }

}
