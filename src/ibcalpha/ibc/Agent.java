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

import java.util.ArrayList;
import java.util.List;

import ibcalpha.ibc.IbcTws;


class Agent {
    public static boolean isGateway() {
        String command = System.getProperty("sun.java.command");
        if (command.startsWith("install4j.ibgateway")) {
            return true;
        }

        if (! command.startsWith("install4j.jclient")) {
            return false;
        }

        System.out.println("Unrecognized start class: ".concat(command));
        //throw IllegalStateException(command);
        return false;
    }

    // https://stackoverflow.com/questions/1082953/shlex-alternative-for-java
    static private List<String> shellSplit(CharSequence string) {
        List<String> tokens = new ArrayList<String>();
        boolean escaping = false;
        char quoteChar = ' ';
        boolean quoting = false;
        int lastCloseQuoteIndex = Integer.MIN_VALUE;
        StringBuilder current = new StringBuilder();
        for (int i = 0; i<string.length(); i++) {
            char c = string.charAt(i);
            if (escaping) {
                current.append(c);
                escaping = false;
            } else if (c == '\\' && !(quoting && quoteChar == '\'')) {
                escaping = true;
            } else if (quoting && c == quoteChar) {
                quoting = false;
                lastCloseQuoteIndex = i;
            } else if (!quoting && (c == '\'' || c == '"')) {
                quoting = true;
                quoteChar = c;
            } else if (!quoting && Character.isWhitespace(c)) {
                if (current.length() > 0 || lastCloseQuoteIndex == (i - 1)) {
                    tokens.add(current.toString());
                    current = new StringBuilder();
                }
            } else {
                current.append(c);
            }
        }
        if (current.length() > 0 || lastCloseQuoteIndex == (string.length() - 1)) {
            tokens.add(current.toString());
        }

        return tokens;
    }

    public static void premain(String agentArgs) {
        if (isGateway()) {
            System.out.println("Running in gateway");
        } else {
            System.out.println("Running in TWS");
        }

        if (agentArgs == null || agentArgs.equals("")) {
            agentArgs = "NULL";
        }

        String[] args = shellSplit(agentArgs).toArray(new String[0]);
        System.out.println("Parsed arguments: ".concat(String.join("|", args)));

        try {
            Settings.settings().setIsGateway(isGateway());
            IbcTws.installExceptionHandler();
            IbcTws.setupDefaultEnvironment(args);
            IbcTws.initialize();
            //IbcTws.startTwsOrGateway();
            IbcTws.startInitialTasks();
        } catch (Exception e) {
            System.out.println("Could not start: ".concat(e.toString()));
            return;
        }
    }
}
