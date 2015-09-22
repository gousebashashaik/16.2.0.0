/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.ExchangeRateModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.ExcursionPriceModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.WeatherModel;
import uk.co.portaltech.travel.model.WeatherTypeModel;
import uk.co.portaltech.travel.services.AttractionService;
import uk.co.portaltech.travel.services.ExchangeRateService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ExcursionFacade;
import uk.co.portaltech.tui.populators.ExcursionPricePopulator;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.PriceAndAvailabilityWrapper;

/**
 * @author arun.y
 *
 */
@UnitTest
public class ExcursionPricePopulatorTest {
	
	@InjectMocks
	private ExcursionPricePopulator excursion= new ExcursionPricePopulator();	
	@Mock
	private ExchangeRateService exchangeRateService;
	@Mock
	private AttractionService attractionService;
	@Mock
	private CMSSiteService siteService;
	@Mock
	private LocationService locationService;
	@Mock
	private ExcursionFacade excursionFacade;
	
	private ExcursionModel sourceModel;
	private ExcursionViewData targetData;
	private ExcursionViewData excursionViewData;
	private ExcursionPriceModel excursionPriceModel;
	private Set<ExcursionPriceModel> excursionPrice;
	private ExchangeRateModel exchangeRateModel;
	private CatalogVersionModel catalogVersionModel;
	private PriceAndAvailabilityWrapper priceAndAvailabilityWrapper;
	private ItemModel attraction;
	private LocationModel location;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		sourceModel = mock(ExcursionModel.class);
		targetData = new ExcursionViewData();
		excursionViewData = new ExcursionViewData();
		excursionPriceModel = new ExcursionPriceModel();
		excursionPrice = new HashSet<ExcursionPriceModel>();
		exchangeRateModel = new ExchangeRateModel();
		priceAndAvailabilityWrapper = new PriceAndAvailabilityWrapper();
		attraction = new ItemModel();
		location = new LocationModel();

		WeatherModel weatherModel = new WeatherModel();
		Set<LocationModel> locationModelSet = new HashSet<LocationModel>();
		WeatherTypeModel weatherTypeModel = new WeatherTypeModel();
		List<WeatherTypeModel> weatherTypeModelList = new ArrayList<WeatherTypeModel>();	
		
		// Setting Test Data for Test Case
 		
		BigDecimal child1 = BigDecimal.valueOf(20.00000000);
		BigDecimal adult1 = BigDecimal.valueOf(40.00000000);
		BigDecimal excgRate1 = BigDecimal.valueOf(1.16500000);
		
		excursionPriceModel.setChildPrice(child1);
		excursionPriceModel.setAdultPrice(adult1);
		excursionPriceModel.setCurrency("EUR");
		excursionPrice.add(excursionPriceModel);
		sourceModel.setExcursionPrices(excursionPrice);
		exchangeRateModel.setCurrencyExchangeRate(excgRate1);
		
		BigDecimal child2 = BigDecimal.valueOf(28.50000000);
		BigDecimal adult2 = BigDecimal.valueOf(57.00000000);
		BigDecimal excgRate2 = BigDecimal.valueOf(1.16500000);
		
		excursionPriceModel.setChildPrice(child2);
		excursionPriceModel.setAdultPrice(adult2);
		excursionPriceModel.setCurrency("EUR");
		excursionPrice.add(excursionPriceModel);
		sourceModel.setExcursionPrices(excursionPrice);
		exchangeRateModel.setCurrencyExchangeRate(excgRate2);
		
		BigDecimal child3 = BigDecimal.valueOf(23.00000000);
		BigDecimal adult3 = BigDecimal.valueOf(46.00000000);
		BigDecimal excgRate3 = BigDecimal.valueOf(1.16500000);
		
		excursionPriceModel.setChildPrice(child3);
		excursionPriceModel.setAdultPrice(adult3);
		excursionPriceModel.setCurrency("EUR");
		excursionPrice.add(excursionPriceModel);
		sourceModel.setExcursionPrices(excursionPrice);		
		exchangeRateModel.setCurrencyExchangeRate(excgRate3);
		
