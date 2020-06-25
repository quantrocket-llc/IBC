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
import java.util.concurrent.TimeUnit;


class TwsSettingsSaver {
    private static final TwsSettingsSaver instance = new TwsSettingsSaver();

    private TwsSettingsSaver() {
    }

    static TwsSettingsSaver getInstance() {
        return instance;
    }

    public void initialise() {
        List<Date> dates = Settings.settings().saveTwsSettingsAt();
        if (dates != null) {
            for (Date date : dates) {
                scheduleSave(date);
            }
        }
    }

    private static void scheduleSave(Date saveTime) {
        Utils.logToConsole("Tws settings will be saved at " + IntervalParser.dateFormat.format(saveTime));

        MyScheduledExecutorService.getInstance().scheduleAtFixedRate(() -> {
            Utils.logToConsole("Saving Tws settings");
            Utils.invokeMenuItem(MainWindowManager.mainWindowManager().getMainWindow(), new String[] {"File", "Save Settings"});
        }, saveTime.getTime() - System.currentTimeMillis(), 86400000, TimeUnit.MILLISECONDS);
    }
}
