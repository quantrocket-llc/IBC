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

import javax.swing.JCheckBox;
import javax.swing.JDialog;

class EnableApiTask implements ConfigurationAction {

    private final CommandChannel mChannel;
    boolean mEnableRemoteConnections;
    boolean mDisableReadonlyApi;

    private JDialog configDialog;

    EnableApiTask(final CommandChannel channel, boolean enableRemoteConnections, boolean disableReadonlyApi) {
        mChannel = channel;
        mEnableRemoteConnections = enableRemoteConnections;
        mDisableReadonlyApi = disableReadonlyApi;
    }

    private void enableApi() throws IbcException {
        JCheckBox cb = SwingUtils.findCheckBox(configDialog, "Enable ActiveX and Socket Clients");
        if (cb == null) throw new IbcException("could not find Enable ActiveX checkbox");

        if (cb.isSelected()) {
            Utils.logToConsole("TWS is already configured to accept API connections");
        } else {
            cb.doClick();
            Utils.logToConsole("TWS has been configured to accept API connections");
        }
    }

    private void enableRemoteConnections() throws IbcException {
        JCheckBox cb = SwingUtils.findCheckBox(configDialog, "Allow connections from localhost only");
        if (cb == null) throw new IbcException("could not find localhost-only checkbox");

        if (cb.isSelected()) {
            cb.doClick();
            Utils.logToConsole("TWS configured to allow remote connections");
        } else {
            Utils.logToConsole("TWS already configured to allow remote connections");
        }
    }

    private void disableReadonlyApi() throws IbcException {
        JCheckBox cb = SwingUtils.findCheckBox(configDialog, "Read-Only API");
        if (cb == null) throw new IbcException("could not find Read-Only checkbox");

        if (cb.isSelected()) {
            cb.doClick();
            Utils.logToConsole("TWS configured to allow read-write API");
        } else {
            Utils.logToConsole("TWS already configured to allow read-write API");
        }
    }

    @Override public void run() {
        try {
            Utils.logToConsole("Doing ENABLEAPI configuration");

            Utils.selectApiSettings(configDialog);

            enableApi();
            if (mEnableRemoteConnections) {
                enableRemoteConnections();
            }
            if (mDisableReadonlyApi) {
                disableReadonlyApi();
            }
        } catch (IbcException e) {
            Utils.logError("CommandServer: " + e.getMessage());
            mChannel.writeNack(e.getMessage());
        }

        SwingUtils.clickButton(configDialog, "OK");
        mChannel.writeAck("configured");
    }

    @Override
    public void initialise(JDialog configDialog) {
        this.configDialog = configDialog;
    }

}
