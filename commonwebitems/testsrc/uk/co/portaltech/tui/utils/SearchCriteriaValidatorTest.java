/**
 *
 */
package uk.co.portaltech.tui.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.hamcrest.Matchers.is;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.web.view.data.ErrorData;


@UnitTest
public class SearchCriteriaValidatorTest {


	@InjectMocks
	 private  SearchCriteriaValidator validator=new SearchCriteriaValidator();

	  private SearchPanelComponentModel facilityModel;

	  private DeepLinkRequestTestData deepLinkRequestTestData;

	  private List<ErrorData> errors = new ArrayList<ErrorData>();


	     /** Holds the value for _COMPONENT_UID.*/
	   public static final String TEST_COMPONENT_UID = "WF_COM_300";

	 @Before
	    public void setUp() {
	        initMocks(this);
	      validator = new SearchCriteriaValidator();
	      deepLinkRequestTestData = new DeepLinkRequestTestData();
	  	  facilityModel = deepLinkRequestTestData.createDummyFacilityModel();

	    }


	
	@SuppressWarnings("boxing")
	@Test
	public void validateEmptySearchRequest() {
		errors = new ArrayList<ErrorData>();

		   validator.validate(deepLinkRequestTestData.searchRequestData(),  errors,facilityModel);
		   assertThat(errors.size(),is(Integer.valueOf(4)));
	}

	@SuppressWarnings("boxing")
	@Test
	public void validateIfWhenIsNUll() {
		errors = new ArrayList<ErrorData>();

		   validator.validate( deepLinkRequestTestData.searchRequestDataWithoutWhen(),   errors,facilityModel);
		   assertThat(errors.size(),is(Integer.valueOf(3)));
		   assertThat(errors.get(1).getCode(),is("INVALID_WHEN"));
	}

	@SuppressWarnings("boxing")
	@Test
	public void validateWhenHasEmpty() {
		errors = new ArrayList<ErrorData>();

		   validator.validate( deepLinkRequestTestData.searchRequestDataWithEmptyWhen(),  errors,facilityModel);
		   assertThat(errors.size(),is(Integer.valueOf(3)));
		   assertThat(errors.get(1).getCode(),is("INVALID_WHEN"));
	}

	@SuppressWarnings("boxing")
	@Test
	public void validateWhenHasInvalidDateFormat() {
		errors = new ArrayList<ErrorData>();

		   validator.validate( deepLinkRequestTestData.searchRequestDataWithInvalidWhen(),   errors,facilityModel);
		   assertThat(errors.size(),is(Integer.valueOf(3)));
		   assertThat(errors.get(1).getCode(),is("INVALID_WHEN_FORMAT"));
	}

	@SuppressWarnings("boxing")
	@Test
	public void validateWhenIsGreaterThanToday() {
		errors = new ArrayList<ErrorData>();

		   validator.validate( deepLinkRequestTestData.searchRequestDataWithWhenLessThanTodayDate(),   errors,facilityModel);
		   assertThat(errors.size(),is(Integer.valueOf(3)));
		   assertThat(errors.get(1).getCode(),is("INVALID_WHEN_DATE"));
	}

	@SuppressWarnings("boxing")
	@Test
	public void validatePartyCompositions() {
		errors = new ArrayList<ErrorData>();

		   validator.validate( deepLinkRequestTestData.searchRequestDataWithPartyComposition(),   errors,facilityModel);
		   assertThat(errors.size(),is(Integer.valueOf(2)));
		   assertThat(errors.get(1).getCode(),is("INVALID_PARTY_COMPOSITION_MAX"));
	}
	@SuppressWarnings("boxing")
	@Test
	public void validatePartyCompositionsMaxAdult() {
		errors = new ArrayList<ErrorData>();

		   validator.validate( deepLinkRequestTestData.searchRequestDataWithPartyCompositionForMaxAdults(),  errors,facilityModel);
		   assertThat(errors.size(),is(Integer.valueOf(2)));
		   assertThat(errors.get(1).getCode(),is("INVALID_PARTY_COMPOSITION_MAX"));
	}

	@SuppressWarnings("boxing")
	@Test
	public void validateWhenHasTamperedData() {
		   errors = new ArrayList<ErrorData>();
		   validator.validate( deepLinkRequestTestData.searchRequestDataWithTamperedDataInWhen(), errors,facilityModel);
		   assertThat(errors.size(),is(Integer.valueOf(3)));
		   assertThat(errors.get(0).getCode(),is("TAMPER_DATA_FOUND"));
	}
	
	


}
