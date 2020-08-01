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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;


class PropertyParser {
    /**
     * Returns the int value associated with property named key.
     *
     * Returns defaultValue if there is no such property, or if the property
     * value cannot be converted to an int.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue) {
        String value = System.getenv(key);

        // handle key missing or key=[empty string] in .ini file
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Utils.logToConsole(
                "Invalid number \""
                + value
                + "\" for property \""
                + key
                + "\"");
            return defaultValue;
        }
    }

    /**
     * Returns the value associated with property named key.
     *
     * Returns defaultValue if no such property.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }

        return value;
    }

    /**
     * Returns the boolean value associated with property named key.
     *
     * Returns defaultValue if there is no such property, or if the property
     * value cannot be converted to a boolean.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = System.getenv(key);

        // handle key missing or key=[empty string] in .ini file
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        if (value.equalsIgnoreCase("true")) {
            return true;
        } else if (value.equalsIgnoreCase("yes")) {
            return true;
        } else if (value.equalsIgnoreCase("false")) {
            return false;
        } else if (value.equalsIgnoreCase("no")) {
            return false;
        } else {
            return defaultValue;
        }
    }
}
