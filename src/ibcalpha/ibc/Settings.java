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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class Settings {
    private final Properties props = new Properties();
    private String path;

    private static Settings _instance = new Settings();

    public static Settings settings() {
        return _instance;
    }

    public void logDiagnosticMessage(){
        Utils.logToConsole("using default settings provider: ini file is " + path);
    }

    protected Settings() {
        load(generateDefaultIniPath());
    }

    protected Settings(String path) {
        load(path);
    }

    public void loadFromArgs(String[] args) {
        load(getSettingsPath(args));
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

    private void load(String path) {
        this.path = path;
        props.clear();

        try {
            File f = new File(path);
            InputStream is = new BufferedInputStream(new FileInputStream(f));
            props.load(is);
            is.close();
        } catch (FileNotFoundException e) {
            Utils.logToConsole("Properties file " + path + " not found");
        } catch (IOException e) {
            Utils.logToConsole(
                    "Exception accessing Properties file " + path);
            Utils.logToConsole(e.toString());
        }
    }

    static String generateDefaultIniPath() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            return System.getenv("HOMEDRIVE") +
                    System.getenv("HOMEPATH") + File.separator +
                    "Documents" + File.separator +
                    "IBC" + File.separator +
                    "config.ini";
        } else {
            return System.getProperty("user.home") + File.separator +
                    "IBC" + File.separator +
                    "config.ini";
        }
    }

    static String getSettingsPath(String[] args) {
        String iniPath;
        if (args.length == 0 || args[0].equalsIgnoreCase("NULL")) {
            iniPath = getWorkingDirectory() + "config." + getComputerUserName() + ".ini";
        } else if (args[0].length() == 0) {
            iniPath = generateDefaultIniPath();
        } else {// args.length >= 1
            iniPath = args[0];
        }

        File finiPath = new File(iniPath);
        if (!finiPath.isFile() || !finiPath.exists()) {
            Utils.exitWithError(
                ErrorCodes.ERROR_CODE_INI_FILE_NOT_EXIST,
                "ini file \"" + iniPath +
                "\" either does not exist, or is a directory.  quitting..."
            );
        }
        return iniPath;
    }

    private static String getComputerUserName() {
        StringBuilder sb = new StringBuilder(System.getProperty("user.name"));
        int i;
        for (i = 0; i < sb.length(); i++) {
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

    private static String getWorkingDirectory() {
        return System.getProperty("user.dir") + File.separator;
    }

    /**
    returns the boolean value associated with property named key.
    Returns defaultValue if there is no such property,
    or if the property value cannot be converted to a boolean.
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = props.getProperty(key);

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

    private int _commandServerPort = 0;

    public int commandServerPort() {
        return _commandServerPort;
    }

    public void setCommandServerPort(int commandServerPort) {
        _commandServerPort = commandServerPort;
    }

    /**
    returns the int value associated with property named key.
    Returns defaultValue if there is no such property,
    or if the property value cannot be converted to an int.
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue) {
        String value = props.getProperty(key);

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
    returns the value associated with property named key.
    Returns defaultValue if no such property.
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        String value = props.getProperty(key, defaultValue);

        // handle key=[empty string] in .ini file
        if (value.isEmpty()) {
            value = defaultValue;
        }
        return value;
    }

    public boolean getFixEnabled() {
        return settings().getBoolean("FIX", false);
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
}
