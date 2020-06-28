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


public enum IncomingConnectionPolicy {
    ACCEPT,
    REJECT,
    MANUAL;

    public static IncomingConnectionPolicy fromString(String mode)
            throws IllegalArgumentException {
        switch (mode.toLowerCase()) {
            case "accept":
                return ACCEPT;
            case "reject":
                return REJECT;
            case "manual":
                return MANUAL;
        }

        throw new IllegalArgumentException("Invalid incoming connection policy: " + mode);
    }

    @Override
    public String toString() {
        switch (this) {
            case ACCEPT:
                return "accept";
            case REJECT:
                return "reject";
            case MANUAL:
                return "manual";
        }

        throw new IllegalArgumentException();
    }
}
