/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.blog.BlogEntry;
import uk.co.portaltech.tui.web.view.data.BlogData;

/**
 * @author s.consolino
 *
 */
public class BlogDataPopulator implements Populator<BlogEntry, BlogData> {

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(BlogEntry source, BlogData target) throws ConversionException {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");

        target.setTitle(source.getTitle());
        target.setLink(source.getLink());
    }
}
