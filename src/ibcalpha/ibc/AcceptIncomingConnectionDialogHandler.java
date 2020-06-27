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

import java.awt.Window;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;

class AcceptIncomingConnectionDialogHandler implements WindowHandler {
    public boolean filterEvent(Window window, int eventId) {
        return eventId == WindowEvent.WINDOW_OPENED;
    }

    public boolean recogniseWindow(Window window) {
        return (
            window instanceof JDialog &&
            (SwingUtils.findLabel(window, "Accept incoming connection") != null)
        );
    }

    public void handleWindow(Window window, int eventID) {
        switch (Settings.settings().incomingConnectionPolicy()) {
            case MANUAL:
                Utils.logToConsole("not confirming incoming connection because AcceptIncomingConnectionAction set to manual.");
                return;

            case ACCEPT:
                if (! (SwingUtils.clickButton(window, "OK") ||
                       SwingUtils.clickButton(window, "Yes"))) {
                    Utils.logError("could not accept incoming connection because we could not find one of the controls.");
                }
                return;

            case REJECT:
                if (! SwingUtils.clickButton(window, "No")) {
                    Utils.logError("could not accept incoming connection because we could not find one of the controls.");
                }
                return;

            default:
                Utils.logError("could not accept incoming connection because the AcceptIncomingConnectionAction setting is invalid.");
        }
    }
}
