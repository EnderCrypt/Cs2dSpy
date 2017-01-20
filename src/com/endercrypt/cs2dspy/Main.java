package com.endercrypt.cs2dspy;

import java.awt.Color;
import java.awt.Dimension;
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
import com.endercrypt.cs2dspy.gui.View;
import com.endercrypt.cs2dspy.gui.keyboard.Keyboard;
import com.endercrypt.cs2dspy.gui.splash.SplashWindow;
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
		keyboard.bindKey(KeyEvent.VK_E, Keyboard.BindType.PRESS, (keyCode, bindType) -> spectate.cycleNext(realtime));
		keyboard.bindKey(KeyEvent.VK_Q, Keyboard.BindType.PRESS, (keyCode, bindType) -> spectate.cyclePrev(realtime));

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
		SplashWindow.create().showBlocking();
	}

	private static class ApplicationGui implements AwtWindow.DrawListener
	{
		private Dimension screenSize;
		private Optional<SpyPlayer> hoverPlayer = Optional.empty();
		private Optional<SpyPlayer> spectPlayer = Optional.empty();
		private Position mousePosition;
		private Position mapMousePosition;
		private Point mapTilePosition;

		@Override
		public void onDraw(Graphics2D g2d, Dimension guiScreenSize)
		{
			Graphics2D hudG2d = (Graphics2D) g2d.create();
			screenSize = guiScreenSize;

			mousePosition = new Position(window.getMousePosition());
			mapMousePosition = generateMapPosition(mousePosition);
			mapTilePosition = new Point((int) (mapMousePosition.x / 32), (int) (mapMousePosition.y / 32));

			// spectate if enabled
			spectPlayer = spectate.get(realtime);
			spectPlayer.ifPresent((player) -> view.setPosition(player.getPosition()));

			// draw content
			map.draw(g2d, view, screenSize);
			view.translate(g2d, screenSize);
			if (realtime != null)
			{
				realtime.draw(g2d);
				hoverPlayer = realtime.getNearbyPlayer(mapMousePosition, 250.0);
			}

			// dispose graphics
			g2d.dispose();

			// draw HUD
			drawHud(hudG2d);
		}

		private Position generateHudPosition(Position position)
		{
			position = position.getLocation();
			position.x += screenSize.width / 2;
			position.y += screenSize.height / 2;
			Position viewPosition = view.getPosition();
			position.x -= viewPosition.x;
			position.y -= viewPosition.y;
			return position;
		}

		private Position generateMapPosition(Position position)
		{
			position = position.getLocation();
			position.x -= screenSize.width / 2;
			position.y -= screenSize.height / 2;
			Position viewPosition = view.getPosition();
			position.x += viewPosition.x;
			position.y += viewPosition.y;
			return position;
		}

		private void drawHud(Graphics2D g2d)
		{
			g2d.setColor(Color.BLACK);
			// draw mouse position
			GuiText guiLocationText = new GuiText();
			guiLocationText.addText(Math.round(mapMousePosition.x) + ", " + Math.round(mapMousePosition.y) + " (" + mapTilePosition.x + "|" + mapTilePosition.y + ")", Color.BLACK);
			guiLocationText.draw(g2d, Alignment.RIGHT, 10, 10);

			// draw data about player near mouse
			hoverPlayer.ifPresent(new Consumer<SpyPlayer>()
			{
				@Override
				public void accept(SpyPlayer player)
				{
					Position position = player.getPosition();
					Position hudPosition = generateHudPosition(position);
					g2d.drawLine((int) hudPosition.x, (int) hudPosition.y, (int) mousePosition.x, (int) mousePosition.y);
					player.drawHudInfo(g2d, new Point((int) mousePosition.x, (int) mousePosition.y));
				}
			});

			if (realtime != null)
			{
				g2d.setColor(Color.WHITE);
				realtime.drawHud(g2d, screenSize);
			}

			// draw info about spectating a player
			spectate.get(realtime).ifPresent(new Consumer<SpyPlayer>()
			{
				@Override
				public void accept(SpyPlayer player)
				{
					GuiText guiSpectatingText = new GuiText();
					guiSpectatingText.addText("Spectating (" + player.getID(), Color.BLACK);
					if (player.isBot())
					{
						guiSpectatingText.addText(", Bot", Color.BLACK);
					}
					guiSpectatingText.addText(") ", Color.BLACK);
					guiSpectatingText.addText(player.getName(), player.getTeam().getColor());
					guiSpectatingText.draw(g2d, Alignment.LEFT, screenSize.width - 7, 10);
				}
			});
		}
	}
}
