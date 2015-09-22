/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.DestinationData;

/**
 * @author sureshbabu.rn
 *
 */
public class ProductDestinationConverter extends
   AbstractPopulatingConverter<AccommodationViewData, DestinationData>
{

   public static final int TWO = 2;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter# createTarget()
    */
   @Override
   public DestinationData createTarget()
   {

      return new DestinationData();
   }

   @Override
   public void populate(final AccommodationViewData source, final DestinationData target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      target.setId(source.getCode());
      target.setName(source.getName());
      target.setType(source.getAccommodationType());
      final Map<String, List<Object>> trating = source.getFeatureCodesAndValues();
      final Iterator iterator = trating.entrySet().iterator();
      while (iterator.hasNext())
      {
         final Map.Entry<String, List<Object>> entrySet =
            (Map.Entry<String, List<Object>>) iterator.next();
         if ("tRating".equals(entrySet.getKey()))
         {
            final List<Object> tRating = entrySet.getValue();
            if (tRating != null && !(tRating.isEmpty())
               && !("VILLA".equals(source.getAccommodationType())))
            {
               if (tRating.get(0).toString().length() > TWO)
               {
                  target.settRating(tRating.get(0).toString().replaceAll(" plus", "+"));
               }
               else
               {
                  target.settRating(tRating.get(0).toString());
               }
            }
         }
      }
   }
}
