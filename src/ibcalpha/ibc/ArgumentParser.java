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

import java.io.File;


/**
 * Allowable parameter combinations:
 * 
 * 1. No parameters
 * 
 * 2. <iniFile> [<tradingMode>]
 * 
 * 3. <iniFile> <apiUserName> <apiPassword> [<tradingMode>]
 * 
 * 4. <iniFile> <fixUserName> <fixPassword> <apiUserName> <apiPassword> [<tradingMode>]
 * 
 * where:
 * 
 *      <iniFile>       ::= NULL | path-and-filename-of-.ini-file 
 * 
 *      <tradingMode>   ::= blank | LIVETRADING | PAPERTRADING
 * 
 *      <apiUserName>   ::= blank | username-for-TWS
 * 
 *      <apiPassword>   ::= blank | password-for-TWS
 * 
 *      <fixUserName>   ::= blank | username-for-FIX-CTCI-Gateway
 * 
 *      <fixPassword>   ::= blank | password-for-FIX-CTCI-Gateway
 * 
 */
public class ArgumentParser {
    private Settings _settings;
    private String _settingsPath = null;

    public ArgumentParser(Settings settings) {
        _settings = settings;
    }

    public void parse(String[] args) {
        if (args.length == 0) {
            _setDefaultSettingsPath();
        } else if (args.length == 1) {
            _setSettingsPath(args[0]);
        } else if (args.length == 3) {
            _setSettingsPath(args[0]);
            _setApiCredentials(args[1], args[2]);
        } else if (args.length == 4) {
            _setSettingsPath(args[0]);
            _setApiCredentials(args[1], args[2]);
            _setTradingMode(args[3]);
        } else if (args.length == 6) {
            _setSettingsPath(args[0]);
            _setFixCredentials(args[1], args[2]);
            _setApiCredentials(args[3], args[4]);
            _setTradingMode(args[5]);
        } else {
            Utils.logError("Incorrect number of arguments passed.");
            Utils.logRawToConsole("Number of arguments = " + args.length);
            for (String arg : args) {
                Utils.logRawToConsole(arg);
            }

            Utils.exitWithError(ErrorCodes.ERROR_CODE_INCORRECT_NUMBER_OF_ARGUMENTS);
        }
    }

    private void _setApiCredentials(String loginId, String password) {
        if (! loginId.isEmpty()) {
            if (loginId.startsWith("${") && loginId.endsWith("}")) {
                String loginVarname =  loginId.substring(2, loginId.length() - 1);
                loginId = System.getenv(loginVarname);
            }
            Utils.logToConsole("IB username set from arguments: " + loginId);
            _settings.setIbLoginId(loginId);
        }
        if (! password.isEmpty()) {
            if (password.startsWith("${") && password.endsWith("}")) {
                String passwordVarname = password.substring(2, password.length() - 1);
                password = System.getenv(passwordVarname);
            }
            Utils.logToConsole("IB password set from arguments");
            _settings.setIbPassword(password);
        }
    }

    private void _setFixCredentials(String loginId, String password) {
        if (! loginId.isEmpty()) {
            if (loginId.startsWith("${") && loginId.endsWith("}")) {
                String loginIdVarname = loginId.substring(2, loginId.length() - 1);
                loginId = System.getenv(loginIdVarname);
            }
            Utils.logToConsole("FIX username set from arguments: " + loginId);
            _settings.setFixLoginId(loginId);
        }
        if (! password.isEmpty()) {
            if (password.startsWith("${") && password.endsWith("}")) {
                String passwordVarname = password.substring(2, password.length() - 1);
                password = System.getenv(passwordVarname);
            }
            Utils.logToConsole("FIX password set from arguments");
            _settings.setFixPassword(password);
        }
    }

    private void _setTradingMode(String mode) {
        if (mode.startsWith("${") && mode.endsWith("}")) {
            String modeVarname = mode.substring(2, mode.length() - 1);
            mode = System.getenv(modeVarname);
        }
        Utils.logToConsole("Trading mode set from arguments: " + mode);
        _settings.setTradingMode(TradingMode.fromString(mode));
    }

    static String _makeDefaultSettingsPath() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            return (
                System.getenv("HOMEDRIVE") +
                System.getenv("HOMEPATH") +
                File.separator +
                "Documents" + File.separator +
                "IBC" + File.separator +
                "config.ini"
            );
        } else {
            return (
                System.getProperty("user.home") + File.separator +
                "IBC" + File.separator +
                "config.ini"
            );
        }
    }

    private static String _getUsername() {
        StringBuilder sb = new StringBuilder(System.getProperty("user.name"));

        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
                continue;
            }
            if (c >= 'A' && c <= 'Z') {
                sb.setCharAt(i, Character.toLowerCase(c));
            } else {
                sb.setCharAt(i, '_');
            }
        }

        return sb.toString();
    }

    public String settingsPath() {
        return _settingsPath;
    }

    private void _setDefaultSettingsPath() {
        _settingsPath = (
            System.getProperty("user.dir") + File.separator +
            "config." + _getUsername() + ".ini"
        );
    }

    private void _setSettingsPath(String path) {
        if (path.equalsIgnoreCase("NULL")) {
            _setDefaultSettingsPath();
        } else if (path.length() == 0) {
            _settingsPath = _makeDefaultSettingsPath();
        } else { // args.length >= 1
            _settingsPath = path;
        }
    }
}
