/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.JsonTripadvisorFetchResults;
import uk.co.portaltech.tui.web.view.data.JsonadvisorUserRatingData;
import uk.co.portaltech.tui.web.view.data.JsonadvisorUserReviewData;



/**
 * @author Sreenivas.V
 *
 */
public class TripAdvisorPopulatorTest
{
	@InjectMocks
	private final TripAdvisorPopulator tripAdvisorPopulator = new TripAdvisorPopulator();
	final String Tripadvisorid = "316538";
	final String accomdationcode = "042165";
	private static final Integer maxUserReviews = new Integer(5);

	private static final String SUMMARY = "This was a wonderful hotel for geared towards children. There were eight of us in our party and we were put in next door rooms which worked very well. The rooms were very clean and the views lovely. T…";

	@Mock
	private final JsonTripadvisorFetchResults jsonTripadvisorResults = new JsonTripadvisorFetchResults();

	@Mock
	private ViewSelector viewselector;
	@Mock
	private Calendar publishedDate;
	@Mock
	private AccommodationService accommodationService;


	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		
	
	}
		
	@SuppressWarnings("boxing")
	@Test
	public void testGetTripAdvisorReviews()
	{
		
		final long l = 13213213;
		final Date date = new Date(l);
		final JsonadvisorUserRatingData jsondata = new JsonadvisorUserRatingData();
		
		final Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		final Integer userreviewcount = new Integer(503);
		
		final JsonadvisorUserReviewData userreviewdata = new JsonadvisorUserReviewData();
		final List<JsonadvisorUserReviewData> userreviews = new ArrayList<JsonadvisorUserReviewData>();
		jsondata.setAverageRating(new Double(5.0));
		jsondata.setRatingBar("http://www.tripadvisor.com/img/cdsi/img2/ratings/traveler/4.5-17552-5.png");
		jsondata.setReviewsCount(503);
		userreviewdata.setAuthor("Elizabeth440");
		userreviewdata.setAuthorID("186423");
		userreviewdata.setAuthorLocation("Great Malvern, United Kingdom");
		userreviewdata
				.setContent("This was a wonderful hotel for geared towards children. There were eight of us in our party and we were put in next door rooms which worked very well. The rooms were very clean and the views lovely. The family room was excellent");
		userreviewdata.setContentSummary(SUMMARY);
		userreviewdata.setPublishedDate(publishedDate.toString());
		userreviewdata.setRatingImage("http://www.tripadvisor.com/img/cdsi/img2/ratings/traveler/s5.0-17552-5.png");
		userreviewdata
				.setReviewsUrl("http://www.tripadvisor.com/ShowUserReviews-g297943-d316538-r207781468-m17552-Holiday_Village_Manar-Hammamet_Nabeul_Governorate.html#review207781468");
		userreviewdata.setTitle("Excellent for children");
		userreviews.add(userreviewdata);
		jsondata.setUserReviews(userreviews);
		
		
		Mockito.when(jsonTripadvisorResults.getTripAdvisorreviews(Tripadvisorid, maxUserReviews.intValue())).thenReturn(jsondata);
		
		
		Mockito.when(viewselector.checkIsMobile()).thenReturn(false);
		
		
		assertNotNull(jsondata);
		
		
		assertThat(
				jsondata.getUserReviews().get(0).getReviewsUrl(),
				is("http://www.tripadvisor.com/ShowUserReviews-g297943-d316538-r207781468-m17552-Holiday_Village_Manar-Hammamet_Nabeul_Governorate.html#review207781468"));
		assertThat(jsondata.getAverageRating(), is(new Double(5.0)));
		
		
		}
				
	@SuppressWarnings("boxing")
	@Test
	public void testGetTripAdvisorSummaryData()
	{
		
		final JsonadvisorUserRatingData jsonsummaryratingdata = new JsonadvisorUserRatingData();
		jsonsummaryratingdata.setAverageRating(new Double(5.0));
		jsonsummaryratingdata.setReviewsCount(503);
		jsonsummaryratingdata
				.setRatingReviewsUrl("http://www.tripadvisor.com/Hotel_Review-g297943-d316538-Reviews-m17552-Holiday_Village_Manar-Hammamet_Nabeul_Governorate.html");
		
		final Integer reviewcount = new Integer(503);
		Mockito.when(jsonTripadvisorResults.getTripAdvisorreviews(Tripadvisorid, maxUserReviews.intValue())).thenReturn(
				jsonsummaryratingdata);
		
		assertNotNull(jsonsummaryratingdata);
		
		assertThat(
				jsonsummaryratingdata.getRatingReviewsUrl(),
				is("http://www.tripadvisor.com/Hotel_Review-g297943-d316538-Reviews-m17552-Holiday_Village_Manar-Hammamet_Nabeul_Governorate.html"));
		assertThat(jsonsummaryratingdata.getAverageRating(), is(new Double(5.0)));
		assertThat(new Integer(jsonsummaryratingdata.getReviewsCount()), is(reviewcount));
		
		
	}
}
