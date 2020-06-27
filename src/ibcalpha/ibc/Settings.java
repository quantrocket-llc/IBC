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


public class Settings {
    private static Settings _instance = new Settings();

    public static Settings settings() {
        return _instance;
    }

    protected Settings() {
    }

    /**
     * FIX username - can either be supplied from the .ini file or as args[1]
     * NB: if username is supplied in args[1], then the password must
     * be in args[2], and the IBAPI username and password may be in
     * args[3] and args[4]. If username is supplied in .ini, then the password must
     * also be in .ini.
     */
    private volatile String _fixLoginId = "";

    public String fixLoginId() {
        return _fixLoginId;
    }

    public void setFixLoginId(String fixLoginId) {
        _fixLoginId = fixLoginId;
    }

    /**
     * FIX password - can either be supplied from the .ini file or as args[2]
     */
    private volatile String _fixPassword = "";

    public String fixPassword() {
        return _fixPassword;
    }

    public void setFixPassword(String fixPassword) {
        _fixPassword = fixPassword;
    }

    /**
     * IBAPI username - can either be supplied from the .ini file or as args[1]
     * NB: if IBAPI username is supplied in args[1], then the password must
     * be in args[2]. If IBAPI username is supplied in .ini, then the password must
     * also be in .ini.
     */
    private volatile String _ibLoginId = "";

    public String ibLoginId() {
        return _ibLoginId;
    }

    public void setIbLoginId(String ibLoginId) {
        _ibLoginId = ibLoginId;
    }

    /**
     * IBAPI password - can either be supplied from the .ini file or as args[2]
     */
    private volatile String _ibPassword = "";

    public String ibPassword() {
        return _ibPassword;
    }

    public void setIbPassword(String ibPassword) {
        _ibPassword = ibPassword;
    }


    private int _commandServerPort = 0;

    public int commandServerPort() {
        return _commandServerPort;
    }

    public void setCommandServerPort(int commandServerPort) {
        _commandServerPort = commandServerPort;
    }


    private boolean _fixEnabled = false;

    public boolean fixEnabled() {
        return _fixEnabled;
    }

    public void setFixEnabled(boolean fixEnabled) {
        _fixEnabled = fixEnabled;
    }


    private TradingMode _tradingMode = TradingMode.LIVE;

    public TradingMode tradingMode() {
        return _tradingMode;
    }

    public void setTradingMode(TradingMode tradingMode) {
        _tradingMode = tradingMode;
    }


    private int _overrideTwsApiPort = 0;

    public int overrideTwsApiPort() {
        return _overrideTwsApiPort;
    }

    public void setOverrideTwsApiPort(int overrideTwsApiPort) {
        _overrideTwsApiPort = overrideTwsApiPort;
    }


    private Boolean _storeSettingsOnServer = null;

    public Boolean storeSettingsOnServer() {
        return _storeSettingsOnServer;
    }

    public void setStoreSettingsOnServer(boolean storeSettingsOnServer) {
        _storeSettingsOnServer = Boolean.valueOf(storeSettingsOnServer);
    }


    private String _commandServerPrompt = "";

    public String commandServerPrompt() {
        return _commandServerPrompt;
    }

    public void setCommandServerPrompt(String commandServerPrompt) {
        _commandServerPrompt = commandServerPrompt;
    }


    private boolean _suppressInfoMessages = true;

    public boolean suppressInfoMessages() {
        return _suppressInfoMessages;
    }

    public void setSuppressInfoMessages(boolean suppressInfoMessages) {
        _suppressInfoMessages = suppressInfoMessages;
    }


    private String _commandServerBindAddress = "";

    public String commandServerBindAddress() {
        return _commandServerBindAddress;
    }

    public void setCommandServerBindAddress(String bindAddress) {
        _commandServerBindAddress = bindAddress;
    }


    private String _commandServerControlFrom = "";

    public String commandServerControlFrom() {
        return _commandServerControlFrom;
    }

    public void setCommandServerControlFrom(String controlFrom) {
        _commandServerControlFrom = controlFrom;
    }


    private List<Date> _saveTwsSettingsAt = null;

    public List<Date> saveTwsSettingsAt() {
        return _saveTwsSettingsAt;
    }

    public void setSaveTwsSettingsAt(List<Date> dates) {
        _saveTwsSettingsAt = dates;
    }


