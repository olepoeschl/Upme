<!-- Logo and description -->
<div align="center">
    <img src="https://github.com/user-attachments/assets/5308385b-8846-46ed-9c0c-5dea107db3fd"/>
    <!--h3 align="center">Effortless self-updates for Java desktop applications</h3-->
    <h3 align="center">...because Java desktop development isn't dead</h3>
</div>

<!-- Badges -->
---

<div align="center">
    <img alt="Status Badge" src="https://img.shields.io/badge/Status-Design phase-yellow"/>
    <img alt="Windows CI Badge" src="https://github.com/olepoeschl/Upme/actions/workflows/ci-windows.yml/badge.svg?branch=main"/>
    <img alt="Windows CI Badge" src="https://github.com/olepoeschl/Upme/actions/workflows/ci-linux.yml/badge.svg?branch=main"/>
    <img alt="Windows CI Badge" src="https://github.com/olepoeschl/Upme/actions/workflows/ci-macos.yml/badge.svg?branch=main"/>
    <img alt="License Badge" src="https://img.shields.io/badge/License-MIT-blue"/>
</div>

---

<!-- What is Upme? -->
Ever wondered why the entire Java ecosystem doesn't provide a single ready-to-use library to add self-update capabilities to desktop apps?
<br>Me too - that's why I create Upme. Its main goals are ease of use, extensibility and robustness.

<!--## Getting started-->
<!-- How to use Upme in your project, with code snippets -->

<!-- ## Example project -->
<!-- link a separate repository as a working example -->


## How it works
Upme breaks the update process down into a clear, three-stage pipeline. The diagram below illustrates the high-level architecture:

![Upme Core Workflow](docs/target/c2.png)

* **PackageResolver:** Identifies the update packages available for your application.
* **PackageProvider:** Retrieves the actual update package, whether from a remote server or local storage.
* **PackageApplier:** Applies the downloaded update package to your application, handling different update strategies like full replacement or patching.

...more details coming soon
<!--... more details or examples for each component, or move to installation instructions-->


<!--
## Contributing
Contributions of all forms are welcome. If you are not sure about something, open an issue first or comment on an existing one, if applicable. Feel free to submit a pull request as soon as you are ready.
Thank you.
-->
