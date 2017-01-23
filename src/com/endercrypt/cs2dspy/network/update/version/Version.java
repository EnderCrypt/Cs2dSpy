package com.endercrypt.cs2dspy.network.update.version;

import java.awt.Graphics2D;

import com.endercrypt.cs2dspy.gui.text.GuiText;
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
public interface Version
{
	public GuiText generateGuiText();

	default void draw(Graphics2D g2d, Alignment alignment, int x, int y)
	{
		GuiText guiText = generateGuiText();
		guiText.draw(g2d, alignment, x, y);
	}
}
