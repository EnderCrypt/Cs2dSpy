package com.endercrypt.cs2dspy.representation.map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.endercrypt.cs2dspy.gui.GraphicsUtil;

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
public class MasterTileset
{
	public static final int PRE_SCALES = 6; // 1 to 6
	public static final int TILE_SIZE = 32;

	private Tileset[] tilesets;

	public MasterTileset(String tilesetFile) throws IOException
	{
		this(new File(tilesetFile));
	}

	public MasterTileset(File tilesetFile) throws IOException
	{
		// read tileset image
		BufferedImage tilesetImage = GraphicsUtil.loadImage(tilesetFile);
		verifyTilesetSize(tilesetImage.getWidth(), tilesetImage.getHeight());
		int x_tiles = tilesetImage.getWidth() / TILE_SIZE;
		int y_tiles = tilesetImage.getHeight() / TILE_SIZE;
		Image[] tiles = new Image[x_tiles * y_tiles];
		int tileID = 0;
		for (int y = 0; y < y_tiles; y++)
		{
			for (int x = 0; x < x_tiles; x++)
			{
				tiles[tileID] = tilesetImage.getSubimage(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
				tileID++;
			}
		}

		// create scaled tilesets
		tilesets = new Tileset[PRE_SCALES];
		for (int i = 0; i < PRE_SCALES; i++)
		{
			tilesets[i] = new Tileset(tiles, i + 1);
		}
	}

	private static void verifyTilesetSize(int x, int y)
	{
		if ((x % TILE_SIZE != 0) || (y % TILE_SIZE != 0))
		{
			throw new BadTilesetException();
		}
	}

	public Tileset tileset()
	{
		return tilesets[0];
	}

	public Tileset getZoomTileset(double zoom)
	{
		int zoomIndex = (int) Math.min(zoom, PRE_SCALES) - 1;
		return getScaledTileset(zoomIndex);
	}

	public Tileset getScaledTileset(int scale)
	{
		return tilesets[scale];
	}
}
