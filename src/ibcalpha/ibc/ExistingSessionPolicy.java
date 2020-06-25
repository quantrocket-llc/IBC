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


public enum ExistingSessionPolicy {
    PRIMARY,
    PRIMARY_OVERRIDE,
    SECONDARY,
    MANUAL;

    public static ExistingSessionPolicy fromString(String mode)
            throws IllegalArgumentException {
        if (mode.equalsIgnoreCase("primary")) {
            return PRIMARY;
        } else if (mode.equalsIgnoreCase("primaryoverride")) {
            return PRIMARY_OVERRIDE;
        } else if (mode.equalsIgnoreCase("secondary")) {
            return SECONDARY;
        } else if (mode.equalsIgnoreCase("manual")) {
            return MANUAL;
        }

        throw new IllegalArgumentException("Invalid existing session policy: " + mode);
    }

    @Override
    public String toString() {
        switch (this) {
            case PRIMARY:
                return "primary";
            case PRIMARY_OVERRIDE:
                return "primaryoverride";
            case SECONDARY:
                return "secondary";
            case MANUAL:
                return "manual";
            default:
                throw new IllegalArgumentException();
        }
    }
}
