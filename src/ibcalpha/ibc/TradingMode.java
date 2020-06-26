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


public enum TradingMode {
    LIVE,
    PAPER;

    public static TradingMode fromString(String mode)
            throws IllegalArgumentException {
        switch (mode.toLowerCase()) {
            case "live":
            case "livetrading":
                return TradingMode.LIVE;

            case "paper":
            case "papertrading":
                return TradingMode.PAPER;
        }

        throw new IllegalArgumentException("Invalid trading mode: " + mode);
    }

    @Override
    public String toString() {
        switch (this) {
            case LIVE:
                return "live";
            case PAPER:
                return "paper";
            default:
                throw new IllegalArgumentException();
        }
    }
}
