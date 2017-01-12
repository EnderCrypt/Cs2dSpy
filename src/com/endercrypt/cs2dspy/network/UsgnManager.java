package com.endercrypt.cs2dspy.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private Map<Integer, UsgnInfo> usgnInfoStorage = new HashMap<>();

	public Optional<UsgnInfo> get(int usgn)
	{
		UsgnInfo info = null;
		if (usgn > 0)
		{
			info = usgnInfoStorage.get(usgn);
			if (info == null)
			{
				downloadUsgnInfo(usgn);
			}
		}
		return Optional.ofNullable(info);
	}

	private void downloadUsgnInfo(int usgn)
	{
		executorService.submit(new UsgnDownloader(usgn, (result) -> usgnInfoStorage.put(usgn, result)));
	}
}
