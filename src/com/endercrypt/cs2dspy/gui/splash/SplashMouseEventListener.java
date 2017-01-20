package com.endercrypt.cs2dspy.gui.splash;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SplashMouseEventListener implements MouseListener
{
	private SplashWindow splashWindow;

	public SplashMouseEventListener(SplashWindow splashWindow)
	{
		this.splashWindow = splashWindow;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		splashWindow.waitingThread.interrupt();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}
}
