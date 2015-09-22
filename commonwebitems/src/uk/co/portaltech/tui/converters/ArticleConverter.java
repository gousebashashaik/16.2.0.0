/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.travel.model.ArticleModel;
import uk.co.portaltech.tui.web.view.data.ArticleViewData;

/**
 * @author gagan
 *
 */
public class ArticleConverter extends AbstractPopulatingConverter<ArticleModel, ArticleViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public ArticleViewData createTarget()
   {
      return new ArticleViewData();
   }

}
