// This file is part of IBC.
// Copyright (C) 2021 QuantRocket LLC

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

public class ConfigureAutoRestartTimeTask implements ConfigurationAction{

    private final String autoRestartTimeRaw;
    private final String autoRestartTimeHHMM;
    private final String autoRestartTimeMeridian;
    private JDialog configDialog;

    ConfigureAutoRestartTimeTask(String autoRestartTime) {
        this.autoRestartTimeRaw = autoRestartTime;
        String[] autoRestartTimeParts = this.autoRestartTimeRaw.split(" ", 2);
        this.autoRestartTimeHHMM = autoRestartTimeParts[0];
        this.autoRestartTimeMeridian = autoRestartTimeParts[1];
    }

    @Override
    public void run() {

        try {
            Utils.logToConsole("Setting auto-restart time");

            Utils.selectLockAndExitSettings(configDialog);

            if ((! autoRestartTimeMeridian.equals("AM")) && (! autoRestartTimeMeridian.equals("PM"))) {
                throw new IbcException("AutoRestartTime must be PM or AM but got " + autoRestartTimeMeridian);
            }

            JTextField autoRestartTimeField = SwingUtils.findTextField(configDialog, 0);
            if (autoRestartTimeField == null) throw new IbcException("could not find Set Auto Restart Time field");

            if (autoRestartTimeField.getText().equals(autoRestartTimeHHMM)) {
                Utils.logToConsole("Auto restart time is already set to " + autoRestartTimeHHMM);
            } else {
                autoRestartTimeField.setText(autoRestartTimeHHMM);
                Utils.logToConsole("Auto restart time is now set to " + autoRestartTimeHHMM);
            }

            JRadioButton amRadio = SwingUtils.findRadioButton(configDialog, "AM");
            JRadioButton pmRadio = SwingUtils.findRadioButton(configDialog, "PM");

            if (autoRestartTimeMeridian.equals("AM")) {
                if (amRadio.isSelected()) {
                    Utils.logToConsole("Auto restart time is already set to AM");
                } else {
                    amRadio.setSelected(true);
                    Utils.logToConsole("Auto restart time is now set to AM");
                }
            } else {
                if (pmRadio.isSelected()) {
                    Utils.logToConsole("Auto restart time is already set to PM");
                } else {
                    pmRadio.setSelected(true);
                    Utils.logToConsole("Auto restart time is now set to PM");
                }
            }

            /* click Apply button */
            SwingUtils.clickButton(configDialog, "Apply");

        } catch (IbcException e) {
            Utils.logException(e);
        }
    }

    @Override
    public void initialise(JDialog configDialog) {
        this.configDialog = configDialog;
    }
}
