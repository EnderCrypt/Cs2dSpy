package com.endercrypt.cs2dspy.network.update.version;

import java.awt.Color;
import java.awt.Graphics2D;

import com.endercrypt.cs2dspy.gui.GuiText.Alignment;

public class SameVersion extends GenericVersion
{
	private double alpha = 1.0;

	public SameVersion()
	{
		super("Version up to date!", getColor(1.0));
	}

	private static Color getColor(double alpha)
	{
		return new Color(0, 255, 0, (int) (255 * alpha));
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, Alignment alignment)
	{
		alpha -= 0.005;
		double xBack = (0.25 - alpha);
		if (xBack < 0)
			xBack = 0;
		xBack = (xBack / 0.25) * 20;
		xBack *= xBack;
		x -= (int) xBack;
		if (alpha > 0)
		{
			color = getColor(alpha);
			super.draw(g2d, x, y, alignment);
		}
	}
}
