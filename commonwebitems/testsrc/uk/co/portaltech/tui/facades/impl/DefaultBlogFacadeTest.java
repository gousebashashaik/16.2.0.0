/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.Populator;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.blog.BlogEntry;
import uk.co.portaltech.travel.blog.BlogPostEntry;
import uk.co.portaltech.travel.blog.BlogPostService;
import uk.co.portaltech.travel.blog.BlogService;
import uk.co.portaltech.tui.components.model.BlogComponentModel;
import uk.co.portaltech.tui.components.model.BlogPostComponentModel;
import uk.co.portaltech.tui.converters.BlogConverter;
import uk.co.portaltech.tui.facades.impl.DefaultBlogFacade;
import uk.co.portaltech.tui.web.view.data.BlogData;
import uk.co.portaltech.tui.web.view.data.BlogPostData;
import uk.co.tui.web.common.enums.BlogColor;
import uk.co.tui.web.common.enums.PositionType;
import uk.co.tui.web.common.enums.Width;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author niranjani.r
 *
 */
@UnitTest
public class DefaultBlogFacadeTest
{
	
	private final TUILogUtils LOG = new TUILogUtils("DefaultBlogFacadeTest");

	@InjectMocks
	final private DefaultBlogFacade blog = new DefaultBlogFacade();

	@Mock
	private CMSComponentService cmsComponentService;

	@Mock
	private BlogPostService blogPostService;

	@Mock
	private BlogService blogService;

	@Mock
	private BlogConverter blogConverter;
	@Mock
	private Populator<BlogEntry, BlogData> blogDataPopulator;
	@Mock
	private BlogEntry entry1, entry2;
	@Mock
	private BlogData blogData1, blogData2;

	private static final String THOMSONFEEDURL = "http://www.thomson.co.uk/blog/feed/";

	private static final String FIRSTCHOICEFEEDURL = "http://www.firstchoice.co.uk/blog/feed/";
	public static final String TEST_COMPONENT_UID = "WF_COM_200-1";
	private static final int ENTRY_NUM = 3;
	private static final String BLOG_LINK = "http://www.thomson.co.uk/blog/2013/03/thomson-airways-introduces-new-extra-legroom-seats/";
	private static final String BLOG_TITLE = "Thomson Airways Introduces New Extra Legroom Seats";
	private static final String BLOG_LINK1 = "http://www.thomson.co.uk/blog/2013/03/a-different-side-to-spain/";
	private static final String BLOG_TITLE1 = "A Different Side To Spain";
	final private BlogPostData blogPost = new BlogPostData();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		entry1 = new BlogEntry();
		entry1.setLink(BLOG_LINK);
		entry1.setTitle(BLOG_TITLE);

