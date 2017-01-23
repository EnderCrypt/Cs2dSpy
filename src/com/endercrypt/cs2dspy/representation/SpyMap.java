package com.endercrypt.cs2dspy.representation;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import com.endercrypt.cs2dspy.gui.View;
import com.endercrypt.cs2dspy.link.AccessSource;
import com.endercrypt.cs2dspy.representation.map.MasterTileset;
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
	private MasterTileset masterTileset;

	private Dimension mapSize;
	private byte[][] map;

	public SpyMap(AccessSource source) throws IOException
	{
		// line 1
		String filesetFile = SpyCs2dInfo.get().getCs2dDirectory("gfx/tiles/") + source.read();
		masterTileset = new MasterTileset(filesetFile);

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

	public Dimension getSize()
	{
		return mapSize.getSize();
	}

	public Point getCenter()
	{
		return new Point(mapSize.width / 2, mapSize.height / 2);
	}

	public void draw(Graphics2D g2d, View view, Dimension screenSize)
	{
		Tileset zoomedTileset = masterTileset.getZoomTileset(view.getZoom());

		Position viewPosition = view.getPosition();
		viewPosition.x -= (screenSize.width / 2) * view.getZoom();
		viewPosition.y -= (screenSize.height / 2) * view.getZoom();

		int xTiles = (int) (view.getZoom() * (screenSize.getWidth() / MasterTileset.TILE_SIZE) + 2);
		int yTiles = (int) (view.getZoom() * (screenSize.getHeight() / MasterTileset.TILE_SIZE) + 2);

		// which tile on tiles[x][y] the x/y loop starts at
		int xPosition = (int) Math.floor(viewPosition.x / MasterTileset.TILE_SIZE);
		int yPosition = (int) Math.floor(viewPosition.y / MasterTileset.TILE_SIZE);

		// the exact x/y pixel position where tiles should be drawn onto the screen
		int xStart = (int) (viewPosition.x + Math.floor(xPosition * MasterTileset.TILE_SIZE - viewPosition.x));
		int yStart = (int) (viewPosition.y + Math.floor(yPosition * MasterTileset.TILE_SIZE - viewPosition.y));

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
				Image tile = zoomedTileset.frame(frame);
				g2d.drawImage(tile, px, py, 32, 32, null);
			}
		}
	}
}
