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
import java.awt.Dimension;
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
class SplashScreenPanel extends JPanel
{
	private static final BufferedImage TRANSPARENT_LOGO = SwingUtil.loadImage("runelite_transparent.png");
	private static final Dimension VERSION_SIZE = new Dimension(RuneLiteSplashScreen.FRAME_SIZE.width, 40);

	private final JProgressBar bar = new JProgressBar(0, 100);
	private final JLabel messageLabel = new JLabel("Loading...");
	private final JLabel subMessageActionLabel = new JLabel();
	private final JLabel subMessageLabel = new JLabel();

	SplashScreenPanel(final String versionString)
	{
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(RuneLiteSplashScreen.FRAME_SIZE);

		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = 25;

		// Logo
		final ImageIcon transparentLogo = new ImageIcon();
		if (TRANSPARENT_LOGO != null)
		{
			transparentLogo.setImage(TRANSPARENT_LOGO.getScaledInstance(128, 128, Image.SCALE_SMOOTH));
		}
		final JLabel logo = new JLabel(transparentLogo);
		logo.setBorder(new EmptyBorder(15, 0, 0, 0));
		this.add(logo, c);
		c.gridy++;

		// main message
		messageLabel.setFont(FontManager.getRunescapeFont());
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(messageLabel, c);
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

		// alternate message action
		subMessageActionLabel.setFont(FontManager.getRunescapeSmallFont());
		subMessageActionLabel.setForeground(subMessageLabel.getForeground().darker());
		subMessageActionLabel.setHorizontalAlignment(JLabel.CENTER);

		c.anchor = GridBagConstraints.NORTH;
		c.ipady = 15;
		this.add(subMessageActionLabel, c);
		c.gridy++;

		// alternate message
		subMessageLabel.setFont(FontManager.getRunescapeSmallFont());
		subMessageLabel.setForeground(subMessageLabel.getForeground().darker());
		subMessageLabel.setHorizontalAlignment(JLabel.CENTER);
		subMessageLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
		
		c.weighty = 1;
		this.add(subMessageLabel, c);
		c.gridy++;

		// version
		final JLabel version = new JLabel(versionString);
		version.setFont(FontManager.getRunescapeSmallFont());
		version.setHorizontalAlignment(JLabel.CENTER);
		version.setForeground(new Color(136, 136, 136));
		version.setBackground(new Color(39, 39, 39));
		version.setOpaque(true);
		version.setPreferredSize(VERSION_SIZE);
		version.setMinimumSize(VERSION_SIZE);

		c.anchor = GridBagConstraints.PAGE_END;
		c.weighty = 0;
		c.ipady = 0;
		this.add(version, c);
	}
}