    private IncomingConnectionPolicy _incomingConnectionPolicy = IncomingConnectionPolicy.REJECT;

    public IncomingConnectionPolicy incomingConnectionPolicy() {
        return _incomingConnectionPolicy;
    }

    public void setIncomingConnectionPolicy(IncomingConnectionPolicy policy) {
        _incomingConnectionPolicy = policy;
    }


    private ExistingSessionPolicy _existingSessionPolicy = ExistingSessionPolicy.MANUAL;

    public ExistingSessionPolicy existingSessionPolicy() {
        return _existingSessionPolicy;
    }

    public void setExistingSessionPolicy(ExistingSessionPolicy policy) {
        _existingSessionPolicy = policy;
    }


    private ComponentLogPolicy _componentLogPolicy = ComponentLogPolicy.NEVER;

    public ComponentLogPolicy componentLogPolicy() {
        return _componentLogPolicy;
    }

    public void setComponentLogPolicy(ComponentLogPolicy policy) {
        _componentLogPolicy = policy;
    }


    private Date _shutdownTime = null;

    public Date shutdownTime() {
        return _shutdownTime;
    }

    public void setShutdownTime(Date shutdownTime) {
        _shutdownTime = shutdownTime;
    }


    private String _ibDir = System.getProperty("user.dir");

    public String ibDir() {
        return _ibDir;
    }

    public void setIbDir(String ibDir) {
        _ibDir = ibDir;
    }


    private Boolean _readonlyApi = null;

    public Boolean readonlyApi() {
        return _readonlyApi;
    }

    public void setReadonlyApi(boolean readonlyApi) {
        _readonlyApi = Boolean.valueOf(readonlyApi);
    }


    private boolean _showAllTrades = false;

    public boolean showAllTrades() {
        return _showAllTrades;
    }

    public void setShowAllTrades(boolean showAllTrades) {
        _showAllTrades = showAllTrades;
    }


    private boolean _logToConsole = false;

    public boolean logToConsole() {
        return _logToConsole;
    }

    public void setLogToConsole(boolean logToConsole) {
        _logToConsole = logToConsole;
    }


    private boolean _dismissNseCompliance = true;

    public boolean dismissNseCompliance() {
        return _dismissNseCompliance;
    }

    public void setDismissNseCompliance(boolean dismissNseCompliance) {
        _dismissNseCompliance = dismissNseCompliance;
    }


    private boolean _dismissPasswordExpiry = false;

    public boolean dismissPasswordExpiry() {
        return _dismissPasswordExpiry;
    }

    public void setDismissPasswordExpiry(boolean dismissPasswordExpiry) {
        _dismissPasswordExpiry = dismissPasswordExpiry;
    }


    private boolean _readonlyLogin = false;

    public boolean readonlyLogin() {
        return _readonlyLogin;
    }

    public void setReadonlyLogin(boolean readonlyLogin) {
        _readonlyLogin = readonlyLogin;
    }


    private boolean _autoClosedown = false;

    public boolean autoClosedown() {
        return _autoClosedown;
    }

    public void setAutoClosedown(boolean autoClosedown) {
        _autoClosedown = autoClosedown;
    }


    private boolean _allowBlindTrading = false;

    public boolean allowBlindTrading() {
        return _allowBlindTrading;
    }

    public void setAllowBlindTrading(boolean allowBlindTrading) {
        _allowBlindTrading = allowBlindTrading;
    }


    private boolean _minimizeMainWindow = false;

    public boolean minimizeMainWindow() {
        return _minimizeMainWindow;
    }

    public void setMinimizeMainWindow(boolean minimizeMainWindow) {
        _minimizeMainWindow = minimizeMainWindow;
    }


    private boolean _acceptNonBrokerageAccountWarning = false;

    public boolean acceptNonBrokerageAccountWarning() {
        return _acceptNonBrokerageAccountWarning;
    }

    public void setAcceptNonBrokerageAccountWarning(boolean accept) {
        _acceptNonBrokerageAccountWarning = accept;
    }


    private boolean _sendTwsLogsToConsole = false;

    public boolean sendTwsLogsToConsole() {
        return _sendTwsLogsToConsole;
    }

    public void setSendTwsLogsToConsole(boolean send) {
        _sendTwsLogsToConsole = send;
    }
}
