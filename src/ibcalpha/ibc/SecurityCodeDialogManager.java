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


public class SecurityCodeDialogManager {
    private static SecurityCodeDialogManager _instance;
    private Window _mDialog = null;

    static {
        _instance = new SecurityCodeDialogManager();
    }

    public static SecurityCodeDialogManager getInstance() {
        return _instance;
    }

    public void setDialog(Window dialog) {
        _mDialog = dialog;
    }

    public Window getDialog() {
        if (_mDialog == null) {
            //Utils.logToConsole("Request for security code dialog, but none registered");
            return null;
        }

        //Utils.logToConsole("Successful request for security code dialog");
        return _mDialog;
    }
}
