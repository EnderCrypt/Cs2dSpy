package com.endercrypt.cs2dspy.representation.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class PlayerListOverlay
{
	private static final int PADDING = 20;

	private List<Column> columns = new ArrayList<>();

	public PlayerListOverlay()
	{
		// TODO Auto-generated constructor stub
	}

	public void addValue(PlayerValueType playerValue, int weight)
	{
		columns.add(new Column(playerValue, weight));
	}

	private int getTotalWeight()
	{
		int weight = 0;
		for (Column column : columns)
		{
			weight += column.weight;
		}
		return weight;
	}

	public void draw(Graphics2D g2d, SpyPlayer[] players, Dimension screenSize)
	{
		int totalWidht = screenSize.width - (PADDING * 2);
		// full rect
		g2d.setColor(new Color(200, 200, 200, 200));
		g2d.fillRect(PADDING, PADDING, totalWidht, screenSize.height - (PADDING * 2));
		// row lines
		for (int i = 0; i < players.length; i++)
		{
			g2d.setColor(new Color(100, 100, 100, 150));
			g2d.drawLine(PADDING, PADDING + 32 + (i * 16), screenSize.width - PADDING, PADDING + 32 + (i * 16));
		}
		// outline
		g2d.setColor(Color.BLACK);
		g2d.drawRect(PADDING, PADDING, totalWidht, screenSize.height - (PADDING * 2));
		// column title line
		g2d.drawLine(PADDING, PADDING + 16, screenSize.width - PADDING, PADDING + 16);
		// columns
		int x = PADDING;
		double weightWidth = totalWidht / getTotalWeight();
		for (Column column : columns)
		{
			// draw row line
			g2d.drawLine(x, PADDING, x, screenSize.height - PADDING);
			// draw columns title
			g2d.drawString(column.playerValue.name().toLowerCase(), x + 5, PADDING + 13);
			// draw for each player
			for (int i = 0; i < players.length; i++)
			{
				// draw column value
				SpyPlayer player = players[i];
				String value = column.playerValue.get(player);
				g2d.drawString(value, x + 5, PADDING + 30 + (i * 16));
			}
			// move position
			x += (weightWidth * column.weight);
		}
	}

	private static class Column
	{
		private PlayerValueType playerValue;
		private int weight;

		public Column(PlayerValueType playerValue, int weight)
		{
			this.playerValue = playerValue;
			this.weight = weight;
		}
	}
}
