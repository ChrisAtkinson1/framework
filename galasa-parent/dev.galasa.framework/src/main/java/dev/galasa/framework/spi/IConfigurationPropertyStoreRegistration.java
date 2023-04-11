/*
 * Licensed Materials - Property of IBM
 * 
 * (c) Copyright IBM Corp. 2019.
 */
package dev.galasa.framework.spi;

import javax.validation.constraints.NotNull;

/**
 * 
 * @author James Davies
 *
 */
public interface IConfigurationPropertyStoreRegistration {

    /**
     * <p>
     * This method is called to selectively initialise the CPS. If this CPS is to be
     * initialise, it should register the CPS with @{link
     * {@link dev.galasa.framework.spi.IFrameworkInitialisation#registerConfigurationPropertyStore(IConfigurationPropertyStore)}
     * </p>
     * 
     * <p>
     * If there is any problem initialising the sole CPS, then an exception will be
     * thrown that will effectively terminate the Framework
     * </p>
     * 
     * @param frameworkInitialisation - Initialisation object containing access to
     *                                various initialisation methods
     * @throws ConfigurationPropertyStoreException - If there is a problem
     *                                             initialising the underlying store
     * @throws ResultArchiveStoreException
     */
    void initialise(@NotNull IFrameworkInitialisation frameworkInitialisation)
            throws ConfigurationPropertyStoreException, ResultArchiveStoreException;

}
