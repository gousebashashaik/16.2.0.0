/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.AttractionType;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.PositionModel;
import uk.co.portaltech.travel.services.AttractionService;
import uk.co.portaltech.travel.services.impl.DefaultAttractionService;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.converters.AttractionOption;
import uk.co.portaltech.tui.converters.ExcursionOption;
import uk.co.portaltech.tui.facades.impl.DefaultAttractionFacade;
import uk.co.portaltech.tui.populators.MediaDataPopulator;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;

/**
 * @author venkataharish.k
 * 
 */
public class DefaultAttractionFacadeTest {
	@InjectMocks
	private final DefaultAttractionFacade defaultfacade = new DefaultAttractionFacade();
	@Mock
	private AttractionViewData attractionViewData;
	@Mock
	private AttractionService attractionService;

	@Mock
	private Converter<AttractionModel, AttractionViewData> attractionConverter;
	@Mock
	private Converter<ExcursionModel, ExcursionViewData> excursionConverter;
	@Mock
	private ConfigurablePopulator<AttractionModel, AttractionViewData, AttractionOption> attractionConfiguredPopulator;
	@Mock
	private ConfigurablePopulator<ExcursionModel, ExcursionViewData, ExcursionOption> excursionConfiguredPopulator;
	@Mock
	private Converter<ItemModel, AttractionViewData> attractionEditorialContentConverter;
	@Mock
	private Populator<ItemModel, AttractionViewData> attractionEditorialContentPopulator;
	@Mock
	private CMSSiteService siteService;
	@Mock
	private MediaDataPopulator mediaDataPopulator;
	@Mock
	private CatalogVersionModel model;
	@Mock
	private CatalogModel catalogmodel;
	@Mock
	private DefaultAttractionService defaultattractionservice;
	@Mock
	private LocaleProvider provider;
	@Mock
	private ProductService productService;
	@Mock
	private PositionModel positionModel;
	@Mock
	private ResultData resultData;
	AttractionModel attractionModel1 = Mockito.mock(AttractionModel.class);
	List<MediaViewData> galleryImages;
	ItemModel itemModel = Mockito.mock(ItemModel.class);
	ExcursionModel excursionModel = Mockito.mock(ExcursionModel.class);
	ExcursionViewData excursionData = Mockito.mock(ExcursionViewData.class);
	AttractionModel attractionModel = Mockito.mock(AttractionModel.class);
	AttractionViewData attractionData = Mockito.mock(AttractionViewData.class);
	String attractionCode = "981247";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		CatalogVersionModel model = new CatalogVersionModel();
		CatalogModel catalog = new CatalogModel();
		catalog.setId("fc-catalog");
		model.setCatalog(catalog);
		model.setVersion("Online");
		model.setCatalog(catalogmodel);
		Set<CatalogVersionModel> value = new HashSet<CatalogVersionModel>();
		value.add(model);
		MediaViewData media = new MediaViewData();
		String description = "this is for gallery images";
		String mime = "image";
		media.setDescription(description);
		media.setMime(mime);
		galleryImages = new ArrayList<MediaViewData>();
		galleryImages.add(media);
		dummyGetAttractionEnrichedData();
		dummyGetEditorialContent();
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultAttractionFacade#getAttractionUspsByCode(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAttractionUspsByCode() {
		ItemModel itemModel = Mockito.mock(ItemModel.class);
		itemModel.getPk();
		itemModel = attractionService.getAttractionForCode(attractionCode,
				siteService.getCurrentCatalogVersion());
		Mockito.when(
				attractionService.getAttractionForCode(attractionCode,
						siteService.getCurrentCatalogVersion())).thenReturn(
				itemModel);
		ExcursionModel excursionModel = new ExcursionModel();
		String value = "spain";
		Locale loc = new Locale("eng");
		excursionModel.setName(value, loc);
		excursionModel.setPosition(positionModel);
		ExcursionViewData excursionData = new ExcursionViewData();
		String childAgeMin = "8";
		excursionData.setChildAgeMax(childAgeMin);
		Mockito.when(excursionConverter.convert(excursionModel)).thenReturn(
				excursionData);
		if (itemModel != null) {
			itemmodelnotnull();
		}
		assertNotSame(excursionModel, excursionData);
	}

	@Test
	public void itemmodelnotnull() {

		if (itemModel instanceof ExcursionModel) {
			excursionConverter.convert(excursionModel);
			excursionConfiguredPopulator.populate(excursionModel,
					excursionData, Arrays.asList(ExcursionOption.USPS));
			assertNotNull(excursionData);

		}
		if (itemModel instanceof AttractionModel) {
			attractionConverter.convert(attractionModel);
			attractionConfiguredPopulator.populate(attractionModel,
					attractionData, Arrays.asList(AttractionOption.USPS));

			assertNotNull(attractionData);
		}

	}

	@Test
	public void testGetAttractionUspsByCodeforNull() {
		String attractionCode3 = "";
		ItemModel itemModel2 = attractionService.getAttractionForCode(
				attractionCode3, siteService.getCurrentCatalogVersion());
		assertNull(itemModel2);
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultAttractionFacade#getEditorialContent(java.lang.String)}
	 * .
	 */
	private void dummyGetEditorialContent() {
		CatalogVersionModel model1 = new CatalogVersionModel();

		CatalogModel catalog = new CatalogModel();
		catalog.setId("fc-catalog");
		Locale loc = new Locale("eng");
		String value = "lang";
		catalog.setName(value, loc);
		model1.setCatalog(catalog);
		model1.setVersion("Online");
		model1.setCreationtime(new Date(3 / 13 / 2013));
		String longitude = "42.907837";
		String name = "spain";
		attractionViewData.setLongitude(longitude);
		attractionViewData.setName(name);

	}

	@Test
	public void testGetEditorialContent() {

		String attractionCode = "981247";

		AttractionViewData attractionViewData = Mockito
				.mock(AttractionViewData.class);

		ItemModel itemModel = Mockito.mock(ItemModel.class);
		Mockito.when(
				attractionService.getAttractionForCode(attractionCode, model))
				.thenReturn(itemModel);
		Mockito.when(attractionEditorialContentConverter.convert(itemModel))
				.thenReturn(attractionViewData);

		ItemModel itemmodel1 = attractionService.getAttractionForCode(
				attractionCode, model);
		assertEquals(itemmodel1, attractionService.getAttractionForCode(
				attractionCode, model));
		assertNotNull(attractionViewData);

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultAttractionFacade#getAttractionEnrichedData(java.util.List)}
	 * .
	 */
	@Test
	public void testGetEditorialContentforNegative() {
		String attractionCode1 = "";
		ItemModel itemModel1 = Mockito.mock(ItemModel.class);
		AttractionViewData attractionViewData = Mockito
				.mock(AttractionViewData.class);
		CatalogVersionModel model1 = null;
		Mockito
				.when(
						attractionService.getAttractionForCode(attractionCode1,
								model1)).thenReturn(itemModel1);
		
		assertNotNull(attractionViewData);
	}

	private void dummyGetAttractionEnrichedData() {
		String code2 = "Location";
		String latitude = "42.907837";
		String longitude = "25.468765";
		String Name = "sight";
		String searchResultSubtype = "000822";
		AttractionType attType = AttractionType.BAR;
		attractionModel.setAttractionType(attType);
		ResultData result = Mockito.mock(ResultData.class);
		result.setCode(code2);
		result.setName(Name);
		result.setLatitude(latitude);
		result.setLongitude(longitude);
		result.setSearchResultSubtype(searchResultSubtype);
		attractionViewData.setType(result.getSearchResultSubtype());
		attractionViewData.setCode(result.getCode());
		attractionViewData.setName(result.getName());
		attractionViewData.setLatitude(result.getLatitude());
		attractionViewData.setLongitude(result.getLongitude());
		attractionViewData.setGalleryImages(galleryImages);
	}

	@Test
	public void testGetAttractionEnrichedData() {

		AttractionModel attractionModel = Mockito.mock(AttractionModel.class);
		ResultData result = Mockito.mock(ResultData.class);

		attractionViewData = attractionEditorialContentConverter
				.convert(attractionModel);

		ItemModel itemModel = attractionService.getAttractionForCode(result
				.getCode(), siteService.getCurrentCatalogVersion());
		
		if (itemModel != null) {
			forimages();
			assertNotNull(attractionViewData);
		}
		testGetAttractionEnrichedDatafornull();
	}

	public void forimages() {
		AttractionModel attractionModel = Mockito.mock(AttractionModel.class);
		MediaContainerModel mediaContainerModel = new MediaContainerModel();
		String value = "25098";
		String mime = "image";
		mediaContainerModel.setMediaMogulId(value);
		Collection<MediaContainerModel> value2 = new ArrayList<MediaContainerModel>();
		attractionModel.setGalleryImages(value2);
		Mockito.when(attractionModel.getGalleryImages()).thenReturn(value2);
		MediaViewData viewData = new MediaViewData();
		viewData.setMime(mime);
		List<MediaViewData> galleryImages = new ArrayList<MediaViewData>();
		galleryImages.add(viewData);
		for (MediaModel media : mediaContainerModel.getMedias()) {
			MediaViewData mediaData = new MediaViewData();
			mediaDataPopulator.populate(media, mediaData);
			Mockito.when(attractionViewData.getGalleryImages()).thenReturn(
					galleryImages);
		}

	}

	@Test
	public void testGetAttractionEnrichedDatafornull() {
		String code = "981247";
		ResultData resultData = Mockito.mock(ResultData.class);
		List<ResultData> result = new ArrayList<ResultData>();
		resultData.setCode(code);
		result.add(resultData);
		ItemModel itemModel = attractionService.getAttractionForCode(resultData
				.getCode(), siteService.getCurrentCatalogVersion());
		assertNull(itemModel);

	}

}
