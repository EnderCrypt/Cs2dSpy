package com.endercrypt.cs2dspy.representation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import com.endercrypt.cs2dspy.link.AccessSource;
import com.endercrypt.cs2dspy.representation.player.SpyPlayer;
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
public class SpyRealtime
{
	private final SpyChat spyChat;
	private final SpyPlayer[] players;
	private final int ageMs;

	public SpyRealtime(AccessSource source) throws IOException
	{
		spyChat = new SpyChat(source);
		int playerCount = source.readInt();
		players = new SpyPlayer[playerCount];
		for (int i = 0; i < playerCount; i++)
		{
			players[i] = new SpyPlayer(source);
		}
		ageMs = (int) (System.currentTimeMillis() - source.getAccessAge());
	}

	public SpyPlayer[] players()
	{
		return Arrays.copyOf(players, players.length);
	}

	public Optional<SpyPlayer> getNearbyPlayer(Position position, double maxLength)
	{
		SpyPlayer spyPlayer = null;
		double dist = maxLength;
		for (SpyPlayer player : players)
		{
			double cDist = player.getPosition().distance(position);
			if (cDist < dist)
			{
				spyPlayer = player;
				dist = cDist;
			}
		}
		return Optional.ofNullable(spyPlayer);
	}

	public void draw(Graphics2D g2d)
	{
		g2d.setStroke(new BasicStroke(3));
		for (SpyPlayer player : players)
		{
			player.draw(g2d);
		}
		g2d.setStroke(new BasicStroke(1));
	}

	public void drawHud(Graphics2D hud, Dimension screenSize)
	{
		FontMetrics fontMetrics = hud.getFontMetrics();

		// latency
		if (ageMs > 1500)
		{
			String timeText = null;
			if (ageMs < 10_000)
				timeText = ageMs + " ms";
			else
				timeText = Math.round(ageMs / 1000.0) + " sec";
			String warning = "Connection issue: " + timeText;
			int warningWidth = fontMetrics.stringWidth(warning) + 10;
			int x = (screenSize.width / 2) - (warningWidth / 2);
			int y = 32;
			hud.setColor(Color.BLACK);
			hud.drawRect(x, y, warningWidth, fontMetrics.getHeight());
			hud.setColor(Color.WHITE);
			hud.fillRect(x, y, warningWidth, fontMetrics.getHeight());
			hud.setColor(Color.RED);
			hud.drawString(warning, x + 5, y + fontMetrics.getHeight() - fontMetrics.getDescent());
		}

		// chat
		spyChat.draw(hud, screenSize);
	}
}