		excursionViewData.setChildPrice("19.99");
		excursionViewData.setFromPrice("39.50");
		excursionViewData.setCode("992713");
		excursionViewData.setName("Seville 1 Day");
		excursionViewData.setChildAgeMax("10");
		excursionViewData.setChildAgeMin("5");
		excursionViewData.setBookingUrl("http://excursions.firstchoice.co.uk/excursions-details/PRT/Portugal/000499/Algarve/992713/Seville-1-Day.aspx#cost");
		excursionViewData.setUrl("/holiday/attraction/Alvor/Seville-1-Day-992713");
		priceAndAvailabilityWrapper.setExcursion(excursionViewData);
				
		attraction.getItemtype();
		location.getItemtype();
		location.setExcursionPrices(excursionPrice);
		location.setType(LocationType.DESTINATION);
		location.setCode("001969");
		locationModelSet.add(location);
		weatherModel.setLocation(locationModelSet);
		weatherTypeModel.setWeather(weatherModel);
		weatherTypeModelList.add(weatherTypeModel);
		weatherModel.setWeatherTypes(weatherTypeModelList);
		location.setWeather(weatherModel);
			
	}
	
	@Test
	public void testPopulate() throws ConversionException
	{
		Mockito.when(sourceModel.getExcursionPrices()).thenReturn(excursionPrice);
		Mockito.when(CollectionUtils.get(sourceModel.getExcursionPrices(), 0)).thenReturn(excursionPrice);
		Mockito.when(exchangeRateService.getExchangeRate(Mockito.anyString(), Mockito.any(CatalogVersionModel.class))).thenReturn(exchangeRateModel);
		BigDecimal exchangeRate = exchangeRateModel.getCurrencyExchangeRate();
		Assert.assertNotNull(exchangeRate);
		BigDecimal adultPrice=null;
		BigDecimal childPrice=null;
		for(ExcursionPriceModel price : sourceModel.getExcursionPrices())
        {
        	if(adultPrice==null) {
        		adultPrice = price.getAdultPrice(); 
                }else if(price.getAdultPrice().compareTo(adultPrice)<0) {
                	adultPrice = price.getAdultPrice();
                }
                
            if(childPrice==null) {
               	childPrice = price.getChildPrice(); 
                }else if(price.getChildPrice().compareTo(childPrice)<0) {
                	childPrice = price.getChildPrice();
                }
        }
		excursion.populate(sourceModel, targetData);
		Assert.assertNotNull(sourceModel);
		Assert.assertNotNull(targetData);
		Assert.assertEquals(targetData.getChildPrice(),"19.99");
		Assert.assertEquals(targetData.getFromPrice(),"39.50");
	}
	
	@Test
	public void testCurrencyCodeIsNull() 
	{
		String currencyCode = null;
		Mockito.when(sourceModel.getExcursionPrices()).thenReturn(excursionPrice);
		Mockito.when(CollectionUtils.get(sourceModel.getExcursionPrices(), 0)).thenReturn(excursionPrice);
		Assert.assertNull(exchangeRateService.getExchangeRate(currencyCode, catalogVersionModel));
	}
		
	@Test
	public void testGetExcursionViewDataForPriceAndAvailability() throws NoSuchComponentException
	{		
		String productCode = "992713";		
		priceAndAvailabilityWrapper.getExcursion();
		Assert.assertFalse(productCode.isEmpty());
		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalogVersionModel);
		Mockito.when(attractionService.getAttractionForCode(productCode,catalogVersionModel)).thenReturn(attraction);
		Assert.assertNotNull(attraction);
		Mockito.when(locationService.getLocationForItem(attraction, null)).thenReturn(location);
		Assert.assertNotNull(location);
		Mockito.when(excursionFacade.getLowsetPriceExcursionForLocation(location, attraction)).thenReturn(priceAndAvailabilityWrapper);
		Assert.assertNotNull(priceAndAvailabilityWrapper);
		Assert.assertEquals(priceAndAvailabilityWrapper.getExcursion(), excursionViewData);
		Assert.assertEquals(excursionViewData.getChildAgeMax(), "10");
		Assert.assertEquals(excursionViewData.getChildAgeMin(), "5");
		Assert.assertEquals(excursionViewData.getCode(), "992713");
		Assert.assertEquals(excursionViewData.getName(), "Seville 1 Day");
	}
	
	@Test
	public void testProductCodeIsEmpty()
	{
		String pCode = "";
		Assert.assertTrue(pCode.isEmpty());
		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalogVersionModel);
		Assert.assertNull(attractionService.getAttractionForCode(pCode,catalogVersionModel));
	}
}