package com.endercrypt.cs2dspy.representation;

import java.io.IOException;

import com.endercrypt.cs2dspy.link.AccessSource;
import com.endercrypt.cs2dspy.link.SpyAccess;

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