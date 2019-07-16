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

import java.io.File;

class RuneLiteSplashScreenTest
{
	private static final File LOG_FILE = new File(System.getProperty("user.home"), ".runelite/logs/client.log");

	public static void main(String[] args) throws Exception
	{
		RuneLiteSplashScreen.setTheme();
		RuneLiteSplashScreen.invalidVersion(null);
		RuneLiteSplashScreen.errorMessage(null, "Error message goes here", LOG_FILE);

		final RuneLiteSplashScreen frame = new RuneLiteSplashScreen(LOG_FILE, "Version");
		frame.setMessage("Testing", 0);

		// Intentionally don't close after this call
		frame.invalidVersion();

		for (int i = 0; i <= 20; i += 5)
		{
			frame.setMessage("Testing", i);
			Thread.sleep(300);
		}

		frame.setProgressStartingPercent(20);
		frame.setProgressEndingPercent(80);
		final int[] fakeBytes = new int[]{10000000, 5400000, 900000, 13000000};
		frame.setFetchBytes(10000000 + 5400000 + 900000 + 13000000);
		for (int byteIdx = 0; byteIdx < fakeBytes.length; byteIdx++)
		{
			final int totalBytes = fakeBytes[byteIdx];
			for (int i = 1; i <= totalBytes; i++)
			{
				frame.progress("test file " + (byteIdx + 1), i, totalBytes);
			}
			frame.processed(totalBytes);
			Thread.sleep(1000);
		}

		for (int i = 80; i <= 100; i++)
		{
			frame.setMessage("Testing", i);
			frame.setSubMessage(i + "%");
			Thread.sleep(100);
		}

		frame.errorMessage("Example error message");

		frame.close();
		System.exit(0);
	}
}
