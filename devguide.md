# IBC dev guide

## Compile

* install ant:
    * MacOS: `brew install ant`
* make sure IB Gateway is installed
* set env var pointing to IB Gateway jar files, for example: `export IBC_BIN=${HOME}/Applications/IBGateway978/jars/`
* change to top-level of project, for example: `cd IBC`
* edit `<property name="ver">` in build.xml with the desired version number
* run without args: `ant`
* commit `IBC.jar` to repository

## Deploy

In Java Agent Mode, only the `IBC.jar` file is needed, so it is possible to simply download the jar file into deployment environments.
