package com.endercrypt.cs2dspy.gui.text;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.endercrypt.cs2dspy.gui.text.GuiText.Alignment;

public class GuiPadding implements GuiElement
{
	private int height;

	public GuiPadding(int height)
	{
		this.height = height;
	}

	@Override
	public int getHeight(FontMetrics fontMetrics)
	{
		return height;
	}

	@Override
	public void draw(Graphics2D g2d, Alignment alignment, int x, int y)
	{
		// invisible
	}
}
