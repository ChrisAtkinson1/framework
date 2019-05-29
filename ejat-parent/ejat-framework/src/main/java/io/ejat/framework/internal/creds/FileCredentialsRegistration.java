package io.ejat.framework.internal.creds;

import java.net.URI;

import javax.validation.constraints.NotNull;

import org.osgi.service.component.annotations.Component;

import io.ejat.framework.spi.IFrameworkInitialisation;
import io.ejat.framework.spi.creds.CredentialsException;
import io.ejat.framework.spi.creds.ICredentialsStoreRegistration;

/**

 * 
 * @author Bruce Abbott
 */
@Component(service= {ICredentialsStoreRegistration.class})
public class FileCredentialsRegistration implements ICredentialsStoreRegistration {

    /**
	 * <p>This method registers this as the only Creds file.</p>
	 * 
	 * @param IFrameworkInitialisation
	 * @throws CredentialsStoreException
	 */
    @Override
    public void initialise(@NotNull IFrameworkInitialisation frameworkInitialisation) throws CredentialsException {
        try {
            URI creds = frameworkInitialisation.getCredentialsStoreUri();
            FileCredentialsStore fcs = new FileCredentialsStore(creds, frameworkInitialisation.getFramework());
            frameworkInitialisation.registerCredentialsStore(fcs);
        } catch (Exception e ) {
            throw new CredentialsException("Could not initialise Framework Property File CREDs", e);
        }
    }

}