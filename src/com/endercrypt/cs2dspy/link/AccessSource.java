package com.endercrypt.cs2dspy.link;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
public class AccessSource implements Closeable
{
	private BufferedReader bufferedReader;
	private long accessAge;

	public AccessSource(File accessFile) throws FileNotFoundException
	{
		this.bufferedReader = new BufferedReader(new FileReader(accessFile));
		this.accessAge = accessFile.lastModified();
	}

	public long getAccessAge()
	{
		return accessAge;
	}

	public String read() throws IOException
	{
		return bufferedReader.readLine();
	}

	public double readDouble() throws NumberFormatException, IOException
	{
		return Double.parseDouble(read());
	}

	public int readInt() throws NumberFormatException, IOException
	{
		return Integer.parseInt(read());
	}

	public byte readByte() throws NumberFormatException, IOException
	{
		return (byte) (readInt() - 127);
	}

	public boolean readBoolean() throws IOException
	{
		return Boolean.parseBoolean(read());
	}

	@Override
	public void close() throws IOException
	{
		bufferedReader.close();
	}
}
