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


public class SettingsParser {
    private Settings _settings;

    public SettingsParser(Settings settings) {
        _settings = settings;
    }

    public void parse() {
        _setTradingMode();
    }

    private void _setTradingMode()
    {
        String mode = _settings.getString("TradingMode", "");
        if (mode.length() > 0) {
            Utils.logToConsole("Trading mode set from config: " + mode);
            _settings.setTradingMode(TradingMode.fromString(mode));
        }
    }
}
