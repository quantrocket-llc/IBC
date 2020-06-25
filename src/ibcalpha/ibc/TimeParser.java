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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeParser {
    public static Date parse(String time) {
        try {
            return _parse(time);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                "Invalid ClosedownAt setting: '" + time +
                "'; format should be: <[day ]hh:mm>   eg 22:00 or Friday 22:00"
            );
        }
    }

    private static Date _parse(String time) throws ParseException {
        int shutdownDayOfWeek;
        int shutdownHour;
        int shutdownMinute;

        Calendar cal = Calendar.getInstance();
        boolean dailyShutdown = false;
        try {
            cal.setTime((new SimpleDateFormat("E HH:mm")).parse(time));
            dailyShutdown = false;
        } catch (ParseException e) {
            try {
                String today = (new SimpleDateFormat("E")).format(cal.getTime());
                cal.setTime((new SimpleDateFormat("E HH:mm")).parse(today + " " + time));
                dailyShutdown = true;
            } catch (ParseException x) {
                throw x;
            }
        }
        shutdownDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        shutdownHour = cal.get(Calendar.HOUR_OF_DAY);
        shutdownMinute = cal.get(Calendar.MINUTE);
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, shutdownHour);
        cal.set(Calendar.MINUTE, shutdownMinute);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH,
                (shutdownDayOfWeek + 7 -
                 cal.get(Calendar.DAY_OF_WEEK)) % 7);
        if (!cal.getTime().after(new Date())) {
            if (dailyShutdown) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            } else {
                cal.add(Calendar.DAY_OF_MONTH, 7);
            }
        }

        return cal.getTime();
    }
}
