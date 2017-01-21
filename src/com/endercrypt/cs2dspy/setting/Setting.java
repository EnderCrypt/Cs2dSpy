package com.endercrypt.cs2dspy.setting;

import java.awt.Color;
import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Setting
{
	private String name;
	private String rawValue;

	protected Setting(String line)
	{
		int separatorIndex = line.indexOf("=");
		name = line.substring(0, separatorIndex).trim();
		rawValue = line.substring(separatorIndex + 1).trim();
	}

	public String getName()
	{
		return name;
	}

	public String getString()
	{
		return rawValue;
	}

	public int getInteger()
	{
		return Integer.parseInt(getString());
	}

	public double getDouble()
	{
		return Double.parseDouble(getString());
	}

	public String[] stringArgs()
	{
		Stream<String> results = Arrays.stream(getString().split(","));
		results = results.map((value) -> value.trim());
		return results.toArray((size) -> new String[size]);
	}

	public double[] doubleArgs()
	{
		Stream<String> stringStream = Arrays.stream(stringArgs());
		try (DoubleStream results = stringStream.mapToDouble((value) -> Double.parseDouble(value)))
		{
			return results.toArray();
		}
	}

	public int[] intArgs()
	{
		Stream<String> stringStream = Arrays.stream(stringArgs());
		try (IntStream results = stringStream.mapToInt((value) -> Integer.parseInt(value)))
		{
			return results.toArray();
		}
	}

	public Color colorArgs(int defaultAlpha)
	{
		int[] colors = intArgs();
		int r = colors[0];
		int g = colors[1];
		int b = colors[2];
		int a = 0;
		if (colors.length > 3)
			a = colors[3];

		return new Color(r, g, b, a);
	}

	public boolean getBoolean()
	{
		if (getString().equalsIgnoreCase("true") || getString().equalsIgnoreCase("1"))
			return true;
		if (getString().equalsIgnoreCase("false") || getString().equalsIgnoreCase("0"))
			return false;
		System.err.println("[" + this + "] boolean values in settings should be either true/false or 1/0, FALSE assumed");
		return false;
	}

	@Override
	public String toString()
	{
		return "\"" + name + "\" = \"" + getString() + "\"";
	}
}
