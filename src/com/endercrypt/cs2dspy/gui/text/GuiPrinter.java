package com.endercrypt.cs2dspy.gui.text;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Queue;

import com.endercrypt.cs2dspy.gui.text.GuiText.Alignment;

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
public class GuiPrinter
{
	public static enum Direction
	{
		UP,
		DOWN;
	}

	private final int x, y;
	private final int padding;
	private final Direction direction;
	private final Alignment textAlignment;
	private final Queue<GuiElement> elements = new ArrayDeque<>();

	public GuiPrinter(int x, int y, int padding, Direction direction, Alignment textAlignment)
	{
		this.x = x;
		this.y = y;
		this.padding = padding;
		this.direction = direction;
		this.textAlignment = textAlignment;
	}

	public void add(GuiElement guiElement)
	{
		elements.add(guiElement);
		elements.add(new GuiPadding(padding));
	}

	public void draw(Graphics2D g2d)
	{
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int yPosition = y;

		if (direction == Direction.UP)
		{
			yPosition -= fontMetrics.getHeight();
		}

		for (GuiElement element : elements)
		{
			element.draw(g2d, textAlignment, x, yPosition);
			int height = element.getHeight(fontMetrics);
			switch (direction)
			{
			case UP:
				yPosition -= height;
				break;
			case DOWN:
				yPosition += height;
				break;
			}
		}
	}

	public static class Builder
	{
		private int x, y;
		private int padding;
		private Direction direction;
		private Alignment textAlignment;

		public Builder setPosition(int x, int y)
		{
			this.x = x;
			this.y = y;
			return this;
		}

		public Builder setPadding(int padding)
		{
			this.padding = padding;
			return this;
		}

		public Builder setDirection(Direction direction)
		{
			this.direction = direction;
			return this;
		}

		public Builder setTextAlignment(Alignment textAlignment)
		{
			this.textAlignment = textAlignment;
			return this;
		}

		public GuiPrinter build()
		{
			return new GuiPrinter(x, y, padding, direction, textAlignment);
		}
	}
}
