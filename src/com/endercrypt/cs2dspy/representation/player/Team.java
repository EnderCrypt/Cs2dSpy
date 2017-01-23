package com.endercrypt.cs2dspy.representation.player;

import java.awt.Color;

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
public enum Team
{
	SPECTATOR(Color.YELLOW),
	TERRORIST(Color.RED),
	COUNTER_TERRORIST(Color.BLUE),
	VIP(Color.BLUE.darker()),
	PVP(Color.GREEN); // cs2dSpy custom pvp team

	private static final Team[] values = values();

	private Color teamColor;

	private Team(Color teamColor)
	{
		this.teamColor = teamColor;
	}

	public Color getColor()
	{
		return teamColor;
	}

	public static Team parse(int teamNumber)
	{
		return values[teamNumber];
	}
}
