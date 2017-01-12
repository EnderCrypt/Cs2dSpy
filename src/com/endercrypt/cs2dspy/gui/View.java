package com.endercrypt.cs2dspy.gui;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.endercrypt.cs2dspy.gui.keyboard.Keyboard;
import com.endercrypt.cs2dspy.gui.keyboard.Keyboard.BindType;
import com.endercrypt.library.position.Motion;
import com.endercrypt.library.position.Position;

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
public class View
{
	public static final double VIEW_SPEED = 5;

	private Position position;
	private Motion motion;

	public View(Position position)
	{
		this.position = position;
		motion = new Motion();
	}

	public void bindMovementKeys(Keyboard keyboard)
	{
		keyboard.bindKey(KeyEvent.VK_A, BindType.HOLD, (keyCode, bindType) -> motion.x -= VIEW_SPEED);
		keyboard.bindKey(KeyEvent.VK_D, BindType.HOLD, (keyCode, bindType) -> motion.x += VIEW_SPEED);
		keyboard.bindKey(KeyEvent.VK_W, BindType.HOLD, (keyCode, bindType) -> motion.y -= VIEW_SPEED);
		keyboard.bindKey(KeyEvent.VK_S, BindType.HOLD, (keyCode, bindType) -> motion.y += VIEW_SPEED);
	}

	public void update()
	{
		position.add(motion);
		motion.multiplyLength(0.8);
	}

	public void translate(Graphics2D g2d)
	{
		g2d.translate(-position.x, -position.y);
	}

	public Position getPosition()
	{
		return position.getLocation();
	}

	public Motion getMotion()
	{
		return new Motion(motion);
	}
}
