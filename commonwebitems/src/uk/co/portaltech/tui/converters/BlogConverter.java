/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.travel.blog.BlogEntry;
import uk.co.portaltech.tui.web.view.data.BlogData;

/**
 * @author s.consolino
 *
 */
public class BlogConverter extends AbstractPopulatingConverter<BlogEntry, BlogData>
{

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter#populate(java
    * .lang.Object, java.lang.Object)
    */
   @Override
   public void populate(final BlogEntry source, final BlogData target)
   {
      super.populate(source, target);
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public BlogData createTarget()
   {
      return new BlogData();
   }

}
