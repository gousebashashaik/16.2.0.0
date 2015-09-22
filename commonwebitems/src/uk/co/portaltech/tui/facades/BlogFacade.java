/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.BlogData;
import uk.co.portaltech.tui.web.view.data.BlogPostData;

/**
 * Facade interface to manage Thomson Blog
 *
 * @author s.consolino
 *
 */
public interface BlogFacade {

    /**
     * Retrieves informations about the feed of the Thomson Blog
     *
     * @param componentUID
     *            UID oh Blog Component
     * @return List of {@link BlogEntry}.
     */
    List<BlogData> getBlogEntries(String componentUID);
    BlogPostData getBlogPostEntries(String componentUID);
}
