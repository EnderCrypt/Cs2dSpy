package com.endercrypt.cs2dspy.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

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
public class MousePointer
{
	private Listener listener = new Listener();
	private Point position = new Point(0, 0);

	public void install(JComponent jComponent)
	{
		jComponent.addMouseMotionListener(listener);
	}

	public Point getPosition()
	{
		return position.getLocation();
	}

	private class Listener implements MouseMotionListener
	{
		@Override
		public void mouseDragged(MouseEvent e)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			position = e.getPoint();
		}
	}
}
