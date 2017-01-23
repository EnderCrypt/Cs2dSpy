package com.endercrypt.cs2dspy.gui.fps;

public class FpsFrame
{
	private long startTime;
	private int delay;
	private long endTime;

	public FpsFrame()
	{
		startTime = System.currentTimeMillis();
	}

	protected void finish()
	{
		endTime = System.currentTimeMillis();
		delay = (int) (endTime - startTime);
	}

	public long getStartTime()
	{
		return startTime;
	}

	public int getDelay()
	{
		return delay;
	}

	public long getEndTime()
	{
		return endTime;
	}
}
