/*
 * Copyright contributors to the Galasa project
 */
package dev.galasa.framework.spi;

import java.util.List;

import javax.validation.constraints.NotNull;

import dev.galasa.framework.spi.ras.IRasSearchCriteria;
import dev.galasa.framework.spi.ras.RasTestClass;

/**
 * 
 * @author Michael Baylis
 *
 */
public interface IResultArchiveStoreDirectoryService {

    @NotNull
    String getName();

    boolean isLocal();
    
    @NotNull
    List<IRunResult> getRuns(@NotNull IRasSearchCriteria... searchCriteria) throws ResultArchiveStoreException;

    /**
     * Get requestors
     * 
     * @return 
     * @throws ResultArchiveStoreException if there are errors accessing the RAS
     */

    @NotNull
    List<String> getRequestors() throws ResultArchiveStoreException;

    @NotNull
    List<RasTestClass> getTests() throws ResultArchiveStoreException;
    
    @NotNull
    List<String> getResultNames() throws ResultArchiveStoreException;
    
    IRunResult getRunById(@NotNull String runId) throws ResultArchiveStoreException;
    
}
