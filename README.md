<!-- Logo and description -->
<div align="center">
    <img src="https://github.com/user-attachments/assets/5308385b-8846-46ed-9c0c-5dea107db3fd"/>
    <!--h3 align="center">...because Java desktop development isn't dead</h3-->
</div>

<!-- Badges -->
<br>
<div align="center">
    <img alt="Status Badge" src="https://img.shields.io/badge/Status-Design phase-yellow"/>
    <img alt="Windows CI Badge" src="https://github.com/olepoeschl/Upme/actions/workflows/ci-windows.yml/badge.svg?branch=main"/>
    <img alt="Windows CI Badge" src="https://github.com/olepoeschl/Upme/actions/workflows/ci-linux.yml/badge.svg?branch=main"/>
    <img alt="Windows CI Badge" src="https://github.com/olepoeschl/Upme/actions/workflows/ci-macos.yml/badge.svg?branch=main"/>
    <img alt="License Badge" src="https://img.shields.io/badge/License-MIT-blue"/>
</div>
<h2></h2>

![Components and flow diagram](docs/target/flow.png)

__Upme__ is a simple library to add self-update capabilities to Java desktop applications. Its main goals are ease of use, extensibility and robustness.
While the diagram above shows a simplified use-case, the library also provides more low-level control.

## Features
- [ ] super high-level wrapper for the average use-case: update to the latest version
- [ ] archive-based updates
    - [ ] keep important application files, e.g. user data
    - [ ] optional restart after the update
    - [ ] rollback on error
    - [ ] create log file
    - [ ] cleanup temporary files
    - [ ] automatically ask for admin permissions if needed
    - [ ] full replacement of all application files
    - [ ] incremental patch based on changes
- [ ] installer-based updates
- [ ] select desired new version out of a list of available updates
- [ ] fetch updates from custom web server
- [ ] fetch updates from Github Releases

<!--## Getting started-->
<!-- How to use Upme in your project, with code snippets -->

<!-- ## Example project -->
<!-- link a separate repository as a working example -->

<!--
## How it works
Upme breaks the update process down into three stages. The diagram below illustrates the high-level architecture:
<br>![Upme Core Workflow](docs/target/c2.png)
-->

<!--
## Contributing
Contributions of all forms are welcome. If you are not sure about something, open an issue first or comment on an existing one, if applicable. Feel free to submit a pull request as soon as you are ready.
Thank you.
-->
