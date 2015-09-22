/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.SEOEditorialTemplateModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.facades.impl.DefaultSEOEditorialTemplateFacade;
import uk.co.portaltech.tui.web.view.data.SEOEditorialTemplateViewData;


/**
 * @author arun.y
 * 
 */
@UnitTest
public class DefaultSEOEditorialTemplateFacadeTest
{

	@InjectMocks
	private final DefaultSEOEditorialTemplateFacade defaultSEOEditorialTemplateFacade = new DefaultSEOEditorialTemplateFacade();
	@Mock
	private FeatureService featureService;

	private SEOEditorialTemplateModel seoTemplateCoun, seoTemplateReg, seoTemplateDest, seoTemplateRes;
	private LocationModel locationCoun, locationReg, locationDest, locationRes;
	private List<CategoryModel> superCategory, superCategory1, superCategory2;
	private String countryName, regionName, destinationName, resortName;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		seoTemplateCoun = new SEOEditorialTemplateModel();
		seoTemplateReg = new SEOEditorialTemplateModel();
		seoTemplateDest = new SEOEditorialTemplateModel();
		seoTemplateRes = new SEOEditorialTemplateModel();
		locationCoun = new LocationModel();
		locationReg = new LocationModel();
		locationDest = new LocationModel();
		locationRes = new LocationModel();
		superCategory = new ArrayList<CategoryModel>();
		superCategory1 = new ArrayList<CategoryModel>();
		superCategory2 = new ArrayList<CategoryModel>();

