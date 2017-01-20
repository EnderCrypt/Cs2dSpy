package com.endercrypt.cs2dspy.gui.splash;

import com.endercrypt.cs2dspy.gui.AwtWindow;

public class SplashWaitingRunnable implements Runnable
{
	private static int DELAY_SECONDS = 10;
	private AwtWindow window;

	public SplashWaitingRunnable(AwtWindow window)
	{
		this.window = window;
	}

	@Override
	public void run()
	{
		try
		{
			Thread.sleep(DELAY_SECONDS * 1000);
		}
		catch (InterruptedException e)
		{
			// ignore and dispose splash screen
		}
		window.dispose();
	}
}
