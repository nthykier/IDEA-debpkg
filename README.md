# debpkg - Debian packaging support

![Build](https://github.com/nthykier/IDEA-debpkg/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.nthykier.debpkg.svg)](https://plugins.jetbrains.com/plugin/com.github.nthykier.debpkg)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.nthykier.debpkg.svg)](https://plugins.jetbrains.com/plugin/com.github.nthykier.debpkg)

## Debian packaging support
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
 * Spellchecking of field values with exception for some known fields.
 * Check fields are placed in a paragraph where it makes sense.
 * Folding of long field values with "Description" folded by default.
 * Validate dependency fields for use of unsupported version operators (e.g. in `Provides`) or build profile
   restrictions (in any binary package relation field).

Plus the features listed for Generic deb822 files.

### Generic deb822 files (*.deb822)

 * Basic syntax highlighting.
 * Basic semantic validation (i.e. no duplicate fields)
 * Support for adding / removing comments via the <kbd>Code</kbd> > <kbd>Comment with Line Comment</kbd> feature.
 * Folding of long field values.
 * Bundled spelling dictionary with common Debian package related terms and jargon to avoid some
   false-positive spelling mistakes.

## Getting started

After installing the plugin, it automatically detects relevant files
supported and start applying highlights and relevant validation rules
based on common patterns such as `debian/control` or `*.deb822`.

Note that most of the auto-detection rules relies on the files being placed
in the `debian` directory (such as `debian/control` and
`debian/changelog`). Alternatively, you can set the file type manually
by using IDEA's <kbd>Associate with File Type...</kbd> feature.

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
