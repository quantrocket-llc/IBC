# IBC

This is a fork of Richard King's [IBC project](https://github.com/IbcAlpha/IBC), simplified, customized, and enhanced for QuantRocket. General users should utilize the [source repository](https://github.com/IbcAlpha/IBC), not this one.

This fork utilizes Java Agent Mode, which was developed in another fork, [@docker-tws/IBC](https://github.com/docker-tws/IBC). Many thanks to these upstream repositories.

## Java Agent Mode

IBC can run using the Java Instrumentation API. In this mode, the
`tws.vmoptions` or `ibgateway.vmoptions` file is updated to include a new
parameter. This parameter tells Java to load IBC early during the regular TWS
or Gateway startup process. With the `.vmoptions` parameter present, any
attempt to start TWS or Gateway using normal entry points (`.exe` program file
or desktop icons) will cause IBC to be transparently loaded.

The primary advantage of Agent mode is no changes are made to how TWS or
Gateway is started, permitting its auto-restart functionality to operate
correctly. With auto-restart, TWS automatically restarts itself each day rather
than logging off, requiring manual two-factor authentication only once per
week, on Sundays. This vastly increases the time between manual interventions
for a 2FA-enabled account running under IBC.

## Installation

1. Locate `tws.vmoptions` or `ibgateway.vmoptions` by following [the Interactive Brokers documentation](https://www.interactivebrokers.com/en/software/tws/usersguidebook/priceriskanalytics/custommemory.htm).

2. Add the line: `-javaagent:/path/to/IBC.jar` (UNIX) or
   `-javaagent:C:\\IBC\\IBC.jar`, using the complete path to the IBC jar
   included in the release download.

3. You may specify space-separated IBC command line parameters, by placing an
   `=` (equals) sign after the Jar path, then writing the parameters.

### Allowable parameter combinations:

1. No parameters

2. `<iniFile> [<tradingMode>]`

3. `<iniFile> <apiUserName> <apiPassword> [<tradingMode>]`

4. `<iniFile> <fixUserName> <fixPassword> <apiUserName> <apiPassword> [<tradingMode>]`

Where:

```
     <iniFile>       ::= NULL | path-and-filename-of-.ini-file

     <tradingMode>   ::= blank | live | paper

     <apiUserName>   ::= blank | username-for-TWS

     <apiPassword>   ::= blank | password-for-TWS

     <fixUserName>   ::= blank | username-for-FIX-CTCI-Gateway

     <fixPassword>   ::= blank | password-for-FIX-CTCI-Gateway
```

### Examples

Load IBC from `C:\IBC\IBC.jar`, and use `C:\IBC\live.ini` for the INI file:

`-javaagent:C:\\IBC\\IBC.jar=C:\\IBC\\live.ini`

As above, but login with paper trading:

`-javaagent:C:\\IBC\\IBC.jar=C:\\IBC\\live.ini PAPERTRADING`

Environment variables `${...}` can be used to specify trading mode and api or FIX credentials:

`-javaagent:/path/to/IBC.jar=/path/to/config.ini ${TWSUSERID} ${TWSPASSWORD} ${TRADING_MODE}`

### Command Server Protocol

The command server reads newline-terminated requests and writes
newline-terminated replies.

Valid requests:

* `EXIT`: gracefully terminate the connection to the command server
* `STOP`: graefully terminate TWS
* `ENABLEAPI [enable-remote-connections] [disable-readonly-api]`: Enable the TWS API in TWS settings. If `enable-remote-connections` is specified, disable requirement for API connections to come from `localhost`. If `disable-readonly-api` is specified, allow API clients to trade.
* `RECONNECTDATA`: send CTRL-ALT-F key sequence to TWS, causing it to restart its connections to streaming data servers.
* `RECONNECTACCOUNT`: send CTRL-ALT-R key sequence to TWS, causing it to restart its connections to the account server.
* `SECURITYCODE <code>`: enter the two-factor authentication security code, if the dialog is visible.

Responses:

* `INFO ...`: some informational notice
* `OK ...`: the last request completed successfully
* `ERROR ...`: the last request failed



## License

IBC is licensed under the
[GNU General Public License](http://www.gnu.org/licenses/gpl.html) version 3.
