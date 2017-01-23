package com.endercrypt.cs2dspy.representation;

import java.io.IOException;

import com.endercrypt.cs2dspy.link.AccessSource;
import com.endercrypt.cs2dspy.link.SpyAccess;

public class SpyCs2dInfo
{
	private static final SpyCs2dInfo instance = new SpyCs2dInfo();

	public static SpyCs2dInfo get()
	{
		return instance;
	}

	private final String cs2dDirectory;

	public SpyCs2dInfo()
	{
		try (AccessSource source = SpyAccess.INFO.access())
		{
			cs2dDirectory = source.read();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public String getCs2dDirectory()
	{
		return System.getProperty("user.dir") + "/" + cs2dDirectory;
	}

	public String getCs2dDirectory(String subDirectory)
	{
		return getCs2dDirectory() + subDirectory;
	}
}
