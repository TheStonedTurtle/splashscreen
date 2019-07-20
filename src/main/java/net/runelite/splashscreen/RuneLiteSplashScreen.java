/*
 * Copyright (c) 2019, TheStonedTurtle <https://github.com/TheStonedTurtle>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.splashscreen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.splashscreen.util.SwingUtil;
import net.runelite.splashscreen.util.LinkBrowser;

@Slf4j
public class RuneLiteSplashScreen extends JFrame
{
	public static final Dimension FRAME_SIZE = new Dimension(200, 275);
	private static final BufferedImage LOGO = SwingUtil.loadImage("runelite.png");

	@Getter
	private final SplashScreenPanel panel;
	private final File logFile;

	@Setter
	private long fetchBytes;
	private long processedBytes;

	@Setter
	private float progressStartingPercent = 0f;
	@Setter
	private float progressEndingPercent = 100f;

	public RuneLiteSplashScreen(final File logFile, final String versionText)
	{
		this.logFile = logFile;
		this.setTitle("RuneLite");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(FRAME_SIZE);
		this.setLayout(new BorderLayout());
		this.setUndecorated(true);
		this.setIconImage(LOGO);
		this.setShape(new RoundRectangle2D.Double(0, 0, FRAME_SIZE.width, FRAME_SIZE.height, 15, 15));

		panel = new SplashScreenPanel(versionText);
		this.setContentPane(panel);
		pack();

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void setTheme()
	{
		SwingUtil.setTheme();
	}

	public void setMessage(final String msg, final int value)
	{
		panel.getMessageLabel().setText(msg);
		panel.getBar().setValue(value);
		setSubMessage(null);

		panel.revalidate();
		panel.repaint();
	}

	public void setSubMessage(final String msg)
	{
		final JLabel label = panel.getSubMessageLabel();
		label.setText(msg);

		label.revalidate();
		label.repaint();
	}

	public void progress(String filename, int bytes, int total)
	{
		if (total == 0)
		{
			return;
		}

		// Since we are using the same progress bar we need to specifying a start and end point for the bar
		// This gives us a specified amount of the bar to evenly distribute between downloaded files
		final float progressPercentage = progressEndingPercent - progressStartingPercent;
		final float percent = ((float) (bytes + processedBytes)) / ((float) fetchBytes) * progressPercentage;
		final int actualPercent = (int) (percent + progressStartingPercent);

		final JProgressBar bar = panel.getBar();
		// bar starts at 25%
		bar.setValue(actualPercent);

		setSubMessage("Downloading " + filename + "...");

		panel.revalidate();
		panel.repaint();
	}

	public void processed(final int processedBytes)
	{
		this.processedBytes += processedBytes;
	}

	public void invalidVersion()
	{
		invalidVersion(panel);
	}

	public static void invalidVersion(final JComponent parent)
	{
		final String message = "<html><div style='text-align: center;'>" +
			"Your RuneLite launcher version is outdated<br/>" +
			"Please visit runelite.net to download the updated version</div></html>";
		final Object[] buttons = new Object[]{"Visit runelite.net", "Okay"};

		final int result = JOptionPane.showOptionDialog(parent,
			message,
			"Outdated launcher",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.ERROR_MESSAGE,
			null,
			buttons,
			buttons[1]);

		if (result == JOptionPane.YES_OPTION)
		{
			LinkBrowser.browse("https://runelite.net");
		}
	}

	public void errorMessage(final String message)
	{
		errorMessage(panel, message, logFile);
	}

	public static void errorMessage(final JComponent parent, final String msg, final File logFile)
	{
		final Object[] buttons = new Object[]{"View log file", "Okay"};
		final int result = JOptionPane.showOptionDialog(parent,
			msg,
			"RuneLite Error",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.ERROR_MESSAGE,
			null,
			buttons,
			buttons[1]);

		if (result == JOptionPane.YES_OPTION)
		{
			LinkBrowser.openLocalFile(logFile);
		}
	}

	public void close()
	{
		this.setVisible(false);
		this.dispose();
	}
}
