# IBZ

IBZ automates elements of [Interactive
Brokers](https://www.interactivebrokers.com) [Trader Workstation and
Gateway](https://www.interactivebrokers.com/en/index.php?f=14099#tws-software)
to simplify automated trading via the [Interactive Brokers
API](http://interactivebrokers.github.io). IBZ can automatically:

* Enter login credentials at startup, and optional 2FA code via the command server
* Dismiss nuisance dialogs:
    * "Accept incoming connection?" API dialog
    * "Blind Trading" warning dialog
    * "Newer Version" upgrade dialog
    * "System not currently available" warning
    * "NSE Compliance" Indian regulatory dialog
    * "Password Notice" password expiry dialog
* Shut down TWS or Gateway remote, which can be useful when running in the
  cloud or an inaccessible computer
* Prevent auto-logoff in TWS <974, enabling TWS to run indefinitely.
* Shut down gracefully at a specified day of the week and time, or at a
  specified time every day.

**Contents**

* [Installation](#installation)
* [Environment Variables](#environment-variables)
* [Command Server](#command-server)
* [Support](#support)
* [Building IBZ](#building-ibz)
* [License](#license)
* [History](#history)


## Installation

### Java

On macOS, install the [Oracle Java
runtime](https://www.oracle.com/java/technologies/javase-jre8-downloads.html)
before continuing.


### Trader Workstation

Download TWS or Gateway from the [Interactive Brokers web
site](https://www.interactivebrokers.com/en/index.php?f=14099#tws-software).
The offline installer is recommended to avoid undesirable automatic updates of
a working system.

TWS must have English selected in the login dialog for text to be recognized.
The last selected language is remembered on subsequent restarts.


### Download IBZ

Download the latest Jar file from the [GitHub releases
page](https://github.com/IbcAlpha/IBZ/releases/latest) and save it to the TWS
installation folder.

<table>

<tr>
<th>Platform</th>
<th>Default Installation Folder</th>
</tr>

<tr>
<td>Windows</td>
<td><kbd>C:\Jts</kbd</td>
</tr>

<tr>
<td>Linux</td>
<td><kbd>/home/&lt;username&gt;/Jts</kbd></td>
</tr>

<tr>
<td>macOS</td>
<td><kbd>/Users/&lt;username&gt;/Applications</kbd></td>
</tr>

</table>


### Agent Installation

Locate `tws.vmoptions` or `ibgateway.vmoptions` by following [the Interactive Brokers documentation](https://www.interactivebrokers.com/en/software/tws/usersguidebook/priceriskanalytics/custommemory.htm).
Add a new line to the file: `-javaagent:/home/joe/Jts/IBZ.jar` (UNIX) or
`-javaagent:C:/Jts/IBZ.jar`, using the complete path to the IBZ jar. Example:

```
-javaagent:C:/Jts/IBZ.jar
```

IBZ will load during TWS startup, however it will not activate unless
environment variables are set to configure it.


### Create Folder

Create a folder for startup scripts and log files. The folder should use
[Windows integrated folder
encryption](https://support.microsoft.com/en-au/help/4026312/windows-10-how-to-encrypt-a-file)
if available, and be stored in a private location, such as the computer
account's home directory.

<table>

<tr>
<th>Platform</th>
<th>Suggested Location</th>
</tr>

<tr>
<td>Windows</td>
<td><kbd>C:\Users\&lt;username&gt;\Documents\IBZ</kbd</td>
</tr>

<tr>
<td>Linux</td>
<td><kbd>/home/&lt;username&gt;/ibz</kbd></td>
</tr>

<tr>
<td>macOS</td>
<td><kbd>/Users/&lt;username&gt;/ibz</kbd></td>
</tr>

</table>

For example, a folder named `C:\Users\joe\Documents\IBZ` may be
created on Windows.


### Create Startup Script

Within the folder, create a startup script for each account, to set environment
variables then start TWS. See [Environment Variables](#environment-variables)
for a complete list.

#### Windows

Example name: `C:\Users\joe\Documents\IBZ\paper.bat`

```batch
SET IBZ_LOG_OUTPUT_PATH="%HOMEDRIVE%%HOMEPATH%\Documents\IBZ\paper.log"
SET IBZ_LOGIN_ID="myacct1234"
SET IBZ_PASSWORD="mySecretPassword"
SET IBZ_TRADING_MODE="paper"
C:\Jts\979\tws.exe
```

#### Linux / macOS

Example name: `/home/joe/ibz/paper.sh`

```bash
#!/bin/sh
export IBZ_LOG_OUTPUT_PATH="$HOME/ibz/paper.log"
export IBZ_LOGIN_ID="myacct1234"
export IBZ_LOGIN_PASSWORD="mySecretPassword"
export IBZ_TRADING_MODE="paper"
$HOME/Jts/979/tws
```

Mark the script executable using `chmod +x paper.sh`.


### Test Startup Script

Test the script by double-clicking it or running it from a terminal. TWS should
appear, and the login credentials should be automatically entered.


### Recommended TWS Settings

IBZ works best with particular TWS settings configured.

* **Lock and Exit &rarr; Auto restart**: Enable to reduce the manual
  authentications required for a two-factor authenticated account from daily to
  weekly

* **API &rarr; Settings &rarr; Enable ActiveX and Socket Clients**: Enable to allow API
  programs to connect

* **API &rarr; Settings &rarr; Enable Read-Only API**: Disable to allow API programs to
  manage orders

* **API &rarr; Settings &rarr; Allow connections from localhost only**: Disable to
  allow API programs to connect over the network


## Environment Variables

### Login

Control how IBZ interacts with the Interactive Brokers login dialog.

<table>

<tr>
<th>Setting</th>
<th>Default</th>
<th>Description</th>
</tr>

<tr>
<td><kbd>IBZ_LOGIN_ID</kbd>
<td>
<td>Account username

<tr>
<td><kbd>IBZ_PASSWORD</kbd>
<td>
<td>Account password. The dialog is automatically submitted if both
    <kbd>IBZ_LOGIN_ID</kbd> and <kbd>IBZ_PASSWORD</kbd> are supplied

<tr>
<td><kbd>IBZ_TRADING_MODE</kbd>
<td><kbd>live</kbd>
<td>
    <p>
    Select the TWS >=955 trading mode
    </p>
    <table>
    <tr>
        <th><kbd>paper</kbd>
        <td>Use paper trading
    <tr>
        <th><kbd>live</kbd>
        <td>Use live trading
    </table>

<tr>
<td><kbd>IBZ_READONLY_LOGIN</kbd>
<td><kbd>no</kbd>
<td>
    <p>
    Respond to the two-factor authentication dialog
    </p>
    <table>
    <tr>
        <th><kbd>yes</kbd>
        <td>Click the <em>Enter Read Only</em> button. Market data is available
            but order management is prohibited
    <tr>
        <th><kbd>no</kbd>
        <td>Do nothing; a security code must be entered manually or via the
            <kbd>SECURITYCODE</kbd> command server request
    </table>

<tr>
<td><kbd>IBZ_EXISTING_SESSION_ACTION</kbd>
<td><kbd>manual</kbd>
<td>
    <p>
    Respond to duplicate session warning dialogs
    </p>
    <table>
    <tr>
        <th><kbd>primary</kbd>
        <td>Local session continues and remote session is cancelled.
            Automatically reconnect if a remote session ends the local session
    <tr>
        <th><kbd>primaryoverride</kbd>
        <td>Local session terminates and remote session proceeds. Do not
            reconnect if remote session ends the local session
    <tr>
        <th><kbd>secondary</th>
        <td>Local session exits so and remote session continues
    <tr>
        <th><kbd>manual</th>
        <td>User must handle the dialog
    </table>
</table>


### FIX Login

FIX CTCI requires credentials for FIX order routing, and optional credentials
for market data. FIX order routing credentials need only be supplied when
<kbd>IBZ_FIX</kbd> is <kbd>yes</kbd>.

<table>

<tr>
<th>Setting</th>
<th>Default</th>
<th>Description</th>
</tr>

<tr>
<td><kbd>IBZ_FIX</kbd>
<td><kbd>no</kbd>
<td>
    <p>
    IB Gateway connection mode
    </p>
    <table>
    <tr>
        <th><kbd>yes</kbd>
        <td>Start IB Gateway in FIX CTCI mode
    <tr>
        <th><kbd>no</kbd>
        <td>Start IB Gateway in IB API mode
    </table>

<tr>
<td><kbd>IBZ_FIX_LOGIN_ID</kbd>
<td>
<td>FIX CTCI username

<tr>
<td><kbd>IBZ_FIX_PASSWORD</kbd>
<td>
<td>FIX CTCI password

</table>


### Window Management

<table>

<tr>
<th>Setting</th>
<th>Default</th>
<th>Description</th>
</tr>

<tr>
<td><kbd>IBZ_MINIMIZE_MAIN_WINDOW</kbd>
<td><kbd>no</kbd>
<td>Set to <kbd>yes</kbd> to minimise TWS when it starts

<tr>
<td><kbd>IBZ_MAXIMIZE_MAIN_WINDOW</kbd>
<td><kbd>no</kbd>
<td>Set to <kbd>yes</kbd> to maximize TWS when it starts

<tr>
<td><kbd>IBZ_SHOW_ALL_TRADES</kbd>
<td><kbd>no</kbd>
<td>
    For TWS &lt;955, display the Trade log window at startup, and set the 'All'
    checkbox to ensure the API reports all executions that occurred during the
    past week. Moreover, any attempt by the user to change any of the 'Show
    trades' checkboxes is ignored; similarly if the user closes the Trades log,
    it is immediately re-displayed with the 'All' checkbox set. If set to 'no',
    IBZ does not interact with the Trades log, and only the current session's
    executions are returned via the API (unless the user changes the Trades log
    checkboxes).

</table>


### Nuisance Dialogs

<table>

<tr>
<th>Setting</th>
<th>Default</th>
<th>Description</th>
</tr>

<tr>
<td><kbd>IBZ_DISMISS_PASSWORD_EXPIRY_WARNING</kbd>
<td><kbd>no</kbd>
<td>
    Warning: setting DismissPasswordExpiryWarning=yes will mean you will not be
    notified when your password is about to expire. You must then take other
    measures to ensure that your password is changed within the expiry period,
    otherwise IBZ will not be able to login successfully.

<tr>
<td><kbd>IBZ_DISMISS_NSE_COMPLIANCE_NOTICE</kbd>
<td><kbd>yes</kbd>
<td>
    Indian versions of TWS may display a password expiry notification dialog
    and a NSE Compliance dialog. These can be dismissed by setting the
    following to yes. By default the password expiry notice is not dismissed,
    but the NSE Compliance notice is dismissed. 

<tr>
<td><kbd>IBZ_ALLOW_BLIND_TRADING</kbd>
<td><kbd>no</kbd>
<td>
    If you attempt to place an order for a contract for which you have no
    market data subscription, TWS displays a dialog to warn you against such
    blind trading. yes   means the dialog is dismissed as though the user had
    clicked the 'Ok' button: this means that you accept the risk and want the
    order to be submitted. no    means the dialog remains on display and must
    be handled by the user.

<tr>
<td><kbd>IBZ_ACCEPT_NON_BROKERAGE_ACCOUNT_WARNING</kbd>
<td>yes
<td>
    Logging in to a paper-trading account results in TWS displaying a dialog
    asking the user to confirm that they are aware that this is not a brokerage
    account. Until this dialog has been accepted, TWS will not allow API
    connections to succeed. Setting this to 'yes' (the default) will cause IBZ
    to automatically confirm acceptance. Setting it to 'no' will leave the
    dialog on display, and the user will have to deal with it manually.

</table>


### Logging

<table>

<tr>
<th>Setting</th>
<th>Default</th>
<th>Description</th>
</tr>

<tr>
<td><kbd>IBZ_SEND_TWS_LOGS_TO_CONSOLE</kbd>
<td><kbd>no</kbd>
<td>
    Copy TWS logs (<em>Account &rarr; Diagnostics &rarr; TWS Logs</em>) to the
    IBZ log. This is especially useful to integrate with log management
    facilities of a container orchestrator like Docker or Kubernetes

<tr>
<td><kbd>IBZ_LOG_OUTPUT_PATH</kbd>
<td>
<td>If set, IBZ directs any log output to the file at this path rather than
    wherever TWS chooses.

<tr>
<td><kbd>IBZ_LOG_COMPONENTS</kbd>
<td><kbd>never</kbd>
<td>
    <p>
    Log each window's component hierarchy, and the values of text boxes and
    labels. This may impact performance and increase log size
    </p>
    <table>
    <tr>
        <th><kbd>open</kbd><br>
            <kbd>yes</kbd><br>
            <kbd>true</kbd>
        <td>Log each window when it is first opened
    <tr>
        <th><kbd>activate</th>
        <td>Log each window when it is made active
    <tr>
        <th><kbd>never</kbd><br>
            <kbd>no</kbd><br>
            <kbd>false</kbd>
        <td>Never log window structure information
    </table>
</table>


### API Control

<table>

<tr>
<th>Setting</th>
<th>Default</th>
<th>Description</th>
</tr>

<tr>
<td><kbd>IBZ_READONLY_API</kbd>
<td><kbd>yes</kbd>
<td>
    <p>
    Modify the <em>Enable Read-Only API</em> setting during startup
    </p>
    <table>
    <tr>
        <th><kbd>yes</kbd>
        <td>API programs cannot submit, modify or cancel orders
    <tr>
        <th><kbd>no</kbd>
        <td>API programs can do these things.
    <tr>
        <td><em>(unset)</em>
        <td>Existing TWS/Gateway configuration is unchanged.
    </table>
<tr>
<td><kbd>IBZ_ACCEPT_INCOMING_CONNECTION</kbd>
<td><kbd>manual</kbd>
<td>
    <p>
    Specify how to answer incoming API connection confirmation dialogs. Use of <kbd>reject</kbd> with explicitly
    configured trusted IP addresses in the API configuration page is encouraged where appropriate
    </p>
    <table>
    <tr>
        <th><kbd>accept</kbd>
        <td>Accept incoming API connections
    <tr>
        <th><kbd>reject</kbd>
        <td>Reject incoming API connections
    <tr>
        <th><kbd>manual</kbd>
        <td>User must manually answer incoming API connection dialogs
    </table>

<tr>
<td><kbd>IBZ_OVERRIDE_TWS_API_PORT</kbd>
<td>
<td>
    <p>
    Modify the <em>Socket port</em> API setting during startup
    </p>
    <table>
    <tr>
        <td><em>(number)</em>
        <td>Modify the TWS API 'Socket port' during startup
    <tr>
        <td><em>(unset)</em>
        <td>No change
    </table>


</table>


### Command Server Settings

<table>

<tr>
<th>Setting</th>
<th>Default</th>
<th>Description</th>
</tr>

<tr>
<td><kbd>IBZ_COMMAND_SERVER_PORT</kbd>
<td>0
<td>
    Do NOT CHANGE THE FOLLOWING SETTINGS unless you intend to issue commands to IBZ
    (for example using telnet). Note that these settings have nothing to do with
    running programs that use the TWS API.
    <p>
    The port that IBZ listens on for commands such as "STOP". DO NOT set this to
    the port number used for TWS API connections. There is no good reason to change
    this setting unless the port is used by some other application (typically
    another instance of IBZ). The default value is 0, which tells IBZ not to start
    the command server

<tr>
<td><kbd>IBZ_CONTROL_FROM</kbd>
<td>
<td>
    A comma separated list of ip addresses, or host names, which are allowed
    addresses for sending commands to IBZ.  Commands can always be sent from
    the same host as IBZ is running on.

<tr>
<td><kbd>IBZ_BIND_ADDRESS</kbd>
<td>
<td>
    Specifies the IP address on which the Command Server is to listen. For a
    multi-homed host, this can be used to specify that connection requests are
    only to be accepted on the specified address. The default is to accept
    connection requests on all local addresses.

<tr>
<td><kbd>IBZ_COMMAND_PROMPT</kbd>
<td>
<td>
    The specified string is output by the server when the connection is first
    opened and after the completion of each command. This can be useful if
    sending commands using an interactive program such as telnet. The default
    is that no prompt is output.

<tr>
<td><kbd>IBZ_SUPPRESS_INFO_MESSAGES</kbd>
<td><kbd>yes</kbd>
<td>
    Some commands can return intermediate information about their progress.
    This setting controls whether such information is sent. The default is that
    such information is not sent.

</table>


### Other

<table>


<tr>
<td><kbd>IBZ_STORE_SETTINGS_ON_SERVER</kbd>
<td>
<td>If you wish to store a copy of your TWS settings on IB's servers as well as
    locally on your computer, set this to 'yes': this enables you to run TWS on
    different computers with the same configuration, market data lines, etc. If
    set to 'no', running TWS on different computers will not share the same
    settings. If no value is specified, TWS will obtain its settings from the
    same place as the last time this user logged in (whether manually or using
    IBZ).

<tr>
<td><kbd>IBZ_AUTO_CLOSEDOWN</kbd>
<td><kbd>no</kbd>
<td>
    <p>
    Set this to `no` to prevent TWS's daily auto closedown.
    <p>
    TWS 974 and Gateway 975 changed how autologoff works. When the configured time
    approaches, logoff can be deferred once by changing the autologoff time in the
    'Exit Session Setting' dialog as in earlier versions, but when the new time
    arrives, TWS will logoff even if the autologoff time is changed again. The
    `IbAutoClosedown=no` setting therefore no longer works properly.
    <p>
    Configure the TWS 974 and Gateway 975 auto-restart function, and configure IBZ
    to run as a Java agent.
</td>

<tr>
<td><kbd>IBZ_CLOSEDOWN_AT</kbd>
<td>
<td>
    <p>
    NB: starting with TWS 974 this is no longer a useful option for TWS, because of
    changes in TWS 974's autologoff handling. However it can still be useful with
    the Gateway.
    <p>
    To tell IBZ to tidily close TWS at a specified time every day, set this value
    to <hh:mm>, for example: ClosedownAt=22:00
    <p>
    To tell IBZ to tidily close TWS at a specified day and time each week, set this
    value to <dayOfWeek hh:mm>, for example: ClosedownAt=Friday 22:00
    <p>
    Note that the day of the week must be specified using your default locale. Also
    note that Java will only accept characters encoded to ISO 8859-1 (Latin-1).
    This means that if the day name in your default locale uses any non-Latin-1
    characters you need to encode them using Unicode escapes (see
    http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#3.3 for
    details). For example, to tidily close TWS at 12:00 on Saturday where the
    default locale is Simplified Chinese, use the following:
    <p>
    ClosedownAt=\u661F\u671F\u516D 12:00


<tr>
<td><kbd>IBZ_SAVE_TWS_SETTINGS_AT</kbd>
<td>
<td>
    <p>
    Automatically save TWS settings on a schedule. You can specify one or more
    specific times, like this:
    <p>
    SaveTwsSettingsAt=HH:MM [ HH:MM]... 
    <p>
    for example: SaveTwsSettingsAt=08:00   12:30 17:30
    <p>
    Or you can specify an interval at which settings are to be saved, optionally
    starting at a specific time and continuing until another time, like this:
    <p>
    Every n [{mins | hours}] [hh:mm] [hh:mm]
    <p>
    where the first hh:mm is the start time and the second is the end time. If
    you don't specify the end time, settings are saved regularly from the start
    time till midnight. If you don't specify the start time. settings are saved
    regularly all day, beginning at 00:00. Note that settings will always be
    saved at the end time, even if that is not exactly one interval later than
    the previous time. If neither 'mins' nor 'hours' is specified, 'mins' is
    assumed.
    </p>
    <table>
    <tr>
        <th colspan="2">Examples
    <tr>
        <td rowspan="2">Every 30 mins all day from 00:00
        <td><kbd>Every 30</kbd>
    <tr>
        <td><kbd>Every 30 mins</kbd>
    <tr>
        <td rowspan="3">Every hour from 08:00 until 00:00
    <tr>
        <td><kbd>Every 1 hours 08:00</kbd>
    <tr>
        <td><kbd>Every 1 hours 08:00 00:00</kbd>
    <tr>
        <td>Every 90 mins from 08:00 until 17:43
        <td><kbd>Every 90 08:00 17:43</kbd>
    </table>

</table>


## Command Server

IBZ can respond to commands sent by another program over the network. It is
enabled by the <kbd>IBZ_COMMAND_SERVER_PORT</kbd> variable.

### Requests

The command server reads newline-terminated requests and writes
newline-terminated replies.

<table>

<tr>
<th>Command
<th>Description

<tr>
<td><kbd>EXIT</kbd>
<td>Gracefully terminate the connection to the command server

<tr>
<td><kbd>STOP</kbd>
<td>Gracefully terminate TWS

<tr>
<td><kbd>ENABLEAPI</kbd>
<td>Enable the TWS API in TWS settings. If `enable-remote-connections` is
    specified, disable requirement for API connections to come from
    `localhost`. If `disable-readonly-api` is specified, allow API clients to
    trade.

<tr>
<td><kbd>RECONNECTDATA</kbd>
<td>Send CTRL-ALT-F key sequence to TWS, causing it to restart its connections
    to streaming data servers.

<tr>
<td><kbd>RECONNECTACCOUNT</kbd>
<td>Send CTRL-ALT-R key sequence to TWS, causing it to restart its connections
    to the account server.

<tr>
<td><kbd>SECURITYCODE</kbd>
<td>Enter the two-factor authentication security code, if the dialog is
    visible.

</table>

### Responses

<table>

<tr>
<th>Response
<th>Description

<tr>
<td><kbd>INFO</kbd>
<td>Some informational notice

<tr>
<td><kbd>OK</kbd>
<td>The last request completed successfully

<tr>
<td><kbd>ERROR</kbd>
<td>The last request failed

</table>


## Support

Join the [IBC User Group](https://groups.io/g/ibcalpha) to request assistance
or offer suggestions for improvement.

Report bugs via [the group](https://groups.io/g/ibcalpha) or
[GitHub](https://github.com/docker-tws/IBZ/issues). Include the IBZ and
TWS/Gateway versions in use, and a full description of the incorrect behaviour.

IBZ creates a log that can be helpful in diagnosing problems. It is helpful to
attach this log to any problem reports.


## Building IBZ

[Apache Ant](http://ant.apache.org/) is required. The `IBC_BIN` environment
variable must  be set to a folder containing the TWS jar files. For example:

```batch
set IBC_BIN=C:\Jts\963\jars
ant
```

## License

IBZ is licensed under the
[GNU General Public License](http://www.gnu.org/licenses/gpl.html) version 3.


## History

IBZ is a branch of [IBC](https://github.com/IbcAlpha/ibc) that adds support for
auto-restart, and simplifies installation and documentation at the expense of
compatibility with existing installations.

IBC is a fork of
[IBController](https://github.com/ib-controller/ib-controller). Between 2004
and early 2018, Richard King was primary maintainer, developer and supporter
for IBController. For reasons beyond Richard's control, Ricard withdrew from
IBController and forked IBC. IBC is developed and supported to the same
standards of IBController.

The status of IBController seems unclear, so users are invited to switch to
IBC.

We also thank past contributors: Richard King, Steven Kearns, Ken Geis, Ben
Alex and Shane Castle.
