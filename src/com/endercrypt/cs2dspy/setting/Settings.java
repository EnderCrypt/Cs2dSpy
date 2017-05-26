package com.endercrypt.cs2dspy.setting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
public class Settings
{
	private static final Settings instance = new Settings("Settings.txt");
	private static final boolean PRINT_SETTINGS = true;

	public static Settings get()
	{
		return instance;
	}

	private Map<String, Setting> settingsMap = new HashMap<>();

	private Settings(String settingsFile)
	{
		File file = new File(settingsFile);
		try (BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			println("Settings available:");
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				line = line.trim();
				if ((line.startsWith("//") == false) && (line.contains("=")))
				{
					Setting setting = new Setting(line);
					settingsMap.put(setting.getName().toLowerCase(), setting);
					println(setting.toString());
				}
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static void println(String message)
	{
		if (PRINT_SETTINGS)
			System.out.println(message);
	}

	public Setting key(String name)
	{
		return settingsMap.get(name.toLowerCase());
	}
}
