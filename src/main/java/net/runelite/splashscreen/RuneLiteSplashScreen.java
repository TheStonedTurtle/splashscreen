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
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.splashscreen.util.LinkBrowser;
import net.runelite.splashscreen.util.SwingUtil;

@Slf4j
public class RuneLiteSplashScreen extends JFrame
{
	static final Dimension FRAME_SIZE = new Dimension(600, 350);

	@Getter
	private final MessagePanel messagePanel = new MessagePanel();
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
		this.setIconImage(SwingUtil.LOGO);

		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(RuneLiteSplashScreen.FRAME_SIZE);

		panel.add(new InfoPanel(versionText), BorderLayout.EAST);
		panel.add(messagePanel, BorderLayout.WEST);

		this.setContentPane(panel);
		pack();

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void setBarText(final String text)
	{
		final JProgressBar bar = messagePanel.getBar();
		bar.setString(text);
		bar.setStringPainted(text != null);
		bar.revalidate();
		bar.repaint();
	}

	public void setMessage(final String msg, final int value)
	{
		messagePanel.getBarLabel().setText(msg);
		messagePanel.getBar().setValue(value);
		setBarText(null);

		this.getContentPane().revalidate();
		this.getContentPane().repaint();
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

		final JProgressBar bar = messagePanel.getBar();
		bar.setValue(actualPercent);
		setBarText("Downloading " + filename);

		this.getContentPane().revalidate();
		this.getContentPane().repaint();
	}

	public void processed(final int processedBytes)
	{
		this.processedBytes += processedBytes;
	}

	public void invalidVersion()
	{
		invalidVersion(messagePanel);
	}

	public static void invalidVersion(final JComponent parent)
	{
		final String message = "<html><div style='text-align: center;'>" +
			"Your RuneLite launcher version is outdated<br/>" +
			"Please visit runelite.net to download the updated version</div></html>";
		final JButton[] buttons = new JButton[]{
			SwingUtil.createFlatButton("Visit runelite.net", JOptionPane.YES_OPTION),
			SwingUtil.createFlatButton("Okay", JOptionPane.NO_OPTION)
		};

		final int result = SwingUtil.showRuneLiteOptionPane(parent, message, JOptionPane.YES_NO_OPTION, buttons);

		if (result == JOptionPane.YES_OPTION)
		{
			LinkBrowser.browse("https://runelite.net");
		}
	}

	public void errorMessage(final String message)
	{
		errorMessage(messagePanel, message, logFile);
	}

	public static void errorMessage(final JComponent parent, String msg, final File logFile)
	{
		final JButton[] buttons = new JButton[]{
			SwingUtil.createFlatButton("View log file", JOptionPane.YES_OPTION),
			SwingUtil.createFlatButton("Okay", JOptionPane.NO_OPTION)
		};

		if (!msg.startsWith("<html>"))
		{
			msg = "<html><div style='text-align:center'>" + msg + "</div></html>";
		}

		final int result = SwingUtil.showRuneLiteOptionPane(parent, msg, JOptionPane.YES_NO_OPTION, buttons);

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
