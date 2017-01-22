package com.endercrypt.cs2dspy.representation.map;

import java.awt.Image;

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
