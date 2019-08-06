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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.splashscreen.RuneLiteSplashScreen;

@Slf4j
public class SwingUtil
{
	/* The orange color used for the branding's accents */
	public static final Color BRAND_ORANGE = new Color(220, 138, 0);
	/* The orange color used for the branding's accents, with lowered opacity */
	public static final Color BRAND_ORANGE_TRANSPARENT = new Color(220, 138, 0, 120);
	public static final Color DARKER_GRAY_COLOR = new Color(30, 30, 30);
	public static final Color BUTTON_HOVER_COLOR = new Color(60, 60, 60);
	/* The background color of the scrollbar's track */
	public static final Color SCROLL_TRACK_COLOR = new Color(25, 25, 25);
	public static final Color MEDIUM_GRAY_COLOR = new Color(77, 77, 77);

	public static final Border BUTTON_BORDER = new EmptyBorder(5, 17, 5, 17);
	public static final Border BORDERED_BUTTON_BORDER = new CompoundBorder(
		new MatteBorder(1, 1, 1, 1, Color.BLACK),
		new EmptyBorder(4, 16, 4, 16)
	);

	public static final BufferedImage LOGO = SwingUtil.loadImage("runelite.png");

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

	/**
	 * Creates a custom {@link JButton} with a flat design for use inside {@link JOptionPane}.
	 * The button will display the passed {@code text} and set the value of the pane to {@code buttonOption} on click
	 * @param text text to be displayed inside the button
	 * @param buttonOption the code to be set via {@link JOptionPane#setValue(Object)}
	 * @return newly created {@link JButton}
	 */
	public static JButton createFlatButton(final String text, final int buttonOption)
	{
		final JButton button = new JButton(text);
		button.setForeground(Color.WHITE);
		button.setBackground(Color.BLACK);
		button.setFont(SwingUtil.RUNESCAPE_FONT);
		button.setBorder(BUTTON_BORDER);

		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(true);

		// Selecting the button option requires us to determine which parent element is the JOptionPane
		button.addActionListener(e -> {
			JComponent component = (JComponent) e.getSource();
			while (component != null)
			{
				if (component instanceof JOptionPane)
				{
					((JOptionPane) component).setValue(buttonOption);
					component = null;
				}
				else
				{
					component = component.getParent() == null ? null : (JComponent) component.getParent();
				}
			}
		});

		// Use change listener instead of mouse listener for buttons
		button.getModel().addChangeListener(e ->
		{
			final ButtonModel model = (ButtonModel) e.getSource();
			button.setBackground(model.isRollover() ? SwingUtil.BUTTON_HOVER_COLOR : Color.BLACK);
			button.setBorderPainted(model.isPressed());
			button.setBorder(model.isPressed() ? BORDERED_BUTTON_BORDER : BUTTON_BORDER);
		});

		return button;
	}

	/**
	 * Opens a {@link JDialog} with a stylized {@link JOptionPane} ignoring UIManager defaults.
	 * The buttons should be created via the {@link #createFlatButton(String, int)} function to look correctly
	 * @param component The frame the dialog should be attached to. nullable
	 * @param content The string content to be added to the content pane
	 * @param optionType The JOptionPane option type of dialog pane to create
	 * @param buttons Buttons to display, created via {@link #createFlatButton(String, int)}
	 * @return The Integer value representing the button selected
	 */
	public static int showRuneLiteOptionPane(final JComponent component, final String content, final int optionType, final JButton[] buttons)
	{
		final JLabel contentLabel = new JLabel(content);
		contentLabel.setFont(RUNESCAPE_FONT);
		contentLabel.setForeground(Color.WHITE);
		contentLabel.setBackground(DARKER_GRAY_COLOR);

		final JPanel p = new JPanel(new BorderLayout());
		p.setBackground(DARKER_GRAY_COLOR);
		p.setForeground(Color.WHITE);
		p.add(contentLabel, BorderLayout.NORTH);

		final JOptionPane pane = new JOptionPane(p,
			JOptionPane.ERROR_MESSAGE,
			optionType,
			null,
			buttons,
			buttons[1]);
		pane.setBackground(DARKER_GRAY_COLOR);
		pane.setForeground(Color.WHITE);
		stylizeJPanels(pane);

		final Frame frame = component == null ? JOptionPane.getRootFrame() : JOptionPane.getFrameForComponent(component);
		final JDialog dialog = new JDialog(frame, "RuneLite Error", true);
		dialog.setContentPane(pane);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setAlwaysOnTop(true);
		dialog.setAutoRequestFocus(true);
		dialog.setLocationRelativeTo(null);
		dialog.setIconImage(LOGO);

		// Listen for value changes and close dialog when necessary
		pane.addPropertyChangeListener(e -> {
			String prop = e.getPropertyName();

			if (dialog.isVisible()
				&& (e.getSource() == pane)
				&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
				dialog.setVisible(false);
			}
		});

		dialog.pack();
		// Try to center dialog based on its size
		dialog.setLocation(dialog.getX() - dialog.getSize().width / 2, dialog.getY() - dialog.getSize().height / 2);
		dialog.setVisible(true);

		return (Integer) pane.getValue();
	}

	private static void stylizeJPanels(final JComponent component)
	{
		for (final Component c : component.getComponents())
		{
			if (c instanceof JPanel)
			{
				c.setBackground(DARKER_GRAY_COLOR);
				c.setForeground(Color.WHITE);
				stylizeJPanels((JComponent) c);
			}
		}
	}
}
