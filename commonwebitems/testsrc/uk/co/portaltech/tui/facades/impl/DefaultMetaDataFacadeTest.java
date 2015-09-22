/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.enums.MetaDataRuleApplicableType;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.MetaDataRuleModel;
import uk.co.portaltech.travel.model.metadatarule.lite.MetaDataRuleLite;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.MetaDataGenerateService;
import uk.co.portaltech.tui.components.model.AccommodationPageModel;
import uk.co.portaltech.tui.components.model.AttractionPageModel;
import uk.co.portaltech.tui.components.model.LocationPageModel;
import uk.co.portaltech.tui.facades.impl.DefaultMetaDataFacade;
import uk.co.portaltech.tui.web.view.data.MetaDataViewData;
import uk.co.tui.web.common.enums.AccommodationPageType;
import uk.co.tui.web.common.enums.AttractionPageType;
import uk.co.tui.web.common.enums.LocationPageType;


/**
 * @author niranjani.r
 * 
 */
@IntegrationTest
public class DefaultMetaDataFacadeTest extends ServicelayerTransactionalTest
{

	@InjectMocks
	private final DefaultMetaDataFacade facade = new DefaultMetaDataFacade();
	@Mock
	private MetaDataGenerateService metaDataGenerateService;
	@Mock
	private FeatureService featureService;
	@Resource
	private ModelService modelService;

