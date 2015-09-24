/**
 *
 */
package uk.co.portaltech.tui.flights.facades.impl;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import javax.annotation.Resource;

import uk.co.portaltech.tui.flights.facades.FlyingWithUsFacade;
import uk.co.tui.flights.service.impl.DefaultFlyingWithUsService;


/**
 * @author vijaya.putheti
 *
 */
public class DefaultFlyingWithUsFacade implements FlyingWithUsFacade
{

	@Resource
	private DefaultFlyingWithUsService flyingWithUsService;

	@Override
	public AbstractPageModel getPageModel(final String subPageType)
	{
		return flyingWithUsService.getPageModel(subPageType);
	}
}
