/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.HowLongDurationModel;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.enums.HowLongBrandType;

import com.googlecode.ehcache.annotations.Cacheable;

/**
 * @author kavita.na
 *
 */
public class DurationHowLongServiceImpl implements DurationHowLongService
{
   private static final String QUERY =
      "SELECT {d.pK} FROM {HowLongDuration as d} WHERE {d.brandType}=?brandType";

   private static final TUILogUtils LOG = new TUILogUtils("DurationHowLongServiceImpl");

   @Resource
   private FlexibleSearchService flexibleSearchService;

   @Override
   @Cacheable(cacheName = "howLongDurationCache")
   public List<HowLongDurationModel> getDurationForCode(final int code, final String brandType)
   {

      final List<HowLongDurationModel> durationList = getDurationModels(brandType);
      final List<HowLongDurationModel> durationListTemp = new ArrayList<HowLongDurationModel>();
      for (final HowLongDurationModel model : durationList)
      {
         if ((code == Integer.parseInt(model.getValue()))
            && (brandType.equalsIgnoreCase(model.getBrandType().toString())))
         {
            durationListTemp.add(model);
            return durationListTemp;
         }
      }

      return Collections.emptyList();

   }

   @Cacheable(cacheName = "howLongDurationCache")
   public List<HowLongDurationModel> getDurations(final String brandType)
   {

      return getDurationModels(brandType);
   }

   /**
    * @return
    */
   @SuppressWarnings("deprecation")
   private List<HowLongDurationModel> getDurationModels(final String brandType)
   {
      Collection<HowLongDurationModel> durationListCol = null;

      final List<HowLongDurationModel> durationList = new LinkedList<HowLongDurationModel>();

      try
      {

         final FlexibleSearchQuery fbsearchquery = new FlexibleSearchQuery(QUERY);
         if ("FC".equalsIgnoreCase(brandType))
         {
            fbsearchquery.addQueryParameter("brandType", HowLongBrandType.FC);
         }
         else if ("FJ".equalsIgnoreCase(brandType))
         {
            fbsearchquery.addQueryParameter("brandType", HowLongBrandType.FJ);
         }
         else
         {
            fbsearchquery.addQueryParameter("brandType", HowLongBrandType.TH);
         }

         durationListCol =
            flexibleSearchService.<HowLongDurationModel> search(fbsearchquery).getResult();
         durationList.addAll(durationListCol);

      }
      catch (final ModelNotFoundException e)
      {

         LOG.error("> Error while retreving the model ", e);

      }
      catch (final AmbiguousIdentifierException e)
      {
         LOG.error("> More Than one model found", e);

      }

      return sortByOrder(durationList);
   }

   private List<HowLongDurationModel> sortByOrder(
      final List<HowLongDurationModel> howLongDurationModelList)
   {
      Collections.sort(howLongDurationModelList, new Comparator<HowLongDurationModel>()
      {
         @SuppressWarnings("deprecation")
         @Override
         public int compare(final HowLongDurationModel model1, final HowLongDurationModel model2)
         {
            if (model1 != null && model2 != null && model1.getOrder() != null
               && model2.getOrder() != null)
            {
               return model1.getOrder().compareTo(model2.getOrder());
            }
            // it is not important if it greater or less than, just it is not equal
            return 0;
         }
      });
      return howLongDurationModelList;
   }
}
