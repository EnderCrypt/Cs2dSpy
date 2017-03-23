package com.endercrypt.cs2dspy.network.update;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.endercrypt.cs2dspy.network.update.version.Version;
import com.endercrypt.cs2dspy.network.update.version.VersionError;

/**
 *	This file is part of Cs2dSpy and was created by Magnus Gunnarsson
 *
 *	Copyright (C) 2017  Magnus Gunnarsson (EnderCrypt)
 *
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class VersionManager
{
	private static VersionNumber currentVersion = new VersionNumber(0, 5);

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
