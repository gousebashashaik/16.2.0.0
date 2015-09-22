/**
 * 
 */
package uk.co.portaltech.tui.facades;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.Registry;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.tui.facades.impl.DefaultBlogFacade;
import uk.co.portaltech.tui.web.view.data.BlogData;

/**
 * @author s.consolino
 * 
 */
@UnitTest
public class BlogFacadeTest {
    private BlogFacade          blogFacade;
    
    @Before
    public void setUp() {
        this.blogFacade = Registry.getApplicationContext().getBean("blogFacade", BlogFacade.class);
    }
    
    @Test
    public void testBlogFacade() {
        List<BlogData> listBlogData = blogFacade.getBlogEntries("blog_comp");
        for (final BlogData blogData : listBlogData) {
            
            System.out.println(blogData.toString());
        }
    }
}
