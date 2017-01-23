package com.endercrypt.cs2dspy.representation.position;

import java.awt.Dimension;
import java.awt.Point;

import com.endercrypt.cs2dspy.gui.View;
import com.endercrypt.library.position.Position;

public class GlobalPosition
{
	public static GlobalPosition fromHud(Position hudPosition, View view, Dimension screenSize)
	{
		return new GlobalPosition(hudPosition, toMapPosition(hudPosition, view, screenSize));
	}

	public static GlobalPosition fromMap(Position mapPosition, View view, Dimension screenSize)
	{
		return new GlobalPosition(toHudPosition(mapPosition, view, screenSize), mapPosition);
	}

	private static Position toHudPosition(Position mapPosition, View view, Dimension screenSize)
	{
		Position viewPosition = view.getPosition();
		Position hudPosition = new Position(0, 0);

		hudPosition.x += (screenSize.width / 2);
		hudPosition.y += (screenSize.height / 2);

		hudPosition.x += (mapPosition.x - viewPosition.x) * view.getDividedZoom();
		hudPosition.y += (mapPosition.y - viewPosition.y) * view.getDividedZoom();

		return hudPosition;
	}

	private static Position toMapPosition(Position hudPosition, View view, Dimension screenSize)
	{
		Position viewPosition = view.getPosition();
		Position mapPosition = new Position(0, 0);

		mapPosition.x -= (screenSize.width / 2) * view.getZoom();
		mapPosition.y -= (screenSize.height / 2) * view.getZoom();

		mapPosition.x += hudPosition.x * view.getZoom();
		mapPosition.y += hudPosition.y * view.getZoom();

		mapPosition.x += viewPosition.x;
		mapPosition.y += viewPosition.y;
		return mapPosition;
	}

	private Position mapPosition;
	private Position hudPosition;
	private Point tilePosition;

	private GlobalPosition(Position hudPosition, Position mapPosition)
	{
		this.hudPosition = hudPosition;
		this.mapPosition = mapPosition;
		this.tilePosition = new Point((int) (mapPosition.x / 32), (int) (mapPosition.y / 32));
	}

	// map //

	public Position getMapPosition()
	{
		return new Position(mapPosition);
	}

	// map tile //

	public Point getTilePosition()
	{
		return new Point(tilePosition);
	}

	// hud //

	public Position getHudPosition()
	{
		return new Position(hudPosition);
	}
}
