// This file is part of IBC.
// Copyright (C) 2004 Steven M. Kearns (skearns23@yahoo.com )
// Copyright (C) 2004 - 2018 Richard L King (rlking@aultan.com)
// For conditions of distribution and use, see copyright notice in COPYING.txt

// IBC is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// IBC is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with IBC.  If not, see <http://www.gnu.org/licenses/>.

package ibcalpha.ibc;

import java.io.OutputStreamWriter;

import org.apache.logging.log4j.core.appender.WriterAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.LogManager;


public class ConsoleRedirector {
    private static boolean _done = false;

    public static void onLoginWindow() {
        if(_done || !Settings.settings().sendTwsLogsToConsole()) {
            return;
        }

        WriterAppender appender = WriterAppender.createAppender(
            PatternLayout.newBuilder()
                .withPattern("%d [%t] %p %c - %m%n")
                .build(),
            null,                                           // Filter
            new OutputStreamWriter(Utils.getOutStream()),   // Writer
            "ConsoleLoggingAppender",                       // name
            false,                                          // follow
            true                                            // ignore exceptions
        );
        appender.start();

        LoggerContext context = (LoggerContext) LogManager.getContext(
            /* Referencing any twslaunch class is sufficient. */
            twslaunch.a.class.getClassLoader(),
            false
        );

        Configuration config = context.getConfiguration();
        config.addAppender(appender);
        config.getRootLogger().addAppender(appender, null, null);
        context.updateLoggers(config);
        _done = true;
    }
}
