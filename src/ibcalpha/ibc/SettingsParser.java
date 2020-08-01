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
import java.util.Date;
import java.util.List;


public class SettingsParser {
    private Settings _settings;

    public SettingsParser(Settings settings) {
        _settings = settings;
    }

    public void parse() {
        PropertyParser parser = new PropertyParser();

        _parseCommandServer(parser);
        _parseTwsSettings(parser);
        _parseTwsSaveSettingsAt(parser);
        _parseTradingMode(parser);
        _parseApiCredentials(parser);
        _parseFixCredentials(parser);
    }

    private void _parseTwsSettings(PropertyParser parser) {
        String path = parser.getString("IbDir", "");
        if (! path.isEmpty()) {
            _settings.setIbDir(path);
        }

        int port = parser.getInt("IBZ_OVERRIDE_TWS_API_PORT", 0);
        if (port != 0) {
            _settings.setOverrideTwsApiPort(port);
        }

        if (! parser.getString("IBZ_STORE_SETTINGS_ON_SERVER", "").isEmpty()) {
            boolean store = parser.getBoolean("IBZ_STORE_SETTINGS_ON_SERVER", false);
            _settings.setStoreSettingsOnServer(store);
        }

        if (! parser.getString("IBZ_READONLY_API", "").isEmpty()) {
            boolean readonly = parser.getBoolean("IBZ_READONLY_API", true);
            _settings.setReadonlyApi(readonly);
        }

        String action = parser.getString("IBZ_ACCEPT_INCOMING_CONNECTION", "manual");
        _settings.setIncomingConnectionPolicy(IncomingConnectionPolicy.fromString(action));

        action = parser.getString("IBZ_EXISTING_SESSION_ACTION", "manual");
        _settings.setExistingSessionPolicy(
            ExistingSessionPolicy.fromString(action)
        );

        action = parser.getString("IBZ_LOG_COMPONENTS", "never");
        _settings.setComponentLogPolicy(ComponentLogPolicy.fromString(action));

        path = parser.getString("IBZ_LOG_OUTPUT_PATH", "");
        if (! path.equals("")) {
            _settings.setLogOutputPath(path);
        }

        String time = parser.getString("IBZ_CLOSEDOWN_AT", "");
        if (! time.isEmpty()) {
            _settings.setShutdownTime(TimeParser.parse(time));
        }

        _settings.setFixEnabled(parser.getBoolean("IBZ_FIX", false));
        _settings.setShowAllTrades(parser.getBoolean("IBZ_SHOW_ALL_TRADES", false));
        _settings.setAcceptNonBrokerageAccountWarning(
            parser.getBoolean("IBZ_ACCEPT_NON_BROKERAGE_ACCOUNT_WARNING", true));
        _settings.setReadonlyLogin(parser.getBoolean("IBZ_READONLY_LOGIN", false));
        _settings.setMinimizeMainWindow(
            parser.getBoolean("IBZ_MINIMIZE_MAIN_WINDOW", false));
        _settings.setMaximizeMainWindow(
            parser.getBoolean("IBZ_MAXIMIZE_MAIN_WINDOW", false));
        _settings.setAutoClosedown(
            parser.getBoolean("IBZ_AUTO_CLOSEDOWN", false));
        _settings.setAllowBlindTrading(
            parser.getBoolean("IBZ_ALLOW_BLIND_TRADING", false));
        _settings.setDismissPasswordExpiry(
            parser.getBoolean("IBZ_DISMISS_PASSWORD_EXPIRY_WARNING", false));
        _settings.setDismissNseCompliance(
            parser.getBoolean("IBZ_DISMISS_NSE_COMPLIANCE_NOTICE", true));
        _settings.setSendTwsLogsToConsole(
            parser.getBoolean("IBZ_SEND_TWS_LOGS_TO_CONSOLE", false));
    }

    private void _parseTwsSaveSettingsAt(PropertyParser parser) {
        // setting format: SaveTwsSettingsAt=hh:mm [hh:mm]...
        //             or: SaveTwsSettingsAt=Every n [{mins | hours}] [hh:mm [hh:mm]]
        String timesSetting = parser.getString("IBZ_SAVE_TWS_SETTINGS_AT", "");
        if (timesSetting.isEmpty()) {
            return;
        }

        try {
            _settings.setSaveTwsSettingsAt(IntervalParser.parse(timesSetting));
        } catch (IbcException e) {
            Utils.logError("Invalid setting SaveTwsSettingsAt=" + timesSetting + ": " + e.getMessage() + "\nTWS Settings will not be saved automatically");
        }
    }

    private void _parseCommandServer(PropertyParser parser)
    {
        String commandPrompt = parser.getString("IBZ_COMMAND_PROMPT", "");
        if (! commandPrompt.isEmpty()) {
            _settings.setCommandServerPrompt(commandPrompt);
        }

        String bindAddress = parser.getString("IBZ_BIND_ADDRESS", "");
        if (! bindAddress.isEmpty()) {
            _settings.setCommandServerBindAddress(bindAddress);
        }

        int port = parser.getInt("IBZ_COMMAND_SERVER_PORT", 0);
        if (port != 0) {
            _settings.setCommandServerPort(port);
        }

        String controlFrom = parser.getString("IBZ_CONTROL_FROM", "");
        if (! controlFrom.isEmpty()) {
            _settings.setCommandServerControlFrom(controlFrom);
        }

        boolean suppress = parser.getBoolean("IBZ_SUPPRESS_INFO_MESSAGES", true);
        _settings.setSuppressInfoMessages(suppress);
    }

    private void _parseTradingMode(PropertyParser parser)
    {
        String mode = parser.getString("IBZ_TRADING_MODE", "live");
        if (! mode.isEmpty()) {
            Utils.logToConsole("Trading mode set from config: " + mode);
            _settings.setTradingMode(TradingMode.fromString(mode));
        }
    }

    private void _parseApiCredentials(PropertyParser parser)
    {
        String ibLoginId = parser.getString("IBZ_LOGIN_ID", "");
        if (! ibLoginId.isEmpty()) {
            Utils.logToConsole("IB username set from config: " + ibLoginId);
            _settings.setIbLoginId(ibLoginId);
        }

        String ibPassword = parser.getString("IBZ_PASSWORD", "");
        if (! ibPassword.isEmpty()) {
            Utils.logToConsole("IB password set from config");
            _settings.setIbPassword(ibPassword);
        }
    }

    private void _parseFixCredentials(PropertyParser parser)
    {
        String fixLoginId = parser.getString("IBZ_FIX_LOGIN_ID", "");
        if (! fixLoginId.isEmpty()) {
            Utils.logToConsole("FIXLoginId set from config: " + fixLoginId);
            _settings.setFixLoginId(fixLoginId);
        }

        String fixPassword = parser.getString("IBZ_FIX_PASSWORD", "");
        if (! fixPassword.isEmpty()) {
            Utils.logToConsole("FIXPassword set from config: " + fixPassword);
            _settings.setFixPassword(fixPassword);
        }
    }
}
