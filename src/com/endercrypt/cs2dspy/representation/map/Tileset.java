package com.endercrypt.cs2dspy.representation.map;

import java.awt.Image;

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
public class Tileset
{
	private Image[] tiles;

	public Tileset(Image[] tiles, int scale)
	{
		int size = MasterTileset.TILE_SIZE / scale;
		this.tiles = new Image[tiles.length];
		for (int i = 0; i < tiles.length; i++)
		{
			this.tiles[i] = tiles[i].getScaledInstance(size, size, Image.SCALE_SMOOTH);
		}
	}

	public int count()
	{
		return tiles.length;
	}

	public Image frame(byte frame)
	{
		return tiles[frame + 127];
	}
}
