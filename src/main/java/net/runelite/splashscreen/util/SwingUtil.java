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
package net.runelite.splashscreen.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Enumeration;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicProgressBarUI;
import lombok.extern.slf4j.Slf4j;
import net.runelite.splashscreen.RuneLiteSplashScreen;

@Slf4j
public class SwingUtil
{
	/* The orange color used for the branding's accents */
	private static final Color BRAND_ORANGE = new Color(220, 138, 0);
	/* The orange color used for the branding's accents, with lowered opacity */
	private static final Color BRAND_ORANGE_TRANSPARENT = new Color(220, 138, 0, 120);
	private static final Color DARKER_GRAY_COLOR = new Color(30, 30, 30);

	public static Font RUNESCAPE_FONT;
	public static Font RUNESCAPE_FONT_SMALL;
	static
	{
		try
		{
			RUNESCAPE_FONT = Font.createFont(Font.TRUETYPE_FONT,
				SwingUtil.class.getResourceAsStream("runescape.ttf"))
				.deriveFont(Font.PLAIN, 16);
			RUNESCAPE_FONT_SMALL = Font.createFont(Font.TRUETYPE_FONT,
				SwingUtil.class.getResourceAsStream("runescape_small.ttf"))
				.deriveFont(Font.PLAIN, 16);
		}
		catch (Exception e)
		{
			log.warn("Error loading fonts", e);
		}
	}

	/**
	 * Safely sets Swing theme
	 */
	public static void setTheme()
	{
		UIManager.put("ProgressBar.background", BRAND_ORANGE_TRANSPARENT.darker());
		UIManager.put("ProgressBar.foreground", BRAND_ORANGE);
		UIManager.put("ProgressBar.selectionBackground", BRAND_ORANGE);
		UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
		UIManager.put("ProgressBar.border", new EmptyBorder(0, 0, 0, 0));
		UIManager.put("ProgressBar.verticalSize", new Dimension(12, 10));
		UIManager.put("ProgressBar.horizontalSize", new Dimension(10, 12));
		UIManager.put("ProgressBarUI", BasicProgressBarUI.class.getName());
		UIManager.put("Panel.background", DARKER_GRAY_COLOR);
		UIManager.put("Panel.foreground", Color.WHITE);
		UIManager.put("Label.background", DARKER_GRAY_COLOR);
		UIManager.put("Label.foreground", Color.WHITE);
		UIManager.put("OptionPane.background", DARKER_GRAY_COLOR);
		UIManager.put("OptionPane.messageForeground", Color.WHITE);

		// Default all fonts to RuneScape font
		final FontUIResource f = new FontUIResource(RUNESCAPE_FONT);
		final Enumeration keys = UIManager.getDefaults().keys();

		while (keys.hasMoreElements())
		{
			final Object key = keys.nextElement();

			if (UIManager.get(key) instanceof FontUIResource)
			{
				UIManager.put(key, f);
			}
		}
	}

	public static BufferedImage loadImage(final String name)
	{
		try
		{
			synchronized (ImageIO.class)
			{
				return ImageIO.read(RuneLiteSplashScreen.class.getResourceAsStream(name));
			}
		}
		catch (IOException e)
		{
			log.warn("Error loading logo", e);
			return null;
		}
	}
}
