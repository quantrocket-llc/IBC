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

import java.util.Date;
import java.util.List;


public class SettingsParser {
    private Settings _settings;

    public SettingsParser(Settings settings) {
        _settings = settings;
    }

    public void parse() {
        _parseCommandServer();
        _parseTwsSettings();
        _parseTwsSaveSettingsAt();
        _parseTradingMode();
        _parseApiCredentials();
        _parseFixCredentials();
    }

    private void _parseTwsSettings() {
        String path = _settings.getString("IbDir", "");
        if (! path.isEmpty()) {
            _settings.setIbDir(path);
        }

        int port = _settings.getInt("OverrideTwsApiPort", 0);
        if (port != 0) {
            _settings.setOverrideTwsApiPort(port);
        }

        if (! _settings.getString("StoreSettingsOnServer", "").isEmpty()) {
            boolean store = _settings.getBoolean("StoreSettingsOnServer", false);
            _settings.setStoreSettingsOnServer(store);
        }

        if (! _settings.getString("ReadOnlyApi", "").isEmpty()) {
            boolean readonly = _settings.getBoolean("ReadOnlyApi", true);
            _settings.setReadonlyApi(readonly);
        }

        String action = _settings.getString("AcceptIncomingConnectionAction", "manual");
        _settings.setIncomingConnectionPolicy(IncomingConnectionPolicy.fromString(action));

        action = _settings.getString("ExistingSessionDetectedAction", "manual");
        _settings.setExistingSessionPolicy(
            ExistingSessionPolicy.fromString(action)
        );

        action = _settings.getString("LogComponents", "never");
        _settings.setComponentLogPolicy(ComponentLogPolicy.fromString(action));

        String time = _settings.getString("ClosedownAt", "");
        if (! time.isEmpty()) {
            _settings.setShutdownTime(TimeParser.parse(time));
        }
    }

    private void _parseTwsSaveSettingsAt() {
        // setting format: SaveTwsSettingsAt=hh:mm [hh:mm]...
        //             or: SaveTwsSettingsAt=Every n [{mins | hours}] [hh:mm [hh:mm]]
        String timesSetting = _settings.getString("SaveTwsSettingsAt", "");
        if (timesSetting.isEmpty()) {
            return;
        }

        try {
            _settings.setSaveTwsSettingsAt(IntervalParser.parse(timesSetting));
        } catch (IbcException e) {
            Utils.logError("Invalid setting SaveTwsSettingsAt=" + timesSetting + ": " + e.getMessage() + "\nTWS Settings will not be saved automatically");
        }
    }

    private void _parseCommandServer()
    {
        String commandPrompt = _settings.getString("CommandPrompt", "");
        if (! commandPrompt.isEmpty()) {
            _settings.setCommandServerPrompt(commandPrompt);
        }

        String bindAddress = _settings.getString("BindAddress", "");
        if (! bindAddress.isEmpty()) {
            _settings.setCommandServerBindAddress(bindAddress);
        }

        int port = _settings.getInt("CommandServerPort", 0);
        if (port != 0) {
            _settings.setCommandServerPort(port);
        }

        String controlFrom = _settings.getString("ControlFrom", "");
        if (! controlFrom.isEmpty()) {
            _settings.setCommandServerControlFrom(controlFrom);
        }

        boolean suppress = _settings.getBoolean("SuppressInfoMessages", true);
        _settings.setSuppressInfoMessages(suppress);
    }

    private void _parseTradingMode()
    {
        String mode = _settings.getString("TradingMode", "");
        if (! mode.isEmpty()) {
            Utils.logToConsole("Trading mode set from config: " + mode);
            _settings.setTradingMode(TradingMode.fromString(mode));
        }
    }

    private void _parseApiCredentials()
    {
        String ibLoginId = _settings.getString("IbLoginId", "");
        if (! ibLoginId.isEmpty()) {
            Utils.logToConsole("IbLoginId set from config: " + ibLoginId);
            _settings.setIbLoginId(ibLoginId);
        }

        String ibPassword = _settings.getString("IBPassword", "");
        if (! ibPassword.isEmpty()) {
            Utils.logToConsole("IBPassword set from config: " + ibPassword);
            _settings.setIbPassword(ibPassword);
        }
    }

    private void _parseFixCredentials()
    {
        String fixLoginId = _settings.getString("FIXLoginId", "");
        if (! fixLoginId.isEmpty()) {
            Utils.logToConsole("FIXLoginId set from config: " + fixLoginId);
            _settings.setFixLoginId(fixLoginId);
        }

        String fixPassword = _settings.getString("FIXPassword", "");
        if (! fixPassword.isEmpty()) {
            Utils.logToConsole("FIXPassword set from config: " + fixPassword);
            _settings.setFixPassword(fixPassword);
        }
    }
}
