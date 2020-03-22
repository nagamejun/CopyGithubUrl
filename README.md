<p align="left">
  <a href="https://github.com/nagamejun/CopyGithubUrl"><img alt="GitHub Actions status" src="https://github.com/nagamejun/CopyGithubUrl/workflows/CI%20with%20Gradle/badge.svg"></a>
</p>

<h1 align="center">CopyGithubUrl</h1>

### Jetbrains plugin that provides a shortcut to copy the current file path with the current commit on the current branch. Copy only.

<div align="center">
  <img src="https://raw.githubusercontent.com/wiki/nagamejun/CopyGithubUrl/demonstration.gif" alt="demo">
</div>

Installation
------------


This plugin is published on the
[JetBrains Plugin Repository](https://plugins.jetbrains.com/plugin/13982):

    Preferences → Plugins → Browse Repositories → Search for "CopyGithubUrl"

### From Source

Clone this repository:

    $ git clone git@github.com:nagamejun/CopyGithubUrl.git
    $ cd CopyGithubUrl

Update the permissions:

     $ chmod +x ./gradlew

Build the plugin zip file:

    $ ./gradlew buildPlugin

Install the plugin from `./build/distributions/CopyGithubUrl-*.zip`:

    Preferences → Plugins → Install plugin from disk

License
-------------------------------------------------------------------------------

Please see [LICENSE](LICENSE.txt) for details.
