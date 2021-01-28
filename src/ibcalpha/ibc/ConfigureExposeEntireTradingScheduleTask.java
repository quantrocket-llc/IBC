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

public class ConfigureExposeEntireTradingScheduleTask implements ConfigurationAction{

    private final boolean exposeEntireTradingSchedule;
    private JDialog configDialog;

    ConfigureExposeEntireTradingScheduleTask(boolean exposeEntireTradingSchedule) {
        this.exposeEntireTradingSchedule = exposeEntireTradingSchedule;
    }

    @Override
    public void run() {

        try {
            Utils.logToConsole("Setting 'Expose entire trading schedule to API'");

            Utils.selectApiSettings(configDialog);

            JCheckBox exposeEntireTradingScheduleCheckbox = SwingUtils.findCheckBox(configDialog, "Expose entire trading schedule to API");
            if (exposeEntireTradingScheduleCheckbox == null) {
                throw new IbcException("could not find 'Expose entire trading schedule to API' component");
            }

            if (exposeEntireTradingScheduleCheckbox.isSelected() == exposeEntireTradingSchedule) {
                Utils.logToConsole("'Expose entire trading schedule to API' checkbox is already set to: " + exposeEntireTradingSchedule);
            } else {
                exposeEntireTradingScheduleCheckbox.setSelected(exposeEntireTradingSchedule);
                Utils.logToConsole("'Expose entire trading schedule to API' checkbox is now set to: " + exposeEntireTradingSchedule);
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
