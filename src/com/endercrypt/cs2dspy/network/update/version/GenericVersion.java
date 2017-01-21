package com.endercrypt.cs2dspy.network.update.version;

import java.awt.Color;
import java.awt.Graphics2D;

import com.endercrypt.cs2dspy.gui.GuiText;
import com.endercrypt.cs2dspy.gui.GuiText.Alignment;

public class GenericVersion implements Version
{
	private String message;
	private Color color;

	public GenericVersion(String message, Color color)
	{
		this.message = message;
		this.color = color;
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, Alignment alignment)
	{
		GuiText guiText = new GuiText();
		guiText.addText(message, color);
		guiText.draw(g2d, alignment, x, y);
	}
}
