// This file is part of IBC.
// Copyright (C) 2022 QuantRocket LLC

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
import javax.swing.JOptionPane;

class AutoRestartWarningDialogHandler implements WindowHandler {
    public boolean filterEvent(Window window, int eventId) {
        switch (eventId) {
            case WindowEvent.WINDOW_OPENED:
                return true;
            default:
                return false;
        }
    }

    public void handleWindow(Window window, int eventID) {
        if (! SwingUtils.clickButton(window, "OK")) {
            Utils.logError("could not dismiss auto restart warning dialog.");
        }
    }

    public boolean recogniseWindow(Window window) {
        if (! (window instanceof JDialog)) return false;
        JOptionPane optionPane = SwingUtils.findOptionPane(window);
        if (optionPane == null) return false;
        String message = optionPane.getMessage().toString();
        return (message.contains("restart automatically on a daily basis"));
    }

}
