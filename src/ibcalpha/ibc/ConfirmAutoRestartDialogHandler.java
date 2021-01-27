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

import java.awt.Window;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;

class ConfirmAutoRestartDialogHandler implements WindowHandler {
    public boolean filterEvent(Window window, int eventId) {
        switch (eventId) {
            case WindowEvent.WINDOW_OPENED:
            case WindowEvent.WINDOW_ACTIVATED:
                return true;
            default:
                return false;
        }
    }

    public void handleWindow(Window window, int eventID) {
        if (! SwingUtils.clickButton(window, "OK")) {
            if (! SwingUtils.clickButton(window, "No")) {
                Utils.logError("could not dismiss Auto Restart Confirmation dialgo because we could not find one of the controls.");
            }
        }
    }

    public boolean recogniseWindow(Window window) {
        if (! (window instanceof JDialog)) return false;
        /* because the title of these windows is generic, we don't want to recognise windows
        if the Auto-restart configuration has already completed */
        if (ConfigureAutoRestartTask.hasRun) return false;
        return (SwingUtils.titleContains(window, "IB Trader Workstation"));
    }
}
