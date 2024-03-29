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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import net.runelite.splashscreen.util.LinkBrowser;
import net.runelite.splashscreen.util.SwingUtil;

public class InfoPanel extends JPanel
{
	private static final BufferedImage TRANSPARENT_LOGO = SwingUtil.loadImage("runelite_transparent.png");
	static final Dimension PANEL_SIZE = new Dimension(200, RuneLiteSplashScreen.FRAME_SIZE.height);
	private static final Dimension VERSION_SIZE = new Dimension(PANEL_SIZE.width, 30);
	private static final File RUNELITE_DIR = new File(System.getProperty("user.home"), ".runelite");
	private static final File LOGS_DIR = new File(RUNELITE_DIR, "logs");
	private static final String TROUBLESHOOTING_URL = "https://github.com/runelite/runelite/wiki/Troubleshooting-problems-with-the-client";
	private static final String DISCORD_INVITE_LINK = "https://discord.gg/mePCs8U";

	InfoPanel(final String versionString)
	{
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(PANEL_SIZE);
		this.setBackground(new Color(38, 38, 38));

		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = 5;

		// Logo
		final ImageIcon transparentLogo = new ImageIcon();
		if (TRANSPARENT_LOGO != null)
		{
			transparentLogo.setImage(TRANSPARENT_LOGO.getScaledInstance(128, 128, Image.SCALE_SMOOTH));
		}
		final JLabel logo = new JLabel(transparentLogo);
		logo.setBorder(new EmptyBorder(25, 0, 0, 0));

		c.anchor = GridBagConstraints.NORTH;
		c.weighty = 1;
		this.add(logo, c);
		c.gridy++;
		c.anchor = GridBagConstraints.SOUTH;
		c.weighty = 0;

		final JLabel logsFolder = createPanelButton("Open logs folder", null, () -> LinkBrowser.openLocalFile(LOGS_DIR));
		this.add(logsFolder, c);
		c.gridy++;

		final JLabel discord = createPanelButton("Get help on Discord", "Instant invite link to join the RuneLite discord", () -> LinkBrowser.browse(DISCORD_INVITE_LINK));
		this.add(discord, c);
		c.gridy++;

		final JLabel troubleshooting = createPanelButton("Troubleshooting steps", "Opens a link to the troubleshooting wiki", () -> LinkBrowser.browse(TROUBLESHOOTING_URL));
		this.add(troubleshooting, c);
		c.gridy++;

		final JLabel exit = createPanelButton("Exit", "Closes the application immediately", () -> System.exit(0));
		this.add(exit, c);
		c.gridy++;

		// version
		final JLabel version = new JLabel(versionString);
		version.setFont(SwingUtil.RUNESCAPE_FONT_SMALL);
		version.setHorizontalAlignment(JLabel.CENTER);
		version.setForeground(new Color(136, 136, 136));
		version.setBackground(this.getBackground().brighter());
		version.setOpaque(true);
		version.setPreferredSize(VERSION_SIZE);
		version.setMinimumSize(VERSION_SIZE);
		version.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		this.add(version, c);
	}

	private static JLabel createPanelButton(final String name, final String tooltip, final Runnable runnable)
	{
		final JLabel btn = new JLabel(name, JLabel.CENTER);
		btn.setToolTipText(tooltip);
		btn.setOpaque(true);
		btn.setBackground(null);
		btn.setForeground(Color.WHITE);
		btn.setFont(SwingUtil.RUNESCAPE_FONT);
		btn.setBorder(new CompoundBorder(
			new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
			new EmptyBorder(3, 0, 3, 0))
		);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				runnable.run();
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				btn.setBackground(new Color(60, 60, 60));
				btn.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				btn.setBackground(null);
				btn.repaint();
			}
		});

		return btn;
	}
}
