package com.endercrypt.cs2dspy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
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
import com.endercrypt.cs2dspy.gui.View;
import com.endercrypt.cs2dspy.gui.keyboard.Keyboard;
import com.endercrypt.cs2dspy.gui.splash.SplashWindow;
import com.endercrypt.cs2dspy.gui.text.GuiPrinter;
import com.endercrypt.cs2dspy.gui.text.GuiText;
import com.endercrypt.cs2dspy.gui.text.GuiPrinter.Direction;
import com.endercrypt.cs2dspy.gui.text.GuiText.Alignment;
import com.endercrypt.cs2dspy.network.update.VersionManager;
import com.endercrypt.cs2dspy.network.update.version.Version;
import com.endercrypt.cs2dspy.representation.SpyMap;
import com.endercrypt.cs2dspy.representation.SpyRealtime;
import com.endercrypt.cs2dspy.representation.position.GlobalPosition;
import com.endercrypt.cs2dspy.representation.realtime.SpyPlayer;
import com.endercrypt.cs2dspy.setting.Settings;
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
	private static int G_FPS; // Graphical FPS
	private static int Cs2dSpyUpdateFrequency;
	private static int U_FPS; // Update FPS

	private static Timer timer;

	private static AwtWindow window;
	private static View view;
	private static SpyMap map;
	private static SpyRealtime realtime;
	private static Spectate spectate;
	private static Optional<Version> version = Optional.empty();
	private static Font font;

	public static void main(String[] args) throws IOException, InterruptedException
	{
		// read settings
		Settings settings = Settings.get();
		Dimension screenSize = new Dimension(settings.key("Client.Width").getInteger(), settings.key("Client.Height").getInteger());
		G_FPS = settings.key("Client.Fps").getInteger();
		Cs2dSpyUpdateFrequency = settings.key("Cs2d.UpdateFrequency").getInteger();
		U_FPS = 50 / Cs2dSpyUpdateFrequency;

		System.out.println("Starting Cs2d client...");

		// create window
		window = new AwtWindow("Cs2d Spy Client", screenSize, new ApplicationGui());

		// set font
		String fontName = settings.key("Client.FontName").getString();
		int fontSize = settings.key("Client.FontSize").getInteger();
		font = window.getJFrame().getFont();
		font = new Font(font.getName(), font.getStyle(), fontSize);
		for (String existingFontName : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
		{
			if (existingFontName.equals(fontName))
			{
				System.out.println("Using font from settings: " + fontName + " (" + fontSize + " px)");
				font = new Font(fontName, Font.PLAIN, fontSize);
			}
		}
		System.out.println("Font: " + font);

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

		// show splash screen & wait untill it finishes
		SplashWindow.create().showBlocking();

		// check for update
		System.out.println("Current version: " + VersionManager.getCurrentVersion());
		if (Settings.get().key("Client.UpdateCheck").getBoolean())
		{
			version = Optional.of(VersionManager.get().blockingDownloadVersion());
		}
	}

	private static class ApplicationGui implements AwtWindow.DrawListener
	{
		private Dimension screenSize;
		private Optional<SpyPlayer> hoverPlayer = Optional.empty();
		private Optional<SpyPlayer> spectPlayer = Optional.empty();
		private GlobalPosition mousePosition;

		@Override
		public void onDraw(Graphics2D g2d, Dimension guiScreenSize)
		{
			g2d.setFont(font);
			Graphics2D hudG2d = (Graphics2D) g2d.create();
			screenSize = guiScreenSize;

			mousePosition = GlobalPosition.fromHud(new Position(window.getMousePosition()), view, guiScreenSize);

			// spectate if enabled
			spectPlayer = spectate.get(realtime);
			spectPlayer.ifPresent((player) -> view.setPosition(player.getPosition()));

			// draw content
			view.translate(g2d, screenSize);
			map.draw(g2d, view, screenSize);
			if (realtime != null)
			{
				realtime.draw(g2d);
				hoverPlayer = realtime.getNearbyPlayer(mousePosition.getMapPosition(), 250.0);
			}

			// dispose graphics
			g2d.dispose();

			// draw HUD
			drawHud(hudG2d);
		}

		private void drawHud(Graphics2D g2d)
		{
			g2d.setColor(Color.BLACK);

			GuiPrinter infoHud = new GuiPrinter.Builder()
					.setPosition(10, 10)
					.setPadding(5)
					.setDirection(Direction.DOWN)
					.setTextAlignment(GuiText.Alignment.RIGHT)
					.build();
			drawMousePositionInfo(infoHud);
			drawScaleInfo(infoHud);
			drawVersionInfo(infoHud);
			infoHud.draw(g2d);

			drawHoverPlayerData(g2d);

			drawRealtimeHud(g2d);

			drawSpectatingInfo(g2d);
		}

		private void drawMousePositionInfo(GuiPrinter printer)
		{
			GuiText guiLocationText = new GuiText();
			Position mousePixel = mousePosition.getMapPosition();
			Point mouseTile = mousePosition.getTilePosition();
			guiLocationText.addText(Math.round(mousePixel.x) + ", " + Math.round(mousePixel.y) + " (" + mouseTile.x + "|" + mouseTile.y + ")", Color.BLACK);

			printer.add(guiLocationText);
		}

		private static void drawScaleInfo(GuiPrinter printer)
		{
			GuiText guiZoomText = new GuiText();
			guiZoomText.addText(Math.round(100.0 * view.getDividedZoom()) + "% Scale", Color.BLACK);

			printer.add(guiZoomText);
		}

		private static void drawVersionInfo(GuiPrinter printer)
		{
			if (version.isPresent())
			{
				GuiText guiText = version.get().generateGuiText();
				printer.add(guiText);
			}
		}

		private void drawHoverPlayerData(Graphics2D g2d)
		{
			hoverPlayer.ifPresent(new Consumer<SpyPlayer>()
			{
				@Override
				public void accept(SpyPlayer player)
				{
					GlobalPosition playerPosition = GlobalPosition.fromMap(player.getPosition(), view, screenSize);
					Position playerHudPosition = playerPosition.getHudPosition();
					Position mouseHudPosition = mousePosition.getHudPosition();
					g2d.setColor(Color.BLACK);
					g2d.drawLine((int) playerHudPosition.x, (int) playerHudPosition.y, (int) mouseHudPosition.x, (int) mouseHudPosition.y);
					player.drawHudInfo(g2d, new Point((int) mouseHudPosition.x, (int) mouseHudPosition.y));
				}
			});
		}

		private void drawRealtimeHud(Graphics2D g2d)
		{
			if (realtime != null)
			{
				g2d.setColor(Color.WHITE);
				realtime.drawHud(g2d, screenSize);
			}
		}

		private void drawSpectatingInfo(Graphics2D g2d)
		{
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
