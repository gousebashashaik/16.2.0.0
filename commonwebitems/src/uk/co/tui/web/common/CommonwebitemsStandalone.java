/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2011 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package uk.co.tui.web.common;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;

import org.apache.log4j.Logger;


/**
 * Demonstration of how to write a standalone application that can be run directly from within eclipse or from the
 * commandline.<br>
 * To run this from commandline, just use the following command:<br>
 * <code>
 * java -jar bootstrap/bin/ybootstrap.jar "new uk.co.tui.web.common.CommonwebitemsStandalone().run();"
 * </code> From eclipse, just run as Java Application. Note that you maybe need to add all other projects like
 * ext-commerce, ext-pim to the Launch configuration classpath.
 */
public class CommonwebitemsStandalone
{
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(CommonwebitemsStandalone.class);

    /**
     * Main class to be able to run it directly as a java program.
     *
     * @param args
     *           the arguments from commandline
     */
    public static void main(final String[] args)
    {
        new CommonwebitemsStandalone().run();
    }

    public void run()
    {
        Registry.activateStandaloneMode();
        Registry.activateMasterTenant();

        final JaloSession jaloSession = JaloSession.getCurrentSession();
        LOGGER.info("Session ID: " + jaloSession.getSessionID());
        //NOPMD
        LOGGER.info("User: " + jaloSession.getUser());
        //NOPMD
        Utilities.printAppInfo();

        RedeployUtilities.shutdown();
    }
}
