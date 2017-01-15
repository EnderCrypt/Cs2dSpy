package com.endercrypt.cs2dspy.representation.realtime;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Optional;

import com.endercrypt.cs2dspy.AccessSource;
import com.endercrypt.cs2dspy.network.UsgnInfo;
import com.endercrypt.cs2dspy.network.UsgnManager;
import com.endercrypt.cs2dspy.representation.WeaponInfo;
import com.endercrypt.cs2dspy.representation.WeaponInfo.Weapon;
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
public class SpyPlayer
{
	private static final UsgnManager usgnManager = new UsgnManager();

	private final int id;
	private final boolean isBot;
	private final String name;
	private final String ip;
	private final int port;
	private final int ping;
	private final int usgn;
	private final Optional<UsgnInfo> usgnInfo;
	private final double x, y;
	private final double rotation;
	private final Health health;
	private final Team team;
	private final int money;
	private final int score;
	private final int death;
	private final int weaponType;

	private final Info info;

	public SpyPlayer(AccessSource source) throws IOException
	{
		id = source.readInt();
		isBot = source.readBoolean();
		name = source.read();
		ip = source.read();
		port = source.readInt();
		ping = source.readInt();
		usgn = source.readInt();
		usgnInfo = usgnManager.get(usgn);
		x = source.readDouble();
		y = source.readDouble();
		rotation = Math.toRadians(source.readDouble() - 90);
		health = new Health(source);
		team = Team.parse(source.readInt());
		money = source.readInt();
		score = source.readInt();
		death = source.readInt();
		weaponType = source.readInt();

		info = new Info();
	}

	public int getID()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public Team getTeam()
	{
		return team;
	}

	public Position getPosition()
	{
		return new Position(x, y);
	}

	public void draw(Graphics2D g2d)
	{
		// draw white background
		g2d.setColor(Color.WHITE);
		g2d.fillOval((int) x - 16, (int) y - 16, 32, 32);

		// draw line in facing direction
		g2d.setColor(Color.BLACK);
		double xAim = Math.cos(rotation) * 15;
		double yAim = Math.sin(rotation) * 15;
		g2d.drawLine((int) x, (int) y, (int) (x + xAim), (int) (y + yAim));

		// draw team color
		g2d.setColor(team.getColor());
		g2d.fillOval((int) x - 8, (int) y - 8, 16, 16);

		// draw outer health indicator
		g2d.setColor(health.getColor());
		g2d.drawOval((int) x - 16, (int) y - 16, 32, 32);
	}

	public void drawHudInfo(Graphics2D g2d, Point position)
	{
		FontMetrics fontMetrics = g2d.getFontMetrics();
		Dimension infoSize = info.getInfoDimension(fontMetrics);
		infoSize.width += 4;
		g2d.setColor(Color.WHITE);
		g2d.fillRect(position.x, position.y, infoSize.width, infoSize.height);
		g2d.setColor(Color.BLACK);

		int yScroll = position.y + fontMetrics.getAscent();
		for (String line : info)
		{
			g2d.drawString(line, position.x + 2, yScroll);
			yScroll += fontMetrics.getHeight();
		}

		g2d.drawRect(position.x, position.y, infoSize.width, infoSize.height);

		// this looks horrible, maybe i'll re-add it in future versions
		//Image weaponImage = WeaponInfo.getInstance().weapon(weaponType).getGfx();
		//g2d.drawImage(weaponImage, position.x, yScroll, weaponImage.getWidth(null) * 2, weaponImage.getHeight(null) * 2, null);
	}

	private class Info implements Iterable<String>
	{
		private Deque<String> lines = new ArrayDeque<>();

		public Info()
		{
			lines.add("  ID: " + id + (isBot ? " (Bot)" : ""));
			lines.add("Name: " + name);
			lines.add("IP: " + ip + ":" + port + " (" + ping + " ms)");
			lines.add("U.S.G.N: " + ((usgn == 0) ? "None" : usgn));
			if (usgnInfo.isPresent())
			{
				usgnInfo.get().addInfo(lines::add);
			}
			else
			{
				if (usgn > 0)
				{
					lines.add("(Fetching...)");
				}
			}
			lines.add("Health: " + health);
			lines.add("Money: " + money);
			lines.add("Score: " + score + " Deaths: " + death);
			try
			{
				Weapon weapon = WeaponInfo.getInstance().weapon(weaponType);
				lines.add("Weapon: (" + weapon.getID() + ") " + weapon.getName());
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				System.err.println("Received unknown weapon type from cs2d: " + weaponType);
				//e.printStackTrace();
			}
		}

		private int getWidth(FontMetrics fontMetrics)
		{
			int biggest = 0;
			for (String line : lines)
			{
				biggest = Math.max(biggest, fontMetrics.stringWidth(line));
			}
			return biggest;
		}

		private int getHeight(FontMetrics fontMetrics)
		{
			return fontMetrics.getHeight() * lines.size();
		}

		public Dimension getInfoDimension(FontMetrics fontMetrics)
		{
			return new Dimension(getWidth(fontMetrics), getHeight(fontMetrics));
		}

		@Override
		public Iterator<String> iterator()
		{
			return lines.iterator();
		}
	}

	public boolean isBot()
	{
		return isBot;
	}
}
