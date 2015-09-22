/**
 *
 */
package uk.co.portaltech.tui.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.co.portaltech.tui.services.pojo.IntrasiteNavigation;


/**
 * @author akshatha.bb
 *
 */
public class THIntrasiteNavigationServiceTest
{
	private final THIntrasiteNavigationService thIntrasiteNavigationService = new THIntrasiteNavigationService();

	@Test
	public void getIntraSiteNavigationTest()
	{
		final IntrasiteNavigation result = thIntrasiteNavigationService.getIntraSiteNavigation(null);
		assertEquals("TH", result.getSite());
	}
}
