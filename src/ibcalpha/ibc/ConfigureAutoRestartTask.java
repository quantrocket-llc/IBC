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

public class ConfigureAutoRestartTask implements ConfigurationAction{

    private final boolean autoRestart;
    private JDialog configDialog;

    public static boolean hasRun = false;

    ConfigureAutoRestartTask(boolean autoRestart) {
        this.autoRestart = autoRestart;
    }

    @Override
    public void run() {

        try {
            Utils.logToConsole("Setting auto-restart");

            Utils.selectLockAndExitSettings(configDialog);

            JRadioButton autoRestartRadio = SwingUtils.findRadioButton(configDialog, "Auto restart");
            JRadioButton autoLogoffRadio = SwingUtils.findRadioButton(configDialog, "Auto logoff");

            if (autoRestartRadio == null) {
                Utils.logError("could not find Auto restart button");
                return;
            }

            if (autoRestartRadio.isSelected() == autoRestart) {
                Utils.logToConsole("Auto restart radio button is already set to: " + autoRestart);
            } else {
                if (autoRestart) {
                    autoRestartRadio.setSelected(true);
                } else {
                    autoLogoffRadio.setSelected(true);
                }
                Utils.logToConsole("Auto restart radio button is now set to: " + autoRestart);

                /* click Apply button */
                SwingUtils.clickButton(configDialog, "Apply");

                try {
                    /* sleep 1 second so confirmation dialogs can be closed before this thread
                    exits and triggers closing the main configuration window */
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Utils.logException(e);
                }
            }
        } catch (IbcException e) {
            Utils.logException(e);
        }
        ConfigureAutoRestartTask.hasRun = true;
    }

    @Override
    public void initialise(JDialog configDialog) {
        this.configDialog = configDialog;
    }
}
