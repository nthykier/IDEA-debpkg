# debpkg - Debian Packaging Support

![Build](https://github.com/nthykier/IDEA-debpkg/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.nthykier.debpkg.svg)](https://plugins.jetbrains.com/plugin/com.github.nthykier.debpkg)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.nthykier.debpkg.svg)](https://plugins.jetbrains.com/plugin/com.github.nthykier.debpkg)

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

### debian/control

 * Basic syntax highlighting with syntactical validation plus highlight of known field values.
 * Basic (incomplete) validation of field values.
 * Completion of field names, known values for fields and known substitution variables.
 * Documentation for fields, known values in fields and known substitution variables ("CTRL + mouse-over")
 * Spellchecking of relevant fields (e.g. `Description`).
 * Check fields are placed in a paragraph where it makes sense.
 * Folding of long field values with `Description` folded by default.
 * Validate dependency fields for use of unsupported version operators (e.g. in `Provides`) or build profile
   restrictions (in any binary package relation field).

Plus the features listed for Generic deb822 files.

### debian/copyright

 * Conditional detection as a Deb822 file based on the presence of the `Format:` field.  The matching is
   conditional as `debian/copyright` to avoid false-positives for projects that do not use the machine-
   readable Debian copyright format (also known as DEP-5).
 * Completion of field names of common known fields.
 * Documentation for some known fields ("CTRL + mouse-over")
 * Folding of long field values with `License` and `Copyright` folded by default.
 * Check fields are placed in a paragraph where it makes sense.
 * Basic verification of patterns in the `Files` field (wildcards are currently not supported).
 * Spellchecking of relevant fields (e.g. `Comment`, `Disclaimer` and `Source`).

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
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "debpkg"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/nthykier/IDEA-debpkg/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
