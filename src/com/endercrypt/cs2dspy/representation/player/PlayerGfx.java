package com.endercrypt.cs2dspy.representation.player;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.endercrypt.cs2dspy.gui.GraphicsUtil;
import com.endercrypt.cs2dspy.representation.SpyCs2dInfo;

public class PlayerGfx
{
	private static final PlayerGfx instance = new PlayerGfx();

	public static PlayerGfx get()
	{
		return instance;
	}

	private TeamGfx[] teams = new TeamGfx[2];

	public PlayerGfx()
	{
		String playerGfxDirectory = SpyCs2dInfo.get().getCs2dDirectory("gfx/player/");
		try
		{
			teams[0] = new TeamGfx(playerGfxDirectory, "t");
			teams[1] = new TeamGfx(playerGfxDirectory, "ct");
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public TeamGfx team(Team team)
	{
		if (team.equals(Team.TERRORIST))
			return teams[0];
		if (team.equals(Team.COUNTER_TERRORIST))
			return teams[1];
		throw new RuntimeException("Gfx doesent exist for team: " + team.name());
	}

	public Image terrorist(int look)
	{
		return team(Team.TERRORIST).look(look);
	}

	public Image counterTerrorist(int look)
	{
		return team(Team.COUNTER_TERRORIST).look(look);
	}

	public class TeamGfx
	{
		private Image[] looks = new Image[4];

		public TeamGfx(String directory, String prefix) throws IOException
		{
			for (int i = 0; i < looks.length; i++)
			{
				int look = i + 1;
				File gfxFile = new File(directory + prefix + look + ".bmp");
				BufferedImage image;
				image = GraphicsUtil.loadImage(gfxFile);
				image = image.getSubimage(0, 64, 32, 32);
				looks[i] = GraphicsUtil.filterOutColor(image, image.getRGB(0, 0));
			}
		}

		public Image look(int look)
		{
			return looks[look];
		}
	}
}
