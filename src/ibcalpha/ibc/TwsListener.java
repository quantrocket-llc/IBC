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

import java.awt.AWTEvent;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;

class TwsListener implements AWTEventListener {
    private final List<WindowHandler> windowHandlers;

    TwsListener (List<WindowHandler> windowHandlers) {
        this.windowHandlers = windowHandlers;
    }

    @Override
    public void eventDispatched(AWTEvent event) {
        int eventID = event.getID();

        Window window =((WindowEvent) event).getWindow();

        if (eventID == WindowEvent.WINDOW_OPENED ||
                eventID == WindowEvent.WINDOW_ACTIVATED ||
                eventID == WindowEvent.WINDOW_CLOSING ||
                eventID == WindowEvent.WINDOW_CLOSED ||
                eventID == WindowEvent.WINDOW_ICONIFIED ||
                eventID == WindowEvent.WINDOW_DEICONIFIED) {
            logWindow(window, eventID);
        }

        for (WindowHandler wh : windowHandlers) {
            if (wh.filterEvent(window, eventID) && wh.recogniseWindow(window))  {
                wh.handleWindow(window, eventID);
                break;
            }
        }

    }

    private void logWindow(Window window, int eventID) {
        final String event = SwingUtils.windowEventToString(eventID);
        final String windowTitle;
        if (window instanceof JFrame) {
            windowTitle = SwingUtils.getWindowTitle(window);
            Utils.logToConsole("detected frame entitled: " + windowTitle + "; event=" + event);
        } else if (window instanceof JDialog) {
            windowTitle = SwingUtils.getWindowTitle(window);
            Utils.logToConsole("detected dialog entitled: " + windowTitle + "; event=" + event);
        } else {
            windowTitle = window.getClass().getName();
            Utils.logToConsole("detected window: type=" + windowTitle + "; event=" + event);
        }

        if (windowTitle.isEmpty() || 
            (eventID == WindowEvent.WINDOW_OPENED && (logComponents.equals("open") || logComponents.equals("activate")))
            ||
            (eventID == WindowEvent.WINDOW_ACTIVATED && policy == ComponentLogPolicy.ACTIVATE)) {
            Utils.logRawToConsole(SwingUtils.getWindowStructure(window));
        }
    }
}



