package com.endercrypt.cs2dspy.representation.chat;

import java.awt.Color;
import java.io.IOException;

import com.endercrypt.cs2dspy.gui.text.GuiText;
import com.endercrypt.cs2dspy.link.AccessSource;
import com.endercrypt.cs2dspy.representation.realtime.Team;

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
public class ChatMessage
{
	private Team team;
	private String name;
	private String message;

	private GuiText guiText;

	public ChatMessage(AccessSource source) throws NumberFormatException, IOException
	{
		team = Team.parse(source.readInt());
		name = source.read();
		message = source.read();

		guiText = new GuiText();
		guiText.addText(name, team.getColor());
		guiText.addText(": ", Color.YELLOW);
		guiText.addText(message, Color.YELLOW);
	}

	public Team getTeam()
	{
		return team;
	}

	public String getName()
	{
		return name;
	}

	public String getMessage()
	{
		return message;
	}

	public GuiText getGuiText()
	{
		return guiText;
	}
}
