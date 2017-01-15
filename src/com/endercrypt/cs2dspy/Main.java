package com.endercrypt.cs2dspy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

import com.endercrypt.cs2dspy.gui.AwtWindow;
import com.endercrypt.cs2dspy.gui.GuiText;
import com.endercrypt.cs2dspy.gui.GuiText.Alignment;
import com.endercrypt.cs2dspy.gui.SplashWindow;
import com.endercrypt.cs2dspy.gui.View;
import com.endercrypt.cs2dspy.gui.keyboard.Keyboard;
import com.endercrypt.cs2dspy.representation.SpyMap;
import com.endercrypt.cs2dspy.representation.SpyRealtime;
import com.endercrypt.cs2dspy.representation.realtime.SpyPlayer;
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
public class Main
{
	private static final int G_FPS = 50; // Graphical FPS
	private static final int Cs2dSpyUpdateFrequency = 5;
	private static final int U_FPS = 50 / Cs2dSpyUpdateFrequency; // Update FPS

	private static Timer timer;

	private static AwtWindow window;
	private static View view;
	private static SpyMap map;
	private static SpyRealtime realtime;
	private static Spectate spectate;

	public static void main(String[] args) throws IOException, InterruptedException
	{
		// create window
		window = new AwtWindow("Cs2d Spy Client", new Dimension(1000, 500), new ApplicationGui());

		// read in map data from cs2d
		try (AccessSource source = SpyAccess.MAP.access())
		{
			map = new SpyMap(source);
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Please start up the server before launching the spy client", "unable to reach server", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			return;
		}

		// spectate
		spectate = new Spectate();

		// bind keys
		Keyboard keyboard = window.getKeyboard();
		keyboard.bindKey(KeyEvent.VK_W, Keyboard.BindType.PRESS, (keyCode, bindType) -> spectate.stop());
		keyboard.bindKey(KeyEvent.VK_A, Keyboard.BindType.PRESS, (keyCode, bindType) -> spectate.stop());
		keyboard.bindKey(KeyEvent.VK_S, Keyboard.BindType.PRESS, (keyCode, bindType) -> spectate.stop());
		keyboard.bindKey(KeyEvent.VK_D, Keyboard.BindType.PRESS, (keyCode, bindType) -> spectate.stop());
		keyboard.bindKey(KeyEvent.VK_RIGHT, Keyboard.BindType.PRESS, (keyCode, bindType) -> spectate.cycleNext(realtime));
		keyboard.bindKey(KeyEvent.VK_LEFT, Keyboard.BindType.PRESS, (keyCode, bindType) -> spectate.cyclePrev(realtime));

		// create view and bind view-movement keys
		Point mapCenter = map.getCenter();
		view = new View(new Position(mapCenter.x * 32, mapCenter.y * 32));
		view.bindMovementKeys(keyboard);

		// setup timer
		timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				// update keyboard
				window.getKeyboard().update();

				// update view
				view.update();

				// redraw screen
				window.repaint();
			}
		}, 0, 1000 / G_FPS);

		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				try (AccessSource source = SpyAccess.REALTIME.access())
				{
					realtime = new SpyRealtime(source);
				}
				catch (NumberFormatException e) // caught because conversation of a value that hasnt been entirely written
				{
					// ignore
				}
				catch (FileNotFoundException e) // caused when cs2d hasnt had time to write realtime data yet
				{
					// ignore
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}, 0, 1000 / U_FPS);

		// show main window
		window.show();

		// show splash screen
		SplashWindow.create().show();
	}

	private static class ApplicationGui implements AwtWindow.DrawListener
	{
		private Graphics2D g2d;
		private Graphics2D hud;
		private FontMetrics fontMetrics;
		private Dimension screenSize;
		private Optional<SpyPlayer> hoverPlayer = Optional.empty();
		private Optional<SpyPlayer> spectPlayer = Optional.empty();
		private Point mousePosition;
		private Point absoluteMousePosition;
		private Point absoluteTilePosition;

		@Override
		public void onDraw(Graphics2D guiG2d, Dimension guiScreenSize)
		{
			g2d = guiG2d;
			hud = (Graphics2D) g2d.create();
			fontMetrics = g2d.getFontMetrics();
			screenSize = guiScreenSize;

			mousePosition = window.getMousePosition();
			absoluteMousePosition = calculateAbsolutePosition(mousePosition);
			absoluteTilePosition = new Point(absoluteMousePosition.x / 32, absoluteMousePosition.y / 32);

			// spectate if enabled
			spectPlayer = spectate.get(realtime);
			spectPlayer.ifPresent((player) -> view.setPosition(player.getPosition()));

			// draw content
			map.draw(g2d, view, screenSize);
			view.translate(g2d, screenSize);
			if (realtime != null)
			{
				realtime.draw(g2d);
				hoverPlayer = realtime.getNearbyPlayer(absoluteMousePosition, 250.0);

				g2d.setStroke(new BasicStroke(1));
				g2d.setColor(Color.BLACK);

				hoverPlayer.ifPresent(new Consumer<SpyPlayer>()
				{
					@Override
					public void accept(SpyPlayer player)
					{
						Position position = player.getPosition();
						g2d.drawLine(absoluteMousePosition.x, absoluteMousePosition.y, (int) position.x, (int) position.y);
					}
				});
			}

			// draw HUD
			drawHud();
		}

		private Point calculateAbsolutePosition(Point mousePosition)
		{
			mousePosition.x -= screenSize.width / 2;
			mousePosition.y -= screenSize.height / 2;
			Position position = view.getPosition();
			mousePosition.x += position.x;
			mousePosition.y += position.y;
			return mousePosition;
		}

		private void drawHud()
		{
			// draw mouse position
			GuiText guiText = new GuiText();
			guiText.addText(absoluteMousePosition.x + ", " + absoluteMousePosition.y + " (" + absoluteTilePosition.x + "|" + absoluteTilePosition.y + ")", Color.BLACK);
			guiText.draw(hud, Alignment.RIGHT, 10, 10);

			// draw data about player near mouse
			hoverPlayer.ifPresent((player) -> player.drawHudInfo(hud, mousePosition));
			if (realtime != null)
			{
				hud.setColor(Color.WHITE);
				realtime.drawHud(hud, screenSize);
			}

			// draw info about spectating a player
			Optional<SpyPlayer> specPlayer = spectate.get(realtime);
			specPlayer.ifPresent(new Consumer<SpyPlayer>()
			{
				@Override
				public void accept(SpyPlayer player)
				{
					GuiText guiText = new GuiText();
					guiText.addText("Spectating (" + player.getID(), Color.BLACK);
					if (player.isBot())
					{
						guiText.addText(", Bot", Color.BLACK);
					}
					guiText.addText(") ", Color.BLACK);
					guiText.addText(player.getName(), player.getTeam().getColor());
					guiText.draw(hud, Alignment.LEFT, screenSize.width - 7, 10);
				}
			});
		}
	}
}
