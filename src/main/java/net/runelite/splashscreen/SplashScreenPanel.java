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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import net.runelite.splashscreen.util.FontManager;
import net.runelite.splashscreen.util.SwingUtil;

@Getter
public class SplashScreenPanel extends JPanel
{
	private static final BufferedImage TRANSPARENT_LOGO;
	static
	{
		TRANSPARENT_LOGO = SwingUtil.loadImage("runelite_transparent.png");
	}

	private final JProgressBar bar = new JProgressBar(0, 100);
	private final JLabel messageLabel = new JLabel("Loading...");
	private final JLabel subMessageLabel = new JLabel();

	public SplashScreenPanel(final String versionString)
	{
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(RuneLiteSplashScreen.FRAME_SIZE);
		this.setBackground(SwingUtil.ColorScheme.DARKER_GRAY_COLOR);

		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = 5;

		// Logo
		final ImageIcon transparentLogo = new ImageIcon(TRANSPARENT_LOGO.getScaledInstance(128, 128, Image.SCALE_SMOOTH));
		final JLabel logo = new JLabel(transparentLogo);
		logo.setBorder(new EmptyBorder(35, 0, 0, 0));
		this.add(logo, c);
		c.gridy++;

		// runelite title
		final JLabel title = new JLabel("RuneLite");
		title.setFont(FontManager.getRunescapeFont());
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title, c);
		c.gridy++;

		// version
		final JLabel version = new JLabel(versionString);
		version.setForeground(Color.LIGHT_GRAY);
		version.setFont(FontManager.getRunescapeSmallFont());
		version.setHorizontalAlignment(JLabel.CENTER);
		this.add(version, c);
		c.gridy++;

		// progressbar
		final GridBagConstraints progressConstraints = new GridBagConstraints();
		progressConstraints.insets = new Insets(0, 30, 5, 30);
		progressConstraints.fill = GridBagConstraints.HORIZONTAL;
		progressConstraints.anchor = GridBagConstraints.SOUTH;
		progressConstraints.gridy = c.gridy;

		bar.setVisible(true);
		this.add(bar, progressConstraints);
		c.gridy++;

		// main message
		messageLabel.setFont(FontManager.getRunescapeSmallFont());
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(messageLabel, c);
		c.gridy++;

		// alternate message
		subMessageLabel.setFont(FontManager.getRunescapeSmallFont());
		subMessageLabel.setForeground(subMessageLabel.getForeground().darker());
		subMessageLabel.setHorizontalAlignment(JLabel.CENTER);
		c.anchor = GridBagConstraints.NORTH;
		c.weighty = 1;
		this.add(subMessageLabel, c);
		c.gridy++;
	}
}
