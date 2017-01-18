package com.endercrypt.cs2dspy.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Deque;

public class GuiText
{
	private static final int PADDING_WIDTH = 2;
	private static final Color backgroundColor = new Color(255, 255, 255, 100);
	private Deque<Text> texts = new ArrayDeque<>();

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
