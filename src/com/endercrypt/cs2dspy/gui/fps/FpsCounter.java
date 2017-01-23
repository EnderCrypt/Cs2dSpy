package com.endercrypt.cs2dspy.gui.fps;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class FpsCounter
{
	private Deque<FpsFrame> frames = new ArrayDeque<>();

	private int totalTime = 0;

	public FpsCounter()
	{

	}

	public int getFps()
	{
		validate();
		synchronized (frames)
		{
			return frames.size();
		}
	}

	public double averageFrameMs()
	{
		return ((double) totalTime / getFps());
	}

	public void add(FpsFrame frame)
	{
		frame.finish();
		synchronized (frames)
		{
			frames.add(frame);
			totalTime += frame.getDelay();
		}
	}

	private void validate()
	{
		long currentTime = System.currentTimeMillis();
		long lastSecond = currentTime - 1000;
		Iterator<FpsFrame> iterator = frames.iterator();
		while (iterator.hasNext())
		{
			FpsFrame frame = iterator.next();
			if (frame.getEndTime() < lastSecond)
			{
				synchronized (frames)
				{
					totalTime -= frame.getDelay();
					iterator.remove();
				}
			}
			else
			{
				break;
			}
		}
	}
}
