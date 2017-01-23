package com.endercrypt.cs2dspy.link;

import java.io.File;
import java.io.FileNotFoundException;

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
public enum SpyAccess
{
	WEAPONS("Weapons"),
	MAP("Map"),
	REALTIME("Realtime"),
	INFO("Info");

	private final static String extension = "link";
	private final static String directory = System.getProperty("user.dir") + "/access/";

	private String file;

	private SpyAccess(String file)
	{
		this.file = file;
	}

	public File getFile()
	{
		return new File(directory + file + "." + extension);
	}

	public AccessSource access() throws FileNotFoundException
	{
		return new AccessSource(getFile());
	}
}
