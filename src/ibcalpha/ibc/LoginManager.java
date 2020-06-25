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

import javax.swing.JFrame;


public class LoginManager {
    private static LoginManager _instance;

    private String message;

    private volatile JFrame loginFrame = null;

    private boolean fromSettings;

    /**
     * IBAPI username - can either be supplied from the .ini file or as args[1]
     * NB: if IBAPI username is supplied in args[1], then the password must
     * be in args[2]. If IBAPI username is supplied in .ini, then the password must
     * also be in .ini.
     */
    private volatile String _apiUsername;

    /**
     * IBAPI password - can either be supplied from the .ini file or as args[2]
     */
    private volatile String _apiPassword;

    /**
     * FIX username - can either be supplied from the .ini file or as args[1]
     * NB: if username is supplied in args[1], then the password must
     * be in args[2], and the IBAPI username and password may be in
     * args[3] and args[4]. If username is supplied in .ini, then the password must
     * also be in .ini.
     */
    private volatile String _fixUsername;

    /**
     * FIX password - can either be supplied from the .ini file or as args[2]
     */
    private volatile String _fixPassword;

    static {
        _instance = new LoginManager();
    }

    public static LoginManager loginManager() {
        return _instance;
    }

    public static void initialise(LoginManager loginManager){
        if (loginManager == null) throw new IllegalArgumentException("loginManager");
        _instance = loginManager;
    }

    protected LoginManager() {
        /* don't actually get the credentials yet because the settings
         * provider might be changed
         */
        fromSettings = true;
        message = "getting username and password from settings";
    }

    public void loadFromArgs(String[] args) {
        if (Settings.settings().getFixEnabled()) {
            loadApiCredentialsFromArgs(args);
            fromSettings = !loadFixCredentialsFromArgs(args);
        } else {
            fromSettings = !loadApiCredentialsFromArgs(args);
        }

        if (fromSettings) {
            message = "getting username and password from args but not found. Will get from settings";
        } else {
            message = "getting username and password from args";
        }
    }

    public void logDiagnosticMessage(){
        Utils.logToConsole("using default login manager: " + message);
    }

    public String getFixPassword() {
        if (fromSettings) {
            return Settings.settings().getString("_fixPassword", "");
        }
        return _fixPassword;
    }

    public String getFixUsername() {
        if (fromSettings) {
            return Settings.settings().getString("FIXLoginId", "");
        }
        return _fixUsername;
    }

    public String getApiPassword() {
        if (fromSettings) {
            return Settings.settings().getString("IbPassword", "");
        }
        return _apiPassword;
    }

    public String getApiUsername() {
        if (fromSettings) {
            return Settings.settings().getString("IbLoginId", "");
        }
        return _apiUsername;
    }

    public void setLoginFrame(JFrame window) {
        loginFrame = window;
    }

    public JFrame getLoginFrame() {
        return loginFrame;
    }

    private boolean loadFixCredentialsFromArgs(String[] args) {
        if (args.length >= 3 && args.length <= 6) {
            _fixUsername = args[1];
            _fixPassword = args[2];
            return true;
        } else {
            return false;
        }
    }

    private boolean loadApiCredentialsFromArgs(String[] args) {
        if (Settings.settings().getFixEnabled()) {
            if (args.length == 5 || args.length == 6) {
                _apiUsername = args[3];
                _apiPassword = args[4];
                return true;
            } else {
                return false;
            }
        } else if (args.length == 3 || args.length == 4) {
            _apiUsername = args[1];
            _apiPassword = args[2];
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
