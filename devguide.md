# IBC dev guide

## Compile and Release

* install ant:
    * MacOS: `brew install ant`
* make sure IB Gateway is installed
* set env var pointing to IB Gateway jar files, for example: `export IBC_BIN=${HOME}/Applications/IBGateway978/jars/`
* change to top-level of project, for example: `cd IBC`
* edit `<property name="ver">` in build.xml to match the version that will be used for the release.
* run without args: `ant`
* commit `IBC.jar` to repository
* create a release in GitHub, attaching the Linux/Mac/Window zip files which were created by ant and stored in the (not committed) dist folder.

# Deployment Notes

If running in Java Agent Mode, only the `IBC.jar` file is needed (not any of the scripts), so it is possible to simply download the jar file into deployment environments.
