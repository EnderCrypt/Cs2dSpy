package com.endercrypt.cs2dspy.representation.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	public static final int TILE_SIZE = 32;

	private BufferedImage[] tiles;

	public Tileset(String tilesetFile) throws IOException
	{
		this(new File(tilesetFile));
	}

	public Tileset(File tilesetFile) throws IOException
	{
		BufferedImage tilesetImage = readImage(tilesetFile);
		verifyTilesetSize(tilesetImage.getWidth(), tilesetImage.getHeight());
		int x_tiles = tilesetImage.getWidth() / TILE_SIZE;
		int y_tiles = tilesetImage.getHeight() / TILE_SIZE;
		tiles = new BufferedImage[x_tiles * y_tiles];
		int tileID = 0;
		for (int y = 0; y < y_tiles; y++)
		{
			for (int x = 0; x < x_tiles; x++)
			{
				tiles[tileID] = tilesetImage.getSubimage(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
				tileID++;
			}
		}
	}

	/**
	 * MAJORLY improves performance by converting BMP to {@code Tileset.TILESET_IMAGE_TYPE}
	 * @param tilesetFile
	 * @return
	 * @throws IOException
	 */
	private static BufferedImage readImage(File tilesetFile) throws IOException
	{
		BufferedImage image = ImageIO.read(tilesetFile);
		BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		convertedImage.getGraphics().drawImage(image, 0, 0, null);
		return convertedImage;
	}

	private static void verifyTilesetSize(int x, int y)
	{
		if ((x % TILE_SIZE != 0) || (y % TILE_SIZE != 0))
		{
			throw new BadTilesetException();
		}
	}

	public int count()
	{
		return tiles.length;
	}

	public BufferedImage frame(byte frame)
	{
		return tiles[frame + 127];
	}
}
