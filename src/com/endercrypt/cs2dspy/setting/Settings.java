package com.endercrypt.cs2dspy.setting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Settings
{
	private static final Settings instance = new Settings("Settings.txt");
	private static final boolean PRINT_SETTINGS = false;

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
			if (PRINT_SETTINGS)
				System.out.println("Settings available:");
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				line = line.trim();
				if ((line.startsWith("//") == false) && (line.contains("=")))
				{
					Setting setting = new Setting(line);
					settingsMap.put(setting.getName().toLowerCase(), setting);
					if (PRINT_SETTINGS)
						System.out.println(setting);
				}
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Setting key(String name)
	{
		return settingsMap.get(name.toLowerCase());
	}
}
