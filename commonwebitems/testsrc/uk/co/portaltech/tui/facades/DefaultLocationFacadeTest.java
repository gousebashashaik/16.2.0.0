/**
 *
 */
package uk.co.portaltech.tui.facades;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.converters.DestinationConverter;
import uk.co.portaltech.tui.converters.ProductDestinationConverter;
import uk.co.portaltech.tui.converters.ProductToDestinationConverter;
import uk.co.portaltech.tui.facades.impl.DefaultLocationFacade;
import uk.co.portaltech.tui.web.view.data.DestinationData;
import uk.co.portaltech.tui.web.view.data.SuggestionViewData;


/**
 * @author laxmibai.p
 * 
 */

public class DefaultLocationFacadeTest
{

	@Mock
	private ProductToDestinationConverter prodToDestConverter;

	@Mock
	private DestinationConverter destinationConverter;

	@Mock
	private ProductDestinationConverter converter;

	private SearchPanelComponentModel searchPanelComponent;
	private SearchPanelComponentModel searchPanelComponent2, searchPanelComponent3;

	private DefaultLocationFacade locationFacade;
	private List<LocationModel> mostpopulardestinations;
	private List<LocationModel> bestforbeaches;
	private List<LocationModel> bestforfamily;
	private List<ProductRangeModel> holidaytypes;

	private LocationModel locationModel1, locationModel2, locationModel3, locationModel4, locationModel5, locationModel6;
	private ProductRangeModel holidayType1, holidaytype2;
	private DestinationData destData1, destData2, destData;

	private static  String country1 = "IND";
	private static  String country2 = "ESP";
	@Mock
	private CatalogVersionModel catalogVersion;

	private List<DestinationData> destList;
	private DestinationData destinationData;
	private  int FIVE = 5;
	private  int FOUR = 4;
	private  int THREE = 3;
	private  int TWO = 2;
	private  int ONE = 1;
	private List<String> brandList = null;

	@Before
	public void setUp()
	{

		initMocks(this);

		locationFacade = new DefaultLocationFacade(destinationConverter, prodToDestConverter, converter);
		locationModel1 = new LocationModel();
		locationModel2 = new LocationModel();
		locationModel3 = new LocationModel();
		locationModel4 = new LocationModel();
		locationModel5 = new LocationModel();
		locationModel6 = new LocationModel();
		holidayType1 = new ProductRangeModel();
		holidaytype2 = new ProductRangeModel();
		destData = new DestinationData();
		destData1 = new DestinationData();
		destData2 = new DestinationData();
		destinationData = new DestinationData();

		mostpopulardestinations = new ArrayList<LocationModel>();
		bestforbeaches = new ArrayList<LocationModel>();
		bestforfamily = new ArrayList<LocationModel>();
		holidaytypes = new ArrayList<ProductRangeModel>();

		locationModel1.setCode(country1);
		locationModel1.setType(LocationType.COUNTRY);
		locationModel1.setCatalogVersion(catalogVersion);

		destData1.setName("INdia");

		destData1.setId(locationModel1.getCode());
		destData1.setType(locationModel1.getType().getCode());

		locationModel2.setCode(country2);
		locationModel2.setType(LocationType.DESTINATION);
		locationModel2.setCatalogVersion(catalogVersion);

		destData2.setName("SPAIN");
		destData2.setId(locationModel2.getCode());
		destData2.setType(locationModel2.getType().getCode());

		destList = new ArrayList<DestinationData>();
		destList.add(destData2);

		destList.add(destData1);

		mostpopulardestinations.add(locationModel1);
		mostpopulardestinations.add(locationModel2);

		bestforbeaches.add(locationModel1);
		bestforbeaches.add(locationModel2);
		bestforbeaches.add(locationModel3);
		bestforbeaches.add(locationModel4);
		bestforbeaches.add(locationModel5);
		bestforbeaches.add(locationModel6);

		bestforfamily.add(locationModel1);
		bestforfamily.add(locationModel2);

		holidaytypes.add(holidayType1);
		holidaytypes.add(holidaytype2);

		searchPanelComponent = new SearchPanelComponentModel();
		searchPanelComponent.setBestforbeaches(bestforbeaches);
		
		
		searchPanelComponent.setBestforfamily(bestforfamily);

		searchPanelComponent2 = new SearchPanelComponentModel();
		searchPanelComponent3 = new SearchPanelComponentModel();
		searchPanelComponent3.setBestforbeaches(bestforbeaches);
		brandList = new ArrayList<String>();
		brandList.add("TH");
		brandList.add("TH_FC");
	}

