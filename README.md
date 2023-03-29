# debpkg - Debian Packaging Support

![Build](https://github.com/nthykier/IDEA-debpkg/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.nthykier.debpkg.svg)](https://plugins.jetbrains.com/plugin/14724-debian-packaging-support)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.nthykier.debpkg.svg)](https://plugins.jetbrains.com/plugin/14724-debian-packaging-support)

## Debian Packaging Support
<!-- Plugin description -->
Support for common Debian packaging files (e.g. debian/control and debian/changelog).

This is an IDEA plugin (e.g. IntelliJ or PyCharm) that enables support for some
commonly used formats in Debian packaging.
<!-- Plugin description end -->

## Features

### debian/changelog or (*.dch)

 * Basic syntax highlighting with syntactical validation.
 * Spellchecking of changelog content (but not bits you cannot control).
 * Bundled spelling dictionary with common Debian package related terms and jargon to avoid some
   false-positive spelling mistakes.
 * Debian and Ubuntu bug numbers in `Closes` and http/https addresses are linkified.
 * A structure view providing a quick navigation overview for individual versions.
   (<kbd>View</kbd> > <kbd>Tool Windows</kbd> > <kbd>Structure</kbd>)

### debian/control

 * Basic syntax highlighting with syntactical validation plus highlight of known field values.
 * Basic (incomplete) validation of field values.
 * Completion of field names, known values for fields and known substitution variables.
 * Documentation for fields, known values in fields and known substitution variables ("mouse-over")
 * Spellchecking of relevant fields (e.g. `Description`).
 * Check fields are placed in a paragraph where it makes sense.
 * Folding of long field values with `Description` folded by default (also includes comments).
 * Validate dependency fields for use of unsupported version operators (e.g. in `Provides`) or build profile
   restrictions (in any binary package relation field).
 * Warn if a field just duplicates the field of the source paragraph.
 * Refactor support when renaming binary packages (includes renaming files in `debian/*` but not content
   inside `debian/rules`).
 * Detection of some possible mistakes (package name does not match section/architecture, missing substvars)
 * Detect misspellings of known fields such as `Depend` being a typo of `Depends`.
 * Detect non-canonical variants of fields (`depends` -> `Depends` or `XC-Package-Type` -> `Package-Type`).

Plus the features listed for Generic deb822 files.

### debian/tests/control

* Basic syntax highlighting with syntactical validation plus highlight of known field values.
* Basic (incomplete) validation of field values.
* Completion of field names and known values for fields.
* Documentation for fields, known values in fields ("mouse-over")
* Folding of long field values (also includes comments).
* Detect misspellings of known fields such as `Depend` being a typo of `Depends`.
* Detect non-canonical variants of fields (`depends` -> `Depends` or `XC-Package-Type` -> `Package-Type`).

Plus the features listed for Generic deb822 files.

### debian/copyright

 * Conditional detection as a Deb822 file based on the presence of the `Format:` field.  The matching is
   conditional as `debian/copyright` to avoid false-positives for projects that do not use the
   machine-readable Debian copyright format (also known as DEP-5).
 * Completion of field names of common known fields plus file names in the `Files:` field.
 * Documentation for some known fields ("mouse-over")
 * Folding of long field values with `License` and `Copyright` folded by default (also includes comments).
 * Check fields are placed in a paragraph where it makes sense.
 * Basic verification of patterns in the `Files` field.  The plugin only partly supports wildcards
   and will err on the side of assuming a wildcard matches when in doubt.
 * Spellchecking of relevant fields (e.g. `Comment`, `Disclaimer` and `Source`).
 * Valid paths without wildcards in the `Files` fields are turned into references,
   which in turn enables other features such as "jump to" and Refactor support.
 * Detect misspellings of known fields such as `Licenses` being a typo of `License`.
 * Detect non-canonical variants of fields (`files` -> `Files`).

Plus the features listed for Generic deb822 files.

Notably missing features for `debian/copyright`.  The License fields are *not* validated.

### Apt sources (deb822-based) files

* Basic syntax highlighting with syntactical validation plus highlight of known field values.
* Basic (incomplete) validation of field values.
* Completion of field names and known values for fields.
* Documentation for fields, known values in fields ("mouse-over")
* Folding of long field values (also includes comments).
* Detect misspellings of known fields such as `Type` being a typo of `Types`.
* Detect non-canonical variants of fields (`types` -> `Types`).

Plus the features listed for Generic deb822 files.

### Generic deb822 files (*.deb822, *.dsc, *_\*\_\*.buildinfo, *\_\*\_\*.changes)

 * Basic syntax highlighting.
 * Basic semantic validation (i.e. no duplicate fields)
 * Support for adding / removing comments via the <kbd>Code</kbd> > <kbd>Comment with Line Comment</kbd> feature.
 * Folding of long field values.
 * Bundled spelling dictionary with common Debian package related terms and jargon to avoid some
   false-positive spelling mistakes.
 * Folding support to hide GPG signatures.  There is no logic to determine whether the given file permits the
   GPG signature.
 * Spellchecking in selected fields guessed from their names, where the specific subformat does not provide
   a more reliable selection.
 * Quick fix for some syntactical issues with continuation lines.
 * URLs in field values are linkified.

<!--

Commented out because this code/feature is not enabled.

### debian/rules (requires Makefile Language plugin) - /experimental/

*Note*: The [Makefile Language plugin] is bundled with some but not all JetBrains products,
and  you may have to install it manually to activate this feature.

 * Automatically register it as a makefile using the [Makefile Language plugin].
   - Please review [Makefile Language plugin issues] for known issues if you experience any issues with
     how your `debian/rules` file is parsed.

-->

## Getting started

After installing the plugin, it automatically detects relevant files
supported and start applying highlights and relevant validation rules
based on common patterns such as `debian/control` or `*.deb822`.

Note that most of the auto-detection rules relies on the files being placed
in the `debian` directory (such as `debian/control` and
`debian/changelog`).  A few formats that are usually generated (and not hand
edited) will only be auto-detected if the file uses the common pattern for
that file type (e.g. NAME_VERSION_ARCH.changes).  This is deliberate to
reduce false positives in case other plugins react to similar extensions.

Alternatively, you can set the file type manually by using IDEA's
<kbd>Associate with File Type...</kbd> feature.

Once associated, the supported feature set automatically activates on
the relevant editor actions.
 
## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Debian Packaging Support"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/nthykier/IDEA-debpkg/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template



[Makefile Language plugin]: https://plugins.jetbrains.com/plugin/9333-makefile-language/
[Makefile Language plugin issues]: https://youtrack.jetbrains.com/issues?q=tag:%20%7BMakefile%20language%20plug-in%7D
