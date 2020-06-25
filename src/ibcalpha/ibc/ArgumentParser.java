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


public class ArgumentParser {
    private Settings _settings;

    public ArgumentParser(Settings settings) {
        _settings = settings;
    }

    public void parse(String[] args) {
        _parseTradingMode(args);
        _parseCredentials(args);
    }

    /*
     * Must be in either args[1] (if there are two args), or args[3] (if there are
     * four args), or args[5] (if there are six args)
    */
    private void _parseTradingMode(String[] args) {
        String mode = null;
        if (args.length == 2) {
            mode = args[1];
        } else if (args.length == 4) {
            mode = args[3];
        } else if (args.length == 6) {
            mode = args[5];
        }

        if (mode != null) {
            Utils.logToConsole("Trading mode set from arguments: " + mode);
            _settings.setTradingMode(TradingMode.fromString(mode));
        }
    }

    public void _parseCredentials(String[] args) {
        if (_settings.getFixEnabled()) {
            loadApiCredentialsFromArgs(args);
            loadFixCredentialsFromArgs(args);
        } else {
            loadApiCredentialsFromArgs(args);
        }
    }

    private boolean loadFixCredentialsFromArgs(String[] args) {
        if (args.length >= 3 && args.length <= 6) {
            _settings.setFixLoginId(args[1]);
            _settings.setFixPassword(args[2]);
            Utils.logToConsole("FIX credentials set from arguments");
            return true;
        }

        return false;
    }

    private boolean loadApiCredentialsFromArgs(String[] args) {
        if (Settings.settings().getFixEnabled()) {
            if (args.length == 5 || args.length == 6) {
                _settings.setIbLoginId(args[3]);
                _settings.setIbPassword(args[4]);
                Utils.logToConsole("IB credentials set from arguments");
                return true;
            }
            return false;
        } else if (args.length == 3 || args.length == 4) {
            _settings.setIbLoginId(args[1]);
            _settings.setIbPassword(args[2]);
            Utils.logToConsole("IB credentials set from arguments");
            return true;
        } else if (args.length == 5 || args.length == 6) {
            Utils.logError("Incorrect number of arguments passed. quitting...");
            Utils.logRawToConsole("Number of arguments = " +args.length + " which is only permitted if FIX=yes");
            for (String arg : args) {
                Utils.logRawToConsole(arg);
            }
            Utils.exitWithError(ErrorCodes.ERROR_CODE_INCORRECT_NUMBER_OF_ARGUMENTS);
            return false;
        } else {
            return false;
        }
    }
}
