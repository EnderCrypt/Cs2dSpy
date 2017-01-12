package com.endercrypt.cs2dspy.gui.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;

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
public class Keyboard
{
	public enum BindType
	{
		PRESS,
		HOLD,
		RELEASE;
	}

	private Map<KeyBinding, Set<AppKeyListener>> bindings = new HashMap<>();
	private Set<Integer> keysDown = new HashSet<>();

	public Keyboard(JFrame jFrame)
	{
		jFrame.addKeyListener(new KeyboardListener());
	}

	public void update()
	{
		for (Entry<KeyBinding, Set<AppKeyListener>> entry : bindings.entrySet())
		{
			KeyBinding keyBinding = entry.getKey();
			if ((keyBinding.bindType == BindType.HOLD) && keysDown.contains(keyBinding.keyCode))
			{
				Set<AppKeyListener> appKeyListeners = entry.getValue();
				for (AppKeyListener appKeyListener : appKeyListeners)
				{
					appKeyListener.keyTriggered(keyBinding.keyCode, keyBinding.bindType);
				}
			}
		}
	}

	private void trigger(KeyBinding keyBinding)
	{
		Set<AppKeyListener> appKeyListeners = bindings.get(keyBinding);
		if (appKeyListeners != null)
		{
			for (AppKeyListener appKeyListener : appKeyListeners)
			{
				appKeyListener.keyTriggered(keyBinding.keyCode, keyBinding.bindType);
			}
		}
	}

	public void bindKey(int keyCode, BindType bindType, AppKeyListener appKeyListener)
	{
		KeyBinding keyBinding = new KeyBinding(keyCode, bindType);
		Set<AppKeyListener> appKeyListeners = bindings.get(keyBinding);
		if (appKeyListeners == null)
		{
			appKeyListeners = new HashSet<>();
			bindings.put(keyBinding, appKeyListeners);
		}
		appKeyListeners.add(appKeyListener);
	}

	private class KeyBinding
	{
		private int keyCode;
		private BindType bindType;

		protected KeyBinding(int keyCode, BindType bindType)
		{
			this.keyCode = keyCode;
			this.bindType = bindType;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((bindType == null) ? 0 : bindType.hashCode());
			result = prime * result + keyCode;
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			KeyBinding other = (KeyBinding) obj;
			if (bindType != other.bindType) return false;
			if (keyCode != other.keyCode) return false;
			return true;
		}
	}

	private class KeyboardListener implements KeyListener
	{
		@Override
		public void keyTyped(KeyEvent e)
		{
			// unused
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			int keyCode = e.getKeyCode();
			synchronized (keysDown)
			{
				keysDown.add(keyCode);
			}
			Keyboard.this.trigger(new KeyBinding(keyCode, BindType.PRESS));
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			int keyCode = e.getKeyCode();
			synchronized (keysDown)
			{
				keysDown.remove(keyCode);
			}
			Keyboard.this.trigger(new KeyBinding(keyCode, BindType.RELEASE));
		}
	}
}
