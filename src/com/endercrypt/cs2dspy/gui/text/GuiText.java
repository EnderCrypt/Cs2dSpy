package com.endercrypt.cs2dspy.gui.text;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Deque;

import com.endercrypt.cs2dspy.setting.Settings;

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
public class GuiText
{
	private static final int PADDING_WIDTH = 2;
	private Color backgroundColor;// = new Color(255, 255, 255, 100);
	private Deque<Text> texts = new ArrayDeque<>();

	public GuiText()
	{
		backgroundColor = Settings.get().key("Client.GuiTextBgColor").colorArgs(100);
	}

	public GuiText(String text, Color color)
	{
		this();
		addText(text, color);
	}

	public void addText(String text, Color color)
	{
		addText(new Text(text, color));
	}

	public void addText(Text... textValues)
	{
		for (Text text : textValues)
		{
			texts.addLast(text);
		}
	}

	private int getWidth(FontMetrics fontMetrics)
	{
		int width = 0;
		for (Text text : texts)
		{
			width += text.getLength(fontMetrics);
		}
		return width;
	}

	public void draw(Graphics2D g2d, Alignment alignment, int x, int y)
	{
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int width = getWidth(fontMetrics) + (PADDING_WIDTH * 2);
		x += (width * alignment.value);
		int height = fontMetrics.getHeight();
		int ascent = fontMetrics.getAscent();
		g2d.setColor(backgroundColor);
		g2d.fillRect(x, y, width, height);
		x += PADDING_WIDTH;
		for (Text text : texts)
		{
			text.draw(g2d, x, y + ascent);
			x += text.getLength(fontMetrics);
		}
	}

	public static enum Alignment
	{
		LEFT(-1),
		CENTER(-0.5),
		RIGHT(0);

		private double value;

		private Alignment(double value)
		{
			this.value = value;
		}
	}

	public static class Text
	{
		private String text;
		private Color color;

		public Text(String text, Color color)
		{
			this.text = text;
			this.color = color;
		}

		public int getLength(FontMetrics fontMetrics)
		{
			return fontMetrics.stringWidth(text);
		}

		public void draw(Graphics2D g2d, int x, int y)
		{
			g2d.setColor(color);
			g2d.drawString(text, x, y);
		}
	}
}
