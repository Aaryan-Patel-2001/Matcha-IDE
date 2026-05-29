# Matcha — Local Setup & Usage Guide

This guide walks you through building the Matcha IntelliJ Platform plugin from source,
running it inside a sandbox IDE, and installing it into your own IntelliJ IDEA or
Android Studio so you can use it on any project.

Matcha analyzes your Android app's source code (first‑party code and third‑party
libraries) to help you fill out the Google Play **Data Safety** section, and can
generate a Data Safety CSV based on the analysis and your input. All analysis runs
locally on your machine.

---

## 1. Prerequisites

| Requirement | Version | Notes |
|-------------|---------|-------|
| **JDK** | **17** | Required to build against IntelliJ Platform 2024.2+. Newer JDKs may work for the Gradle run but 17 is the supported target. |
| **Git** | any recent | To clone the repo. |
| **A JetBrains IDE to install into** | IntelliJ IDEA 2024.2–2024.3, or Android Studio on the matching platform build (242–243) | Only needed if you want to install the packaged plugin (Section 6). The sandbox in Section 4 does not require a separate IDE. |
| **Internet access** | — | The first build downloads the IntelliJ Platform (~1 GB) and Gradle dependencies. |

You do **not** need to install Gradle yourself — the repo ships with the Gradle
wrapper (`./gradlew`), which downloads the correct Gradle version (8.7).

### Check your Java version

```bash
java -version
```

You should see version 17. If you have multiple JDKs, point `JAVA_HOME` at a JDK 17
install before running Gradle, e.g. on macOS:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

---

## 2. Clone the repository

```bash
git clone <your-fork-or-repo-url> Matcha-IDE
cd Matcha-IDE
```

---

## 3. Build the plugin

From the repository root:

```bash
./gradlew buildPlugin
```

On Windows use `gradlew.bat buildPlugin`.

The first run downloads the IntelliJ Platform and may take several minutes. When it
finishes you should see `BUILD SUCCESSFUL`, and the installable plugin archive will be
at:

```
build/distributions/Matcha-0.9.0.zip
```

(The exact filename tracks `pluginVersion` in `gradle.properties`.)

---

## 4. Run the plugin in a sandbox IDE (recommended for trying it out)

The quickest way to use Matcha without touching your real IDE is the `runIde` task,
which launches a clean, isolated IntelliJ IDEA Community instance with the plugin
pre‑installed:

```bash
./gradlew runIde
```

A separate IDE window opens. Inside it:

1. **Open or create an Android/Java project** (`File → Open…`). Matcha works on Java,
   Kotlin/Groovy, and XML manifest files.
2. Open the **Privacy Labels** tool window (bottom tool‑window bar, or
   `View → Tool Windows → Privacy Labels`).
3. Use the plugin (see Section 5).

This sandbox keeps its own settings and plugin list, so nothing leaks into your normal
IDE setup. Close the window to stop it.

---

## 5. Using Matcha on a project

Once the plugin is active (sandbox or installed), open a project and use the
**Privacy Labels** tool window:

- **Tasks / Overview tree** — Matcha scans the project and lists detected data
  practices: data accessed by your own code (annotated via `@DataAccess` /
  `@DataTransmission`) and data collected/shared by third‑party libraries.
- **Code inspections & quick‑fixes** — Matcha highlights API calls that may access or
  transmit personal data and offers quick‑fixes (Alt+Enter) to add the appropriate
  annotation. The guide panel on the right explains each required/optional step.
- **Third‑party SDK review** — for detected libraries, Matcha shows the SDK's default
  data usage and lets you confirm/adjust each `<data>` entry, then mark it `verified`.
- **Refresh** — click **Refresh** in the tool window after editing code to re‑run the
  analysis.
- **Generate the Data Safety CSV** — click **Generate Data Safety Section CSV** to
  export a CSV reflecting the current analysis plus your inputs, which you can use to
  fill out the Google Play Data Safety form.

> Tip: Matcha relies on the bundled **Java** and **Groovy** plugins, so make sure those
> are enabled in whichever IDE you run it in (they are enabled by default in IntelliJ
> IDEA and Android Studio).

---

## 6. Install the packaged plugin into your own IDE

To use Matcha in your day‑to‑day IntelliJ IDEA or Android Studio (rather than the
sandbox):

1. Build the archive (Section 3) — `build/distributions/Matcha-0.9.0.zip`.
2. In your IDE: **Settings/Preferences → Plugins → ⚙ (gear icon) →
   Install Plugin from Disk…**
3. Select `Matcha-0.9.0.zip`.
4. Restart the IDE when prompted.

> **Compatibility:** this build targets platform builds **242–243** (IntelliJ IDEA /
> Android Studio 2024.2 and 2024.3). Installing into an older or much newer IDE will be
> rejected by the plugin compatibility check. To target a different platform, change
> `platformVersion`, `pluginSinceBuild`, and `pluginUntilBuild` in `gradle.properties`
> and rebuild.

After restart, open the **Privacy Labels** tool window and use it as described in
Section 5.

---

## 7. Verify compatibility (optional)

To confirm the plugin is binary‑compatible with the supported IDE builds, run the
IntelliJ Plugin Verifier:

```bash
./gradlew verifyPlugin
```

It downloads the recommended IDE builds and reports `Compatible` for each. Reports are
written to `build/reports/pluginVerifier/`. Deprecated/experimental/internal API
*usages* are reported as warnings and do not fail the build.

---

## 8. Common Gradle tasks

| Task | Purpose |
|------|---------|
| `./gradlew buildPlugin` | Compile and package the plugin into `build/distributions/`. |
| `./gradlew runIde` | Launch a sandbox IDE with the plugin installed. |
| `./gradlew verifyPlugin` | Check compatibility against the recommended IDE builds. |
| `./gradlew compileJava` | Compile sources only (fast feedback). |
| `./gradlew clean` | Remove build outputs. |

---

## 9. Troubleshooting

- **`BUILD FAILED` mentioning Java/JDK version or "release 17":** you are not building
  with JDK 17. Set `JAVA_HOME` to a JDK 17 install (see Section 1) and retry.
- **First build is very slow / appears stuck:** it is downloading the IntelliJ Platform
  (~1 GB) and dependencies. Let it finish; subsequent builds are cached and fast.
- **`runIde` window shows no "Privacy Labels" tool window:** make sure the bundled
  **Java** and **Groovy** plugins are enabled, and open an actual project (the tool
  window populates from project analysis).
- **Plugin won't install into your IDE ("incompatible"):** your IDE's build number is
  outside the 242–243 range. Adjust `platformVersion` / `pluginSinceBuild` /
  `pluginUntilBuild` in `gradle.properties` and rebuild.
- **Gradle can't find a JDK / wrong JDK picked up:** configure the Gradle JVM explicitly
  via `org.gradle.java.home` in `gradle.properties` or in your IDE's Gradle settings.

---

For background on the project and citation info, see [README.md](README.md).