	/**
	 * value holder for location.
	 */
	private LocationModel location, location1;
	/**
	 * value holder for catalog version
	 */
	private CatalogVersionModel catalogVersion;
	/**
	 * value holder for location.
	 */
	private LocationPageModel page, page1;
	/**
	 * value holder for all metaDatarules.
	 */
	private List<MetaDataRuleLite> metaList, metaList1, metaList2, metaList3, metaList4, metaList5, metaList6;
	/**
	 * value holder for products.
	 */
	private ProductModel product;
	/**
	 * value holder for attractions.
	 */
	private AttractionModel attraction;
	/**
	 * value holder for excursion.
	 */
	private ExcursionModel excursion;
	/**
	 * value holder for attraction page
	 */
	private AttractionPageModel attractionPage;
	/**
	 * value holder for metadatarule.
	 */
	private MetaDataRuleLite metaData, metaData1, metaData2, metaData3, metaData4, metaData5, metaData6, metaData7;
	/**
	 * value holder for accommodation page
	 */
	private AccommodationPageModel accomPage, accomPage1;
	@SuppressWarnings("boxing")
	private static final Boolean ACTIVE = true;

	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);
		// create catalogModel and catalog version.
		final CatalogModel catalogModel;
		catalogModel = modelService.create(CatalogModel.class);
		catalogModel.setId("fc_catalog");

		catalogModel.setDefaultCatalog(ACTIVE);
		modelService.save(catalogModel);

		catalogVersion = new CatalogVersionModel();
		catalogVersion.setVersion("Online");
		catalogVersion.setActive(ACTIVE);
		catalogVersion.setCatalog(catalogModel);
		modelService.save(catalogVersion);

		// create dummy page and page template
		createDummyPageAndPageTemplateModel();

		// create dummy location model
		createDummyLocationData();

		// create dummy metadatarule for locations
		createDummyMetaDataRulesForLocation();

		// create dummy metadatarules for accommodation
		createDummyMetaDataRulesForAccommodation();

		// create dummy product model
		createDummyProductModel();

		// create dummy metadata rules for attractions
		createDummyMetaDataRulesForAttraction();

		// list of metadata rules for each of the location ,accommodation and attractions.
		metaList = new ArrayList<MetaDataRuleLite>();
		metaList.add(metaData);

		metaList1 = new ArrayList<MetaDataRuleLite>();
		metaList1.add(metaData);
		metaList1.add(metaData1);

		metaList2 = new ArrayList<MetaDataRuleLite>();
		metaList2.add(metaData2);
		metaList2.add(metaData3);

		metaList3 = new ArrayList<MetaDataRuleLite>();
		metaList3.add(metaData4);

		metaList4 = new ArrayList<MetaDataRuleLite>();
		metaList4.add(metaData4);
		metaList4.add(metaData5);

		metaList5 = new ArrayList<MetaDataRuleLite>();
		metaList5.add(metaData6);

		metaList6 = new ArrayList<MetaDataRuleLite>();
		metaList6.add(metaData7);

	}

	private void createDummyMetaDataRulesForLocation()
	{
		metaData = modelService.create(MetaDataRuleModel.class);
		metaData.setCode("countryDefault");



		metaData.setMetaDescriptionTemplate("country default template");
		modelService.save(metaData);

		metaData1 = modelService.create(MetaDataRuleModel.class);
		metaData1.setCode("countryOverview-ESP");



		metaData1.setMetaDescriptionTemplate(" Country code Template");
		modelService.save(metaData1);

		metaData2 = modelService.create(MetaDataRuleModel.class);
		metaData2.setCode("countryThingsToDo-ESP");



		metaData2.setMetaDescriptionTemplate(" Country things to do  Template");
		modelService.save(metaData2);

		metaData3 = modelService.create(MetaDataRuleModel.class);
		metaData3.setCode("countryThingsToDo");



		metaData3.setMetaDescriptionTemplate("country things to do  template");
		modelService.save(metaData3);

	}

	private void createDummyMetaDataRulesForAccommodation()
	{
		metaData4 = modelService.create(MetaDataRuleModel.class);
		metaData4.setCode("accommodationDefault");



		metaData4.setMetaDescriptionTemplate("Accommodation default page");
		modelService.save(metaData4);

		metaData5 = modelService.create(MetaDataRuleModel.class);
		metaData5.setCode("accommodationFacilities-007854");



		metaData5.setMetaDescriptionTemplate("Accommodation facilities page");
		modelService.save(metaData5);
	}

	private void createDummyMetaDataRulesForAttraction()
	{
		metaData6 = modelService.create(MetaDataRuleModel.class);
		metaData6.setCode("attractionDefault");



		metaData6.setMetaDescriptionTemplate("Attraction default  page");
		modelService.save(metaData6);

		metaData7 = modelService.create(MetaDataRuleModel.class);
		metaData7.setCode("ExcursionDefault");



		metaData7.setMetaDescriptionTemplate("Excusion default  page");
		modelService.save(metaData7);

	}

	private void createDummyLocationData()
	{
		location = modelService.create(LocationModel.class);
		location.setCode("ESP");
		location.setType(LocationType.COUNTRY);
		location.setCatalogVersion(catalogVersion);
		modelService.save(location);

		location1 = modelService.create(LocationModel.class);
		location1.setCode("GRC");
		location1.setType(LocationType.COUNTRY);
		location1.setCatalogVersion(catalogVersion);
		modelService.save(location1);
	}

	private void createDummyProductModel()
	{
		product = modelService.create(ProductModel.class);
		product.setCode("007854");
		product.setCatalogVersion(catalogVersion);
		modelService.save(product);

		attraction = modelService.create(AttractionModel.class);
		attraction.setCode("980477");
		attraction.setCatalogVersion(catalogVersion);
		modelService.save(attraction);

		excursion = modelService.create(ExcursionModel.class);
		excursion.setCode("995178");
		excursion.setCatalogVersion(catalogVersion);
		modelService.save(excursion);

	}

	private void createDummyPageAndPageTemplateModel()
	{
		final PageTemplateModel pageTemplate = modelService.create(PageTemplateModel.class);
		pageTemplate.setUid("LocationOverview_template1");
		pageTemplate.setCatalogVersion(catalogVersion);
		pageTemplate.setActive(ACTIVE);

		final PageTemplateModel pageTemplate1 = modelService.create(PageTemplateModel.class);
		pageTemplate1.setUid("LocationThingsToDo_template2");
		pageTemplate1.setCatalogVersion(catalogVersion);
		pageTemplate1.setActive(ACTIVE);

		page = modelService.create(LocationPageModel.class);
		page.setPageType(LocationPageType.OVERVIEW);
		page.setUid("countryOverviewPage");
		page.setMasterTemplate(pageTemplate);
		modelService.save(page);

		page1 = modelService.create(LocationPageModel.class);
		page1.setPageType(LocationPageType.THINGSTODO);
		page1.setUid("countryThingsToDoPage");
		page1.setMasterTemplate(pageTemplate1);
		modelService.save(page1);

		accomPage = modelService.create(AccommodationPageModel.class);
		accomPage.setPageType(AccommodationPageType.OVERVIEW);
		accomPage.setUid("");
		accomPage.setMasterTemplate(pageTemplate1);
		modelService.save(accomPage);

		accomPage1 = modelService.create(AccommodationPageModel.class);
		accomPage1.setPageType(AccommodationPageType.FACILITIES);
		accomPage1.setUid("accomfacilities");
		accomPage1.setMasterTemplate(pageTemplate1);
		modelService.save(accomPage1);

		attractionPage = modelService.create(AttractionPageModel.class);
		attractionPage.setUid("attraction Page");
		attractionPage.setPageType(AttractionPageType.OVERVIEW);
		attractionPage.setMasterTemplate(pageTemplate1);
		modelService.save(attractionPage);

	}

	@Test
	public void testforLocationPageWithDefaultMetaDataRule()
	{
		Mockito.when(metaDataGenerateService.getMetaDataForItem(location)).thenReturn(metaList);
		Mockito.when(featureService.getFirstFeatureValueAsString("name", location, new Date(), null)).thenReturn("Country");
		Mockito.when(featureService.getFirstFeatureValueAsString("name", location, new Date(), null)).thenReturn("parent");
		final MetaDataViewData actualView = facade.getMetaDataForItem(location, page);
		assertThat(actualView.getMetaDescription(), is(metaData.getMetaDescriptionTemplate()));

	}

	@Test
	public void testForLocationPageWithCountryCodeMetaDataRule()
	{
		Mockito.when(metaDataGenerateService.getMetaDataForItem(location)).thenReturn(metaList1);
		Mockito.when(featureService.getFirstFeatureValueAsString("name", location, new Date(), null)).thenReturn("Country");
		Mockito.when(featureService.getFirstFeatureValueAsString("name", location, new Date(), null)).thenReturn("parent");
		final MetaDataViewData actualView = facade.getMetaDataForItem(location, page);
		assertThat(actualView.getMetaDescription(), is(metaData1.getMetaDescriptionTemplate()));
	}

	@Test
	public void testForLocationPageWithoutCountryCodeMetaDataRule()
	{
		Mockito.when(metaDataGenerateService.getMetaDataForItem(location1)).thenReturn(metaList2);
		Mockito.when(featureService.getFirstFeatureValueAsString("name", location1, new Date(), null)).thenReturn("Country");
		Mockito.when(featureService.getFirstFeatureValueAsString("name", location1, new Date(), null)).thenReturn("parent");
		final MetaDataViewData actualView = facade.getMetaDataForItem(location1, page1);
		assertThat(actualView.getMetaDescription(), is(metaData3.getMetaDescriptionTemplate()));
	}

	@Test
	public void testForAccommodationWithDefaultMetaDataRule()
	{
		Mockito.when(metaDataGenerateService.getMetaDataForItem(product)).thenReturn(metaList3);
		Mockito.when(featureService.getFirstFeatureValueAsString("name", product, new Date(), null)).thenReturn("accommodation");
		Mockito.when(featureService.getFirstFeatureValueAsString("name", product, new Date(), null)).thenReturn("parent");
		final MetaDataViewData actualView = facade.getMetaDataForItem(product, accomPage);
		assertThat(actualView.getMetaDescription(), is(metaData4.getMetaDescriptionTemplate()));
	}

	@Test
	public void testForAccommodationWithCodeMetaDataRule()
	{
		Mockito.when(metaDataGenerateService.getMetaDataForItem(product)).thenReturn(metaList4);
		Mockito.when(featureService.getFirstFeatureValueAsString("name", product, new Date(), null)).thenReturn("accommodation");
		Mockito.when(featureService.getFirstFeatureValueAsString("name", product, new Date(), null)).thenReturn("parent");
		final MetaDataViewData actualView = facade.getMetaDataForItem(product, accomPage1);
		assertThat(actualView.getMetaDescription(), is(metaData5.getMetaDescriptionTemplate()));
	}

	@Test
	public void testForAttractionWithDefaultMetaDataRule()
	{
		Mockito.when(metaDataGenerateService.getMetaDataForItem(attraction)).thenReturn(metaList5);
		Mockito.when(featureService.getFirstFeatureValueAsString("name", attraction, new Date(), null)).thenReturn("attraction");
		Mockito.when(featureService.getFirstFeatureValueAsString("name", attraction, new Date(), null)).thenReturn("parent");
		final MetaDataViewData actualView = facade.getMetaDataForItem(attraction, attractionPage);
		assertThat(actualView.getMetaDescription(), is(metaData6.getMetaDescriptionTemplate()));
	}

	@Test
	public void testForExcursionWithDefaultMetaDataRule()
	{
		Mockito.when(metaDataGenerateService.getMetaDataForItem(excursion)).thenReturn(metaList6);
		Mockito.when(featureService.getFirstFeatureValueAsString("name", excursion, new Date(), null)).thenReturn("Excusion");
		Mockito.when(featureService.getFirstFeatureValueAsString("name", excursion, new Date(), null)).thenReturn("parent");
		final MetaDataViewData actualView = facade.getMetaDataForItem(excursion, attractionPage);
		assertThat(actualView.getMetaDescription(), is(metaData7.getMetaDescriptionTemplate()));
	}

	@Test
	public void testForEmptyMetaDataRule()
	{

		String flag = null;
		final List<MetaDataRuleLite> meta = new ArrayList<MetaDataRuleLite>();
		Mockito.when(metaDataGenerateService.getMetaDataForItem(excursion)).thenReturn(meta);
		Mockito.when(featureService.getFirstFeatureValueAsString("name", excursion, new Date(), null)).thenReturn("Excusion");
		Mockito.when(featureService.getFirstFeatureValueAsString("name", excursion, new Date(), null)).thenReturn("parent");
		final MetaDataViewData actualView = facade.getMetaDataForItem(excursion, attractionPage);
		if (actualView.getMetaDescription() == null)
		{
			flag = "true";
		}
		assertThat(flag, is("true"));
	}

}
