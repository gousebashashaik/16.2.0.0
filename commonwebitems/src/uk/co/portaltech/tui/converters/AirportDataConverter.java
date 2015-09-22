/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AirportGroupModel;
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.tui.web.view.data.AirportData;

/**
 * @author laxmibai.p
 *
 */
public class AirportDataConverter extends AbstractPopulatingConverter<AirportModel, AirportData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public AirportData createTarget()
   {

      return new AirportData();
   }

   @Override
   public void populate(final AirportModel source, final AirportData target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      final Set<AirportGroupModel> airportGroups = source.getAirportgroup();
      final List<String> groups = getGroups(airportGroups);

      target.setId(source.getCode());
      target.setName(source.getName());
      target.setSynonym(source.getSynonyms());
      target.setGroup(groups);

   }

   private List<String> getGroups(final Set<AirportGroupModel> airportGroups)
   {
      final List<String> groups = new ArrayList<String>();
      for (final AirportGroupModel airportGroupModel : airportGroups)
      {
         groups.add(airportGroupModel.getCode());
      }
      return groups;
   }
}
