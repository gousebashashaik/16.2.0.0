/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.blog.BlogEntry;
import uk.co.portaltech.tui.web.view.data.BlogData;

/**
 * @author sureshbabu.rn
 *
 */
public class BlogDataPopulatorTest {

	BlogDataPopulator blogDataPopulator=new BlogDataPopulator();
	
	BlogEntry source;
	BlogData target;
	
	@Before
	public void setUp() throws Exception {

		source=new BlogEntry();
		target=new BlogData();
		
		source.setTitle("source");
		source.setLink("www.first.co.uk");
	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.populators.BlogDataPopulator#populate(uk.co.portaltech.travel.blog.BlogEntry, uk.co.portaltech.tui.web.view.data.BlogData)}.
	 */
	@Test
	public void testPopulate() {
		assertNotNull(source);
		assertNotNull(target);
		
		blogDataPopulator.populate(source, target);
		
		assertNotNull(target);
		
		assertEquals("source", target.getTitle());
		assertEquals("www.first.co.uk", target.getLink());
	}

}