		entry2 = new BlogEntry();
		entry2.setLink(BLOG_LINK1);
		entry2.setTitle(BLOG_TITLE1);
	}

	private List<BlogData> createBlogData()
	{
		blogData1 = new BlogData();
		blogData1.setLink(BLOG_LINK);
		blogData1.setTitle(BLOG_TITLE);

		blogData2 = new BlogData();
		blogData2.setLink(BLOG_LINK1);
		blogData2.setTitle(BLOG_TITLE1);

		final List<BlogData> data = new ArrayList<BlogData>();
		data.add(blogData1);
		data.add(blogData2);

		return data;
	}

	@SuppressWarnings("boxing")
	private BlogComponentModel createDummyBlogComponeneModel()
	{
		final BlogComponentModel blogComponentModel = new BlogComponentModel();
		blogComponentModel.setUid(TEST_COMPONENT_UID);
		blogComponentModel.setBlogFeedUrl(THOMSONFEEDURL);
		blogComponentModel.setBlogEntriesNumber(ENTRY_NUM);
		return blogComponentModel;
	}

	private BlogPostComponentModel getDummyComponentModelWithEmptyComponent()
	{
		final BlogPostComponentModel blog = new BlogPostComponentModel();
		blog.setUid("");
		return blog;

	}

	private BlogPostComponentModel getDummyComponentModelWithEmptyPostUrl()
	{
		final BlogPostComponentModel blog = new BlogPostComponentModel();
		blog.setUid("blog");
		blog.setName("Blog Component model");
		blog.setTitle(" Thomson blog");
		blog.setBlogFeedUrl(THOMSONFEEDURL);
		blog.setBlogPostUrl("");
		blog.setColor(BlogColor.BLUE);
		blog.setPosition(PositionType.HOMEPAGE_FIRST);
		blog.setWidth(Width.HOMEPAGE_FULL);
		return blog;

	}

	private BlogPostComponentModel getDummyComponentModelWithThomsonPostUrl()
	{
		final BlogPostComponentModel blog = new BlogPostComponentModel();
		blog.setUid("blog");
		blog.setName("Blog Component model");
		blog.setTitle(" Thomson blog");
		blog.setBlogFeedUrl(THOMSONFEEDURL);
		blog.setBlogPostUrl("link2");
		blog.setColor(BlogColor.BLUE);
		blog.setPosition(PositionType.HOMEPAGE_FIRST);
		blog.setWidth(Width.HOMEPAGE_FULL);
		return blog;

	}

	private BlogPostComponentModel getDummyComponentModelWithFirstChoicePostUrl()
	{
		final BlogPostComponentModel blog = new BlogPostComponentModel();
		blog.setUid("blog");
		blog.setName("Blog Component model");
		blog.setTitle(" Thomson blog");
		blog.setBlogFeedUrl("http://www.firstchoice.co.uk/blog/feed/");
		blog.setBlogPostUrl("link2");
		blog.setColor(BlogColor.BLUE);
		blog.setPosition(PositionType.HOMEPAGE_FIRST);
		blog.setWidth(Width.HOMEPAGE_FULL);
		return blog;

	}

	private List<BlogPostEntry> getDummyBlogPostEntry()
	{
		final BlogPostEntry blog = new BlogPostEntry();
		blog.setDescription(" blog 1 ");
		blog.setLink("link1");

		final BlogPostEntry blog1 = new BlogPostEntry();
		blog1.setDescription(" blog 2 ");
		blog1.setLink("link2");

		final List<BlogPostEntry> blogList1 = new ArrayList<BlogPostEntry>();
		blogList1.add(blog);
		blogList1.add(blog1);

		return blogList1;
	}

	@Test
	public void testGetBlogEntries() throws CMSItemNotFoundException
	{
		final List<BlogEntry> list = new ArrayList<BlogEntry>();
		list.add(entry1);
		list.add(entry2);

		final List<BlogData> blogData = createBlogData();
		final BlogComponentModel blogModel = createDummyBlogComponeneModel();

		Mockito.when(cmsComponentService.getAbstractCMSComponent(TEST_COMPONENT_UID)).thenReturn(blogModel);
		Mockito.when(blogService.getBlogEntries(THOMSONFEEDURL, ENTRY_NUM)).thenReturn(list);
		Mockito.when(blogConverter.convert(entry1)).thenReturn(blogData1);
		blogDataPopulator.populate(entry1, blogData1);
		list.add(entry1);


		final List<BlogData> expectedResult = blog.getBlogEntries(TEST_COMPONENT_UID);

		assertThat(expectedResult.get(0).getLink(), is(blogData.get(0).getLink()));
		assertThat(expectedResult.get(0).getTitle(), is(blogData.get(0).getTitle()));


	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.facades.impl.DefaultBlogFacade#getBlogPostEntries(java.lang.String)} .
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testBlogPostEntriesWithoutComponentModel()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent("")).thenReturn(getDummyComponentModelWithEmptyComponent());
			final BlogPostData blogPostData = blog.getBlogPostEntries("");
			assertThat(blogPostData.getBlogPostTitle(), is(blogPost.getBlogPostTitle()));
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(" Could not find component with the name blog component");

		}

	}

	@Test
	public void testBlogPostEntriesWithEmptyPostUrl()
	{

		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent("blog")).thenReturn(getDummyComponentModelWithEmptyPostUrl());
			Mockito.when(blogPostService.getBlogEntries(THOMSONFEEDURL)).thenReturn(getDummyBlogPostEntry());
			final BlogPostData blogPostData = blog.getBlogPostEntries(getDummyComponentModelWithEmptyPostUrl().getUid());
			assertThat(blogPostData.getBlogPostLink(), is("link1"));

		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(" Could not find component with the name blog component");
		}
	}

	@Test
	public void testBlogPostEntriesWithPostUrl()
	{

		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent("blog")).thenReturn(getDummyComponentModelWithThomsonPostUrl());
			Mockito.when(blogPostService.getBlogEntries(THOMSONFEEDURL)).thenReturn(getDummyBlogPostEntry());
			final BlogPostData blogPostData = blog.getBlogPostEntries(getDummyComponentModelWithThomsonPostUrl().getUid());
			assertThat(blogPostData.getBlogPostLink(), is("link2"));

		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(" Could not find component with the name blog component");
		}
	}

	@Test
	public void testBlogPostEntriesWithFirstChoicePostUrl()
	{

		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent("blog")).thenReturn(
					getDummyComponentModelWithFirstChoicePostUrl());
			Mockito.when(blogPostService.getBlogEntries(FIRSTCHOICEFEEDURL)).thenReturn(getDummyBlogPostEntry());
			final BlogPostData blogPostData = blog.getBlogPostEntries(getDummyComponentModelWithFirstChoicePostUrl().getUid());
			assertThat(blogPostData.getBlogPostLink(), is("link2"));

		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(" Could not find component with the name blog component");
		}
	}
}
