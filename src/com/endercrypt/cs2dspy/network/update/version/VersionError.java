package com.endercrypt.cs2dspy.network.update.version;

import java.awt.Color;
import java.awt.Graphics2D;

import com.endercrypt.cs2dspy.gui.GuiText;
import com.endercrypt.cs2dspy.gui.GuiText.Alignment;

public class VersionError implements Version
{
	private static final boolean DISPLAY_CAUSE = true;
	private Throwable cause;

	public VersionError(Throwable cause)
	{
		this.cause = cause;
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, Alignment alignment)
	{
		GuiText guiText = new GuiText();
		guiText.addText("Version check error", Color.RED);
		if (DISPLAY_CAUSE)
		{
			guiText.addText(": ", Color.RED);
			guiText.addText(cause.getMessage(), Color.RED);
		}
		guiText.draw(g2d, alignment, x, y);
	}
}
