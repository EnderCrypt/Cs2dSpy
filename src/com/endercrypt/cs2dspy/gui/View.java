package com.endercrypt.cs2dspy.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import com.endercrypt.cs2dspy.gui.keyboard.Keyboard;
import com.endercrypt.cs2dspy.gui.keyboard.Keyboard.BindType;
import com.endercrypt.cs2dspy.setting.Settings;
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
	public double viewSpeed;
	public double ZOOM_SPEED = 0.05;

	private Position position;
	private Motion motion;

	private double zoom = 1.0;
	private double zoomMotion = 0.0;

	public View()
	{
		viewSpeed = Settings.get().key("Client.CameraSpeed").getDouble();
	}

	public View(Position position)
	{
		this();
		this.position = position;
		motion = new Motion();
	}

	public void bindMovementKeys(Keyboard keyboard)
	{
		keyboard.bindKey(KeyEvent.VK_A, BindType.HOLD, (keyCode, bindType) -> motion.x -= viewSpeed * zoom);
		keyboard.bindKey(KeyEvent.VK_D, BindType.HOLD, (keyCode, bindType) -> motion.x += viewSpeed * zoom);
		keyboard.bindKey(KeyEvent.VK_W, BindType.HOLD, (keyCode, bindType) -> motion.y -= viewSpeed * zoom);
		keyboard.bindKey(KeyEvent.VK_S, BindType.HOLD, (keyCode, bindType) -> motion.y += viewSpeed * zoom);

		keyboard.bindKey(KeyEvent.VK_R, BindType.HOLD, (keyCode, bindType) -> zoomMotion += ZOOM_SPEED);
		keyboard.bindKey(KeyEvent.VK_F, BindType.HOLD, (keyCode, bindType) -> zoomMotion -= ZOOM_SPEED);
	}

	public void update()
	{
		// move
		position.add(motion);
		motion.multiplyLength(0.8);

		// zoom
		zoom += zoomMotion;
		zoomMotion *= 0.75;
		if (zoom < 1)
			zoom = 1;
		if (zoom > 5)
			zoom = 5;
	}

	public double getZoom()
	{
		return zoom;
	}

	public double getDividedZoom()
	{
		return 1.0 / getZoom();
	}

	public void translate(Graphics2D g2d, Dimension screenSize)
	{
		AffineTransform transform = new AffineTransform();
		transform.scale(getDividedZoom(), getDividedZoom());
		transform.translate(-position.x, -position.y);
		transform.translate((screenSize.width / 2) * getZoom(), (screenSize.height / 2) * getZoom());
		g2d.transform(transform);
	}

	public Position getPosition()
	{
		return position.getLocation();
	}

	public void setPosition(Position position)
	{
		this.position.x = position.x;
		this.position.y = position.y;
	}

	public Motion getMotion()
	{
		return new Motion(motion);
	}
}
