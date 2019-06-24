package dev.voras.framework.spi;

import java.util.concurrent.ScheduledExecutorService;

public interface IResourceManagement {
	
	ScheduledExecutorService getScheduledExecutorService();
	
	void resourceManagementRunSuccessful();
}