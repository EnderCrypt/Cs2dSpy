package com.endercrypt.cs2dspy.representation;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import com.endercrypt.cs2dspy.gui.text.GuiPrinter;
import com.endercrypt.cs2dspy.gui.text.GuiPrinter.Direction;
import com.endercrypt.cs2dspy.link.AccessSource;
import com.endercrypt.cs2dspy.gui.text.GuiText;
import com.endercrypt.cs2dspy.representation.chat.ChatMessage;

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
public class SpyChat implements Iterable<ChatMessage>
{
	private Deque<ChatMessage> chat = new ArrayDeque<>();

	public SpyChat(AccessSource source) throws IOException
	{
		int chatLines = source.readInt();
		for (int i = 0; i < chatLines; i++)
		{
			chat.add(new ChatMessage(source));
		}
	}

	@Override
	public Iterator<ChatMessage> iterator()
	{
		return chat.iterator();
	}

	public void draw(Graphics2D g2d, Dimension screenSize)
	{
		GuiPrinter chatPrinter = new GuiPrinter.Builder()
				.setPosition(5, screenSize.height - 5)
				.setPadding(0)
				.setDirection(Direction.UP)
				.setTextAlignment(GuiText.Alignment.RIGHT)
				.build();

		for (ChatMessage chatMessage : this)
		{
			chatPrinter.add(chatMessage.getGuiText());
		}

		chatPrinter.draw(g2d);
	}
}
