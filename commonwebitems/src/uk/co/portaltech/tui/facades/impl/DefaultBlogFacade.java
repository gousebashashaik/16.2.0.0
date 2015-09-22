/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.Populator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.blog.BlogEntry;
import uk.co.portaltech.travel.blog.BlogPostEntry;
import uk.co.portaltech.travel.blog.BlogPostService;
import uk.co.portaltech.travel.blog.BlogService;
import uk.co.portaltech.tui.components.model.BlogComponentModel;
import uk.co.portaltech.tui.components.model.BlogPostComponentModel;
import uk.co.portaltech.tui.converters.BlogConverter;
import uk.co.portaltech.tui.facades.BlogFacade;
import uk.co.portaltech.tui.web.view.data.BlogData;
import uk.co.portaltech.tui.web.view.data.BlogPostData;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * Default implementation of the {@link BlogFacade} interface
 *
 * @author s.consolino
 *
 */
public class DefaultBlogFacade implements BlogFacade
{
    private static final TUILogUtils LOG = new TUILogUtils("DefaultBlogFacade");


    @Resource
    private CMSComponentService            cmsComponentService;

    @Resource
    private BlogService                    blogService;

    @Resource
    private BlogPostService blogPostService;

    @Resource
    private BlogConverter                  blogConverter;

    private Populator<BlogEntry, BlogData> blogDataPopulator;

    /**
     * @param blogDataPopulator
     *            the blogDataPopulator to set
     */
    public void setBlogDataPopulator(
            Populator<BlogEntry, BlogData> blogDataPopulator) {
        this.blogDataPopulator = blogDataPopulator;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.facades.BlogFacade#getBlogEntries()
     */
    @Override
    public List<BlogData> getBlogEntries(final String componentUID) {
        final List<BlogData> blogDatalist = new ArrayList<BlogData>();
        try {
            final BlogComponentModel blogComponentModel = (BlogComponentModel) cmsComponentService
                    .getAbstractCMSComponent(componentUID);
            final List<BlogEntry> blogEntryList = blogService.getBlogEntries(
                    blogComponentModel.getBlogFeedUrl(), blogComponentModel
                            .getBlogEntriesNumber().intValue());
            for (BlogEntry blogEntry : blogEntryList) {
                final BlogData blogData = blogConverter.convert(blogEntry);
                blogDataPopulator.populate(blogEntry, blogData);
                blogDatalist.add(blogData);
            }
        } catch (CMSItemNotFoundException exception) {
            LOG.error("Error during the retrieval of Blog Component", exception);
        }
        return blogDatalist;
    }

    @Override
    public BlogPostData getBlogPostEntries(final String componentUID) {

        BlogPostData blogPostData = new BlogPostData();

        try {
            if(componentUID !=null && !componentUID.isEmpty()){
            final BlogPostComponentModel blogPostComponentModel = (BlogPostComponentModel) cmsComponentService
                    .getAbstractCMSComponent(componentUID);
            final String feedUrl = blogPostComponentModel.getBlogFeedUrl();
            final String postUrl = blogPostComponentModel.getBlogPostUrl();

            final List<BlogPostEntry> blog = blogPostService
                    .getBlogEntries(feedUrl);
            BlogPostEntry data = new BlogPostEntry();
            if (blog != null && !blog.isEmpty()) {
                if (postUrl == null || postUrl.isEmpty()) {

                    data = blog.get(0);

                } else {
                    data = getPostData(blog, postUrl);
                }

            }
            blogPostData = getBlogPostData(blogPostComponentModel, data);
            }
        } catch (CMSItemNotFoundException e) {
            LOG.error("Error during the retrieval of Blog Component", e);
        }

        return blogPostData;
    }

    private BlogPostEntry getPostData(final List<BlogPostEntry> blog,
            final String url) {

        final List<BlogPostEntry> blogList = blog;
        final String postUrl = url;
        BlogPostEntry data = new BlogPostEntry();
        for (BlogPostEntry blogEntry : blogList) {
            if (blogEntry.getLink().equalsIgnoreCase(postUrl)) {
                data = blogEntry;
            }
        }
        return data;

    }

    private BlogPostData getBlogPostData(final BlogPostComponentModel model,
            final BlogPostEntry entry) {

        final BlogPostComponentModel blogPostComponentModel = model;
        final BlogPostEntry postEntry = entry;
        final BlogPostData blogPost = new BlogPostData();
        blogPost.setBlogTitle(blogPostComponentModel.getTitle());
        blogPost.setBlogColor(blogPostComponentModel.getColor().toString());
        blogPost.setBlogPostTitle(postEntry.getTitle());
        blogPost.setBlogPostLink(postEntry.getLink());
        blogPost.setBlogPostDescription(postEntry.getDescription());
        blogPost.setBlogDate(postEntry.getPubDate());
        blogPost.setPosition(blogPostComponentModel.getPosition().getCode());
        blogPost.setWidth(blogPostComponentModel.getWidth().getCode());

        return blogPost;
    }
}
