// This file is part of IBC.
// Copyright (C) 2020 QuantRocket LLC

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

import ibcalpha.ibc.ConfigDialogManager;
import ibcalpha.ibc.ConfigurationAction;
import ibcalpha.ibc.IbcException;
import ibcalpha.ibc.MainWindowManager;
import java.awt.Component;
import java.awt.Container;
import javax.swing.*;

public class ConfigureMasterApiClientIdTask implements ConfigurationAction{

    private final int masterApiClientId;
    private JDialog configDialog;

    ConfigureMasterApiClientIdTask(int masterApiClientId) {
        this.masterApiClientId = masterApiClientId;
    }

    @Override
    public void run() {

        try {
            Utils.logToConsole("Setting Master API client ID");

            Utils.selectApiSettings(configDialog);

            Component comp = SwingUtils.findComponent(configDialog, "Master API client ID");
            if (comp == null) throw new IbcException("could not find Master API client ID component");

            JTextField tf = SwingUtils.findTextField((Container)comp, 0);
            if (tf == null) throw new IbcException("could not find Master API client ID field");

            String currentId = tf.getText();
            Integer currentIdInt = -999;
            if (! currentId.isEmpty()) {
                currentIdInt = Integer.parseInt(currentId);
            }
            if (currentIdInt == masterApiClientId) {
                Utils.logToConsole("Master API client ID is already set to " + tf.getText());
            } else {
                Utils.logToConsole("Master API client ID was set to " + tf.getText());
                tf.setText(Integer.toString(masterApiClientId));
                Utils.logToConsole("Master API client ID now set to " + tf.getText());
            }
        } catch (IbcException e) {
            Utils.logException(e);
        }
    }

    @Override
    public void initialise(JDialog configDialog) {
        this.configDialog = configDialog;
    }
}
