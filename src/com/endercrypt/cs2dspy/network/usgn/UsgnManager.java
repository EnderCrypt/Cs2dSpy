package com.endercrypt.cs2dspy.network.usgn;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.endercrypt.cs2dspy.setting.Settings;

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
public class UsgnManager
{
	private Map<Integer, UsgnInfoTracker> usgnInfoStorage = new HashMap<>();

	private static boolean fetchExternalUsgnData = Settings.get().key("Cs2d.FetchExternalUsgnData").getBoolean();

	public Optional<UsgnInfo> get(int usgn)
	{
		if (fetchExternalUsgnData == false)
		{
			return Optional.empty();
		}
		if (usgn > 0)
		{
			UsgnInfoTracker usgnTracker = usgnInfoStorage.get(usgn);
			if (usgnTracker == null)
			{
				usgnTracker = new UsgnInfoTracker(usgn);
				usgnInfoStorage.put(usgn, usgnTracker);
			}
			return usgnTracker.get();
		}
		return Optional.empty();
	}
}
