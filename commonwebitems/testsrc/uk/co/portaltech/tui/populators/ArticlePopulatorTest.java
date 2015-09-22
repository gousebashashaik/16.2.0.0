/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.ArticleType;
import uk.co.portaltech.travel.model.ArticleModel;
import uk.co.portaltech.tui.web.view.data.ArticleViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

/**
 * @author sureshbabu.rn
 * 
 */
@SuppressWarnings("deprecation")
public class ArticlePopulatorTest {

	@InjectMocks
	private ArticlePopulator articlePopulator = new ArticlePopulator();

	@Mock
	private Converter<MediaModel, MediaViewData> mediaModelConverter;

	@Mock
	private Populator<MediaModel, MediaViewData> mediaViewDataPopulator;

	private ArticleModel articleModel;
	private ArticleViewData articleViewData;
	private MediaContainerModel mediaContainerModel;
	private ArticleType articleType;
	private MediaModel mediaModel;
	private Collection<MediaModel> mediaModelCollecion;
	private MediaViewData mediaViewData;
	private Collection<MediaViewData> mediaViewDataCollection;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		articleModel = new ArticleModel();
		articleViewData = new ArticleViewData();
		mediaContainerModel = new MediaContainerModel();
		mediaModel = new MediaModel();
		mediaModelCollecion = new ArrayList<MediaModel>();
		mediaViewData = new MediaViewData();
		mediaViewDataCollection = new ArrayList<MediaViewData>();

		mediaViewData.setCode("mediaview_code");

		mediaViewDataCollection.add(mediaViewData);

		mediaModel.setCode("media_code");

		mediaModelCollecion.add(mediaModel);

		mediaContainerModel.setMedias(mediaModelCollecion);

		articleModel.setContent("article_content");
		articleModel.setPublishDate(new Date());
		articleModel.setSource("article_source");
		articleModel.setTitle("article_title");
		articleModel.setUrl("www.first.co.uk");
		articleModel.setType(articleType);
		articleModel.setArticleImage(mediaContainerModel);

		articleViewData.setArticleImage(mediaViewDataCollection);
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.ArticlePopulator#populate(uk.co.portaltech.travel.model.ArticleModel, uk.co.portaltech.tui.web.view.data.ArticleViewData)}
	 * .
	 */
	@Test
	public void testPopulate() {
		Mockito.when(mediaModelConverter.convert(Mockito.any(MediaModel.class)))
				.thenReturn(mediaViewData);
		Mockito.doNothing()
				.when(mediaViewDataPopulator)
				.populate(Mockito.any(MediaModel.class),
						Mockito.any(MediaViewData.class));

		assertNotNull(articleModel);
		assertNotNull(articleViewData);

		articlePopulator.populate(articleModel, articleViewData);

		assertNotNull(articleModel);
		assertNotNull(articleViewData);

		assertEquals("article_content", articleViewData.getContent());
		assertEquals("article_source", articleViewData.getSource());
		assertEquals("article_title", articleViewData.getTitle());
		assertEquals("www.first.co.uk", articleViewData.getUrl());

		List<MediaViewData> actual = (List<MediaViewData>) articleViewData
				.getArticleImage();
		Iterator iterator = actual.iterator();
		while (iterator.hasNext()) {
			MediaViewData m = (MediaViewData) iterator.next();
			assertEquals("mediaview_code", m.getCode());
		}

	}

}
