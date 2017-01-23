package com.endercrypt.cs2dspy.gui.text;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Queue;

import com.endercrypt.cs2dspy.gui.text.GuiText.Alignment;

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
	private final Queue<GuiText> texts = new ArrayDeque<>();

	public GuiPrinter(int x, int y, int padding, Direction direction, Alignment textAlignment)
	{
		this.x = x;
		this.y = y;
		this.padding = padding;
		this.direction = direction;
		this.textAlignment = textAlignment;
	}

	public void add(GuiText guiText)
	{
		texts.add(guiText);
	}

	public void draw(Graphics2D g2d)
	{
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int yPosition = y;

		if (direction == Direction.UP)
		{
			yPosition -= fontMetrics.getHeight();
		}

		int yShift = (fontMetrics.getHeight()) + padding;

		for (GuiText text : texts)
		{
			text.draw(g2d, textAlignment, x, yPosition);
			switch (direction)
			{
			case UP:
				yPosition -= yShift;
				break;
			case DOWN:
				yPosition += yShift;
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