	@Test
	public void shouldInitializeAStableSpringContext()
	{
		assertThat(prodToDestConverter, is(notNullValue()));
		assertThat(destinationConverter, is(notNullValue()));
		assertThat(converter, is(notNullValue()));

	}

	@Test
	public void componentShouldReturnReccomendations()
	{
		assertThat(searchPanelComponent.getBestforfamily(), is(notNullValue()));
		assertThat(searchPanelComponent.getBestforbeaches(), is(notNullValue()));
		assertThat(searchPanelComponent.getMostpopulardestinations().getItems(), is(notNullValue()));
		assertThat(searchPanelComponent.getHolidaytypes(), is(notNullValue()));

	}

	@Test
	public void shouldReturnDestGuideViewData()
	{
		when(destinationConverter.convert(locationModel1, destData)).thenReturn(destData1);
		when(destinationConverter.convert(locationModel2, destData)).thenReturn(destData2);
		assertThat(locationFacade.fetchDestinationGuide(searchPanelComponent, null, null, null, "true", brandList),
				is(notNullValue()));
		assertThat(
				locationFacade.fetchDestinationGuide(searchPanelComponent, null, null, null, "true", brandList).getSuggestions(),
				is(notNullValue()));

	}

	@Test
	public void shouldReturnDestDataListForLocations()
	{

		final DestinationData destinationData = new DestinationData();
		when(destinationConverter.convert(locationModel1, destinationData)).thenReturn(destData1);
		when(destinationConverter.convert(locationModel2, destinationData)).thenReturn(destData1);

		assertThat(locationFacade.createDestinationList(bestforbeaches, brandList), is(notNullValue()));

	}

	@Test
	public void returnNoResultsIfNoRecommendations()
	{

		assertThat(locationFacade.fetchDestinationGuide(searchPanelComponent2, null, null, brandList, "true", brandList)
				.getSuggestions(), is(java.util.Collections.<SuggestionViewData> emptyList()));

	}

	@Test
	public void returnDestGuideDataIfAnyOneReccomendationNull()
	{

		when(destinationConverter.convert(locationModel1, destinationData)).thenReturn(destData1);
		when(destinationConverter.convert(locationModel2, destinationData)).thenReturn(destData2);
		assertThat(locationFacade.fetchDestinationGuide(searchPanelComponent3, null, null, brandList, "true", brandList),
				is(notNullValue()));

	}

	@Test
	public void returnOnlyFiveItemsForMoreThanFiveItemsGiven()
	{
		when(destinationConverter.convert(locationModel1, destData)).thenReturn(destData1);
		when(destinationConverter.convert(locationModel2, destData)).thenReturn(destData2);
		if (locationFacade.fetchDestinationGuide(searchPanelComponent, null, null, brandList, "true", brandList).getSuggestions()
				.get(0).getName() == "best For Beaches")
		{
			assertThat(locationFacade.fetchDestinationGuide(searchPanelComponent, null, null, brandList, "true", brandList)
					.getSuggestions().get(0).getChildren().size(), is(FIVE));
			assertThat(locationFacade.fetchDestinationGuide(searchPanelComponent, null, null, brandList, "true", brandList)
					.getSuggestions().get(0).getChildren().size(), not(is(FOUR)));
			assertThat(locationFacade.fetchDestinationGuide(searchPanelComponent, null, null, brandList, "true", brandList)
					.getSuggestions().get(0).getChildren().size(), not(is(THREE)));
			assertThat(locationFacade.fetchDestinationGuide(searchPanelComponent, null, null, brandList, "true", brandList)
					.getSuggestions().get(0).getChildren().size(), not(is(TWO)));
			assertThat(locationFacade.fetchDestinationGuide(searchPanelComponent, null, null, brandList, "true", brandList)
					.getSuggestions().get(0).getChildren().size(), not(is(ONE)));
		}

	}

	@Test
	public void returnAllItemsIfLessThanFiveItemsGiven()
	{
		when(destinationConverter.convert(locationModel1, destData)).thenReturn(destData1);
		when(destinationConverter.convert(locationModel2, destData)).thenReturn(destData2);
		if (locationFacade.fetchDestinationGuide(searchPanelComponent, null, null, brandList, "true", brandList).getSuggestions()
				.get(3).getName() == "holiday Tpes")
		{
			assertThat(locationFacade.fetchDestinationGuide(searchPanelComponent, null, null, brandList, "true", brandList)
					.getSuggestions().get(3).getChildren().size(), is(TWO));
			assertThat(locationFacade.fetchDestinationGuide(searchPanelComponent, null, null, brandList, "true", brandList)
					.getSuggestions().get(3).getChildren().size(), not(is(0)));
		}
	}

}
