package com.endercrypt.cs2dspy.network.update;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.endercrypt.cs2dspy.network.update.version.GenericVersion;
import com.endercrypt.cs2dspy.network.update.version.SameVersion;
import com.endercrypt.cs2dspy.network.update.version.Version;

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
public class VersionNumber
{
	private final int[] version;

	public VersionNumber(int... version)
	{
		this.version = version;
	}

	public VersionNumber(Document document)
	{
		Element element = document.select("#icontent > div:nth-child(4) > div:nth-child(1) > h1").first();
		String title = element.text();
		version = getVersion(title);
	}

	private static int[] getVersion(String title)
	{
		Deque<Integer> versionNumbers = new ArrayDeque<>();
		char[] titleChar = title.toCharArray();
		String currentString = "";
		for (int i = titleChar.length - 1; i >= 0; i--)
		{
			char c = titleChar[i];
			if ((c >= '0') && (c <= '9'))
			{
				currentString = currentString + c;
			}
			else
			{
				if (currentString.equals(""))
					break;
				versionNumbers.addFirst(Integer.parseInt(currentString));
				currentString = "";
			}
		}
		return versionNumbers.stream().mapToInt(i -> i).toArray();
	}

	public Version compareTo(VersionNumber other)
	{
		int score = getScore();
		int otherScore = other.getScore();
		if (score == otherScore)
		{
			return new SameVersion();
		}
		else
		{
			if (score > otherScore)
			{
				return new GenericVersion("Version ahead (Development version)", Color.GREEN);
			}
			if (score < otherScore)
			{
				return new GenericVersion("Version Out Of Date!", new Color(255, 100, 100));
			}
		}
		return null; // impossible
	}

	public int getScore()
	{
		int score = 0;
		for (int i = 0; i < version.length; i++)
		{
			int versionFraction = version[version.length - 1 - i];
			score += Math.pow(100, i) * versionFraction;
		}
		return score;
	}

	@Override
	public String toString()
	{
		return Arrays.toString(version);
	}

	public String toString(String separator)
	{
		StringBuilder sb = new StringBuilder();
		for (int v : version)
		{
			sb.append(String.valueOf(v));
			sb.append(separator);
		}
		sb.setLength(sb.length() - separator.length());
		return sb.toString();
	}
}
