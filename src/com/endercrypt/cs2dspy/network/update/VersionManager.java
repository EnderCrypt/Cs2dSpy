package com.endercrypt.cs2dspy.network.update;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.endercrypt.cs2dspy.network.update.version.Version;
import com.endercrypt.cs2dspy.network.update.version.VersionError;

public class VersionManager
{
	private static VersionNumber currentVersion = new VersionNumber(0, 3);

	public static VersionNumber getCurrentVersion()
	{
		return currentVersion;
	}

	private static VersionManager instance = new VersionManager();

	public static VersionManager get()
	{
		return instance;
	}

	private ExecutorService executorService = Executors.newCachedThreadPool();

	private Future<VersionNumber> asyncDownloadProductionVersion()
	{
		System.out.println("Checking for update..");
		return executorService.submit(new UpdateCheckThread());
	}

	private VersionNumber blockingDownloadProductionVersion() throws ExecutionException
	{
		try
		{
			return asyncDownloadProductionVersion().get();
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Version blockingDownloadVersion()
	{
		VersionNumber versionNumber;
		try
		{
			versionNumber = blockingDownloadProductionVersion();
		}
		catch (ExecutionException e)
		{
			Throwable cause = e.getCause();
			cause.printStackTrace();
			return new VersionError(cause);
		}
		System.out.println("Production version: " + versionNumber);
		return currentVersion.compareTo(versionNumber);
	}
}
