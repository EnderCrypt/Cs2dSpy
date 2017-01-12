package com.endercrypt.cs2dspy.representation;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.endercrypt.cs2dspy.AccessSource;
import com.endercrypt.cs2dspy.representation.map.Tileset;
import com.endercrypt.library.position.Position;

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
public class SpyMap
{
	private Tileset tileset;

	private Dimension mapSize;
	private byte[][] map;

	public SpyMap(AccessSource source) throws IOException
	{
		// line 1
		tileset = new Tileset(source.read());

		// line 2
		int mapWidth = source.readInt();
		int mapHeight = source.readInt();
		mapSize = new Dimension(mapWidth, mapHeight);
		map = new byte[mapSize.width][mapSize.height];

		// line 3 and up
		for (int x = 0; x < mapWidth; x++)
		{
			map[x] = new byte[mapHeight];
			for (int y = 0; y < mapHeight; y++)
			{
				byte frame = source.readByte();
				map[x][y] = frame;
			}
		}
	}

	public byte getFrame(int x, int y)
	{
		return map[x][y];
	}

	public void draw(Graphics2D g2d, Position viewPosition, Dimension screenSize)
	{
		int xTiles = (int) ((screenSize.getWidth() / Tileset.TILE_SIZE) + 2);
		int yTiles = (int) ((screenSize.getHeight() / Tileset.TILE_SIZE) + 2);

		// which tile on tiles[x][y] the x/y loop starts at
		int xPosition = (int) Math.floor(viewPosition.x / Tileset.TILE_SIZE);
		int yPosition = (int) Math.floor(viewPosition.y / Tileset.TILE_SIZE);

		// the exact x/y pixel position where tiles should be drawn onto the screen
		int xStart = (int) Math.floor(xPosition * Tileset.TILE_SIZE - viewPosition.x);
		int yStart = (int) Math.floor(yPosition * Tileset.TILE_SIZE - viewPosition.y);

		for (int y = 0; y < yTiles; y++)
		{
			for (int x = 0; x < xTiles; x++)
			{
				int rx = xPosition + x;
				int ry = yPosition + y;
				if ((rx < 0) || (ry < 0) || (rx >= mapSize.width) || (ry >= mapSize.height))
					continue;
				int px = xStart + (x * 32);
				int py = yStart + (y * 32);
				byte frame = getFrame(rx, ry);
				BufferedImage tile = tileset.frame(frame);
				g2d.drawImage(tile, px, py, null);
			}
		}
	}
}
