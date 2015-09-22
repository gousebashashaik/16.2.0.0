/**
 *
 */
package uk.co.portaltech.tui.utils;



import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import org.junit.Before;
import org.junit.Test;
import uk.co.portaltech.tui.web.view.data.FlightOptionRequestData;


/**
 * @author deepakkumar.k
 *
 */
@IntegrationTest
public class FlightOptionRequestBuilderTest extends ServicelayerTransactionalTest
{
   private FlightOptionRequestData request;
   private static final int  FOUR=4;
   private static final int  SEVEN=7;
   private static final int  NINE=9;
   private static final int  SEVENTEEN=17;

	/**
	 * Test method for {@link uk.co.portaltech.tui.utils.FlightOptionRequestBuilder#builder(uk.co.portaltech.tui.web.view.data.FlightOptionRequestData)}.
	 */
   /**
	 *
	 * setting the data before testing
	 * 	 */
	@SuppressWarnings("boxing")
	@Before
	public void setUp()
	{



	request= new FlightOptionRequestData();
	request.setAct(SEVENTEEN);
	request.setAttrstr("||||||||null|null");
	request.setBc(SEVENTEEN);
	request.setBoardBasisPackageNumber("AI|003222BLPCDS136151490000013615287000004500136213710000013621434000004501");
	request.setDac("LN");
	request.setDay("22");
	request.setDepm(SEVEN);
	request.setDess("TRUE");
	request.setDta(false);
	request.setDtx(0);
	request.setDur(SEVEN);
	request.setDurT("7/w");
	request.setDxsel(0);
	request.setJsen(true);
	request.setLoct(0);
	request.setLs(false);
	request.setMargindt(SEVEN);
	request.setMnth("2");
	request.setMps(NINE);
	request.setMthyr("02/2013");
	request.setNumr("2");
	request.setPconfig(" 1|2|0|2|2|16-14-/");
	request.setPid("003222BLPCDS136151490000013615287000004500136213710000013621434000004501");
	request.setRating(0);
	request.setSd("26/04/2013");
	request.setSda("TRUE");
	request.setTadt(FOUR);
	request.setTchd(2);
	request.setTinf(2);
	request.setTsnr(2);
	request.setTuidesc("CMS227");
	request.setYear("2013");

	}



	/*@Test
	public void testBuilder(){
	String url=	FlightOptionRequestBuilder.builder(request);
	 assertThat(url, is("http://www.firstchoice.co.uk/fcsun/page/flightoptions/flightoptions.page?" +
	 		"sttrkr=boardBasisPackageNumber:AI|003222BLPCDS136151490000013615287000004500136213710000013621434000004501" +
	 		"_mthyr:02/2013_durT:7/w_ls:false_tuidesc:CMS227_day:22_mps:9_sda:TRUE_" +
	 		"pconfig: 1|2|0|2|2|16-14-/_tchd:2_rating:0_dess:TRUE_act:17_jsen:true_" +
	 		"attrstr:||||||||null|null_tinf:2_mnth:2_bc:17_margindt:7_tadt:4_numr:2_" +
	 		"depm:7_dur:7_dtx:0_dxsel:0_dac:LN_loct:0_tsnr:2_year:2013_dta:false_" +
	 		"pid:003222BLPCDS136151490000013615287000004500136213710000013621434000004501_" +
	 		"sd:26/04/2013"));
	}*/

}
