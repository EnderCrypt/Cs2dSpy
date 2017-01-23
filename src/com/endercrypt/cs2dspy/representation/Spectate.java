package com.endercrypt.cs2dspy.representation;

import java.util.Optional;

import com.endercrypt.cs2dspy.representation.realtime.SpyPlayer;

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
public class Spectate
{
	private int spectateID = 0;

	public void cycleNext(SpyRealtime realtime)
	{
		int nextID = Integer.MAX_VALUE;
		for (SpyPlayer player : realtime.players())
		{
			int id = player.getID();
			if ((id > spectateID) && (id < nextID))
			{
				nextID = id;
			}
		}
		if (nextID == Integer.MAX_VALUE)
			nextID = lowestId(realtime);
		spectateID = nextID;
	}

	public void cyclePrev(SpyRealtime realtime)
	{
		int nextID = 0;
		for (SpyPlayer player : realtime.players())
		{
			int id = player.getID();
			if ((id < spectateID) && (id > nextID))
			{
				nextID = id;
			}
		}
		if (nextID == 0)
			nextID = highestId(realtime);
		spectateID = nextID;
	}

	private static int highestId(SpyRealtime realtime)
	{
		int highest = 0;
		for (SpyPlayer player : realtime.players())
		{
			int id = player.getID();
			if (id > highest)
				highest = id;
		}
		return highest;
	}

	private static int lowestId(SpyRealtime realtime)
	{
		int lowest = Integer.MAX_VALUE;
		for (SpyPlayer player : realtime.players())
		{
			int id = player.getID();
			if (id < lowest)
				lowest = id;
		}
		if (lowest > 32)
		{
			lowest = 0; // no players, stop spectating
		}
		return lowest;
	}

	public Optional<SpyPlayer> get(SpyRealtime realtime)
	{
		if (spectateID == 0)
			return Optional.empty();
		for (SpyPlayer player : realtime.players())
		{
			if (player.getID() == spectateID)
				return Optional.of(player);
		}
		// didnt find the spectate id? player stopped existing?
		return Optional.empty();
	}

	public void stop()
	{
		spectateID = 0;
	}
}
