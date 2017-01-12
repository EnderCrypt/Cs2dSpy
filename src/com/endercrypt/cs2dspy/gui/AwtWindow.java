package com.endercrypt.cs2dspy.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.endercrypt.cs2dspy.gui.keyboard.Keyboard;

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
public class AwtWindow
{
	private JFrame jFrame;
	private JPanel jPanel;
	private Keyboard keyboard;
	private MousePointer MousePointer;

	public AwtWindow(String title, Dimension windowSize, DrawListener listener)
	{
		this(title, windowSize, listener, false);
	}

	public AwtWindow(String title, Dimension windowSize, DrawListener listener, boolean undecorated)
	{
		jPanel = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			{
				setPreferredSize(windowSize);
			}

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawString("", 0, 0); // java bug causes delays first time drawing string, might aswell do it as early as possible
				Dimension screenSize = getSize();
				listener.onDraw(g2d, screenSize);
			}
		};

		jFrame = new JFrame(title);
		jFrame.setUndecorated(undecorated);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.add(jPanel);
		jFrame.pack();
		jFrame.setLocationRelativeTo(null);

		MousePointer = new MousePointer();
		MousePointer.install(jPanel);

		keyboard = new Keyboard(jFrame);
	}

	public JPanel getJPanel()
	{
		return jPanel;
	}

	public JFrame getJFrame()
	{
		return jFrame;
	}

	public void show()
	{
		jFrame.setVisible(true);
	}

	public void dispose()
	{
		jFrame.dispose();
	}

	public Keyboard getKeyboard()
	{
		return keyboard;
	}

	public Point getMousePosition()
	{
		return MousePointer.getPosition();
	}

	public void repaint()
	{
		jFrame.repaint();
	}

	public interface DrawListener
	{
		public void onDraw(Graphics2D g2d, Dimension screenSize);
	}
}
