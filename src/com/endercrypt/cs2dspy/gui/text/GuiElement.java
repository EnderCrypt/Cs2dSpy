package com.endercrypt.cs2dspy.gui.text;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.endercrypt.cs2dspy.gui.text.GuiText.Alignment;

public interface GuiElement
{
	public int getHeight(FontMetrics fontMetrics);

	public void draw(Graphics2D g2d, Alignment alignment, int x, int y);
}