		countryName = "Spain";
		regionName = "Andalusia";
		destinationName = "Costa De Almeria";
		resortName = "Roquetas De Mar";
	}

	private void testDataForCountry()
	{
		seoTemplateCoun.setCode("FAM-1");
		seoTemplateCoun.setSeoTitle("[Country]");
		seoTemplateCoun.setSeoSubtitle("[Country]");
		seoTemplateCoun.setSeoBody1("[Country]");
		seoTemplateCoun.setSeoBody2("[Country]");
		seoTemplateCoun.setMetaTitle("[Country]");
		seoTemplateCoun.setMetaKeywords("[Epic Code]");
		seoTemplateCoun.setMetaDescription("[Country] / [Epic Code]");
		locationCoun.setType(LocationType.COUNTRY);
		locationCoun.setCode("ESP");
		locationCoun.setBrands(Arrays.asList(BrandType.FC));
	}

	private void testDataForRegion()
	{
		seoTemplateReg.setCode("FAM-2");
		seoTemplateReg.setSeoTitle("[Region]");
		seoTemplateReg.setSeoSubtitle("[Region]");
		seoTemplateReg.setSeoBody1("[Country]");
		seoTemplateReg.setSeoBody2("[Country]");
		seoTemplateReg.setMetaTitle("[Epic Code]");
		seoTemplateReg.setMetaKeywords("[Country] / [Region]");
		seoTemplateReg.setMetaDescription("[Country] / [Region] / [Epic Code]");
		locationCoun.setType(LocationType.COUNTRY);
		locationCoun.setCode("ESP");
		locationCoun.setBrands(Arrays.asList(BrandType.FC));
		superCategory.add(locationCoun);
		locationReg.setType(LocationType.REGION);
		locationReg.setCode("002894");
		locationReg.setSupercategories(superCategory);
		locationReg.setBrands(Arrays.asList(BrandType.FC));
	}

	private void testDataForDestination()
	{
		seoTemplateDest.setCode("FAM-3");
		seoTemplateDest.setSeoTitle("[Destination]");
		seoTemplateDest.setSeoSubtitle("[Destination]");
		seoTemplateDest.setSeoBody1("[Region]");
		seoTemplateDest.setSeoBody2("[Country]");
		seoTemplateDest.setMetaTitle("[Epic Code]");
		seoTemplateDest.setMetaKeywords("[Country] / [Region] / [Destination]");
		seoTemplateDest.setMetaDescription("[Country] / [Region] / [Destination] / [Epic Code]");
		locationCoun.setType(LocationType.COUNTRY);
		locationCoun.setCode("ESP");
		locationCoun.setBrands(Arrays.asList(BrandType.FC));
		superCategory.add(locationCoun);
		locationReg.setType(LocationType.REGION);
		locationReg.setCode("002894");
		locationReg.setSupercategories(superCategory);
		locationReg.setBrands(Arrays.asList(BrandType.FC));
		superCategory1.add(locationReg);
		locationDest.setType(LocationType.DESTINATION);
		locationDest.setCode("000361");
		locationDest.setSupercategories(superCategory1);
		locationDest.setBrands(Arrays.asList(BrandType.FC));
	}

	private void testDataForResort()
	{
		seoTemplateRes.setCode("FAM-4");
		seoTemplateRes.setSeoTitle("[Resort]");
		seoTemplateRes.setSeoSubtitle("[Destination]");
		seoTemplateRes.setSeoBody1("[Region]");
		seoTemplateRes.setSeoBody2("[Country]");
		seoTemplateRes.setMetaTitle("[Epic Code]");
		seoTemplateRes.setMetaKeywords("[Country] / [Region] / [Destination] / [Resort]");
		seoTemplateRes.setMetaDescription("[Country] / [Region] / [Destination] / [Resort] / [Epic Code]");
		locationCoun.setType(LocationType.COUNTRY);
		locationCoun.setCode("ESP");
		locationCoun.setBrands(Arrays.asList(BrandType.FC));
		superCategory.add(locationCoun);
		locationReg.setType(LocationType.REGION);
		locationReg.setCode("002894");
		locationReg.setSupercategories(superCategory);
		locationReg.setBrands(Arrays.asList(BrandType.FC));
		superCategory1.add(locationReg);
		locationDest.setType(LocationType.DESTINATION);
		locationDest.setCode("000361");
		locationDest.setSupercategories(superCategory1);
		locationDest.setBrands(Arrays.asList(BrandType.FC));
		superCategory2.add(locationDest);
		locationRes.setType(LocationType.RESORT);
		locationRes.setCode("000364");
		locationRes.setSupercategories(superCategory2);
		locationRes.setBrands(Arrays.asList(BrandType.FC));
	}

	@Test
	public final void testResolveLocationNameForTemplateForCountry()
	{
		testDataForCountry();
		when(
				featureService.getFirstFeatureValueAsString(Mockito.anyString(), Mockito.any(ItemModel.class),
						Mockito.any(Date.class), Mockito.anyString())).thenReturn(countryName);
		final SEOEditorialTemplateViewData viewDataCoun = defaultSEOEditorialTemplateFacade.resolveLocationNameForTemplate(
				seoTemplateCoun, locationCoun, "TH");
		Assert.assertNotNull(viewDataCoun);
		Assert.assertTrue("COUNTRY".equals(locationCoun.getType().getCode()));
		Assert.assertEquals("Spain", viewDataCoun.getSeoTitle());
		Assert.assertEquals("Spain", viewDataCoun.getSeoSubtitle());
		Assert.assertEquals("Spain", viewDataCoun.getSeoBody1());
		Assert.assertEquals("Spain", viewDataCoun.getSeoBody2());
		Assert.assertEquals("Spain", viewDataCoun.getMetaTitle());
		Assert.assertEquals("ESP", viewDataCoun.getMetaKeywords());
		Assert.assertEquals("Spain / ESP", viewDataCoun.getMetaDescription());
	}

	@Test
	public final void testResolveLocationNameForTemplateForRegion()
	{
		testDataForRegion();
		when(
				featureService.getFirstFeatureValueAsString(Mockito.anyString(), Mockito.any(ItemModel.class),
						Mockito.any(Date.class), Mockito.anyString())).thenReturn(countryName).thenReturn(regionName);
		final SEOEditorialTemplateViewData viewDataReg = defaultSEOEditorialTemplateFacade.resolveLocationNameForTemplate(
				seoTemplateReg, locationReg, "TH");
		Assert.assertNotNull(viewDataReg);
		Assert.assertTrue("REGION".equals(locationReg.getType().getCode()));
		Assert.assertEquals("Andalusia", viewDataReg.getSeoTitle());
		Assert.assertEquals("Andalusia", viewDataReg.getSeoSubtitle());
		Assert.assertEquals("Spain", viewDataReg.getSeoBody1());
		Assert.assertEquals("Spain", viewDataReg.getSeoBody2());
		Assert.assertEquals("002894", viewDataReg.getMetaTitle());
		Assert.assertEquals("Spain / Andalusia", viewDataReg.getMetaKeywords());
		Assert.assertEquals("Spain / Andalusia / 002894", viewDataReg.getMetaDescription());
	}

	@Test
	public final void testResolveLocationNameForTemplateForDestination()
	{
		testDataForDestination();
		when(
				featureService.getFirstFeatureValueAsString(Mockito.anyString(), Mockito.any(ItemModel.class),
						Mockito.any(Date.class), Mockito.anyString())).thenReturn(countryName).thenReturn(regionName)
				.thenReturn(destinationName);
		final SEOEditorialTemplateViewData viewDataDest = defaultSEOEditorialTemplateFacade.resolveLocationNameForTemplate(
				seoTemplateDest, locationDest, "TH");
		Assert.assertNotNull(viewDataDest);
		Assert.assertTrue("DESTINATION".equals(locationDest.getType().getCode()));
		Assert.assertEquals("Costa De Almeria", viewDataDest.getSeoTitle());
		Assert.assertEquals("Costa De Almeria", viewDataDest.getSeoSubtitle());
		Assert.assertEquals("Andalusia", viewDataDest.getSeoBody1());
		Assert.assertEquals("Spain", viewDataDest.getSeoBody2());
		Assert.assertEquals("000361", viewDataDest.getMetaTitle());
		Assert.assertEquals("Spain / Andalusia / Costa De Almeria", viewDataDest.getMetaKeywords());
		Assert.assertEquals("Spain / Andalusia / Costa De Almeria / 000361", viewDataDest.getMetaDescription());
	}

	@Test
	public final void testResolveLocationNameForTemplateForResort()
	{
		testDataForResort();
		when(
				featureService.getFirstFeatureValueAsString(Mockito.anyString(), Mockito.any(ItemModel.class),
						Mockito.any(Date.class), Mockito.anyString())).thenReturn(countryName).thenReturn(regionName)
				.thenReturn(destinationName).thenReturn(resortName);
		final SEOEditorialTemplateViewData viewDataRes = defaultSEOEditorialTemplateFacade.resolveLocationNameForTemplate(
				seoTemplateRes, locationRes, "TH");
		Assert.assertNotNull(viewDataRes);
		Assert.assertTrue("RESORT".equals(locationRes.getType().getCode()));
		Assert.assertEquals("Roquetas De Mar", viewDataRes.getSeoTitle());
		Assert.assertEquals("Costa De Almeria", viewDataRes.getSeoSubtitle());
		Assert.assertEquals("Andalusia", viewDataRes.getSeoBody1());
		Assert.assertEquals("Spain", viewDataRes.getSeoBody2());
		Assert.assertEquals("000364", viewDataRes.getMetaTitle());
		Assert.assertEquals("Spain / Andalusia / Costa De Almeria / Roquetas De Mar", viewDataRes.getMetaKeywords());
		Assert.assertEquals("Spain / Andalusia / Costa De Almeria / Roquetas De Mar / 000364", viewDataRes.getMetaDescription());
	}
}