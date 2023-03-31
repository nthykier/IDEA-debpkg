<!-- Keep a Changelog guide -> https://keepachangelog.com -->
<!--
- Experimental support for syntax highlighting in `debian/rules` *if* the `Makefile Language` plugin from
  JetBrains is installed.  The feature builds upon the `Makefile Language` plugin  from Jetbrains.  Please see
  https://youtrack.jetbrains.com/issues?q=tag:%20%7BMakefile%20language%20plug-in%7D
  for known issues with the `Makefile` language support.
 -->

# debpkg Changelog

## [Unreleased]
### Added
- Add support for IDEA 2023.1
- Add support for apt deb822-based sources files.
- Add `needs-sudo` as known restriction for `debian/tests/control`.  (Closes: #112)
- Add basic syntax highlighting of APT's `.list` sources file. (Closes: #114)

### Fixed
- Avoid assertion error on creating a new (blank) `debian/control` and the first field typed is *not* `Source:`.  (Closes: #107)
- Avoid assertion error in DEP-5 copyright files when directory is specified rather than a file in `Files:`. (Closes: #109)

### Changed
- Avoid parse errors on empty fields as some deb822 files allow them.  It will trigger a warning or an error
  depending on the file (via an inspection).
- Compile the plugin with Java 11.
- Internal/Choce: Update dependency on `snakeyaml`.
- Internal/Chore: Update build time dependency on `gradle-intellij-plugin` and `lombok`.
- Internal/Chore: Update the CI dependency on `gradle/wrapper-validation-action`.

### Removed
- Remove support for IDEA 2020.2.4.

## [0.0.18]
### Added
- Recognise `debian/tests/control` automatically as a deb822 file
- Recognise the standard fields and most common values for `debian/control/tests`.
- In `debian/tests/control`, show an error if `Tests` and `Test-Command` fields are used in the same paragraph.
- In `debian/tests/control`, show a warning if the `needs-internet` restriction is used without `flaky`.

### Fixed
- Corrected display name of the inspection for non-standard Build-Profiles.

### Changed
- Bump the compat range for IDE to support 2022.3
- When completing known values for a field with multiple values, no longer suggest values already present.
- Which fields support substvars (notably, default to "no substvars" by default).
- Chore: Bump snakeyaml dependency to 1.31.
- Internal/Chore: Update runtime dependency on `commons-text` and `snakeyaml`.
- Internal/Chore: Update build time dependency on `gradle-intellij-plugin`.
- Internal/Chore: Update the CI dependency on `actions/cache`, `actions/setup-java`, and `gradle/wrapper-validation-action`.

## [0.0.17]
### Changed
- Bump the compat range for IDE to support 2022.2 at the cost of 2020.1
- Internal/Chore: Update build time dependency on `gradle-intellij-plugin`.
- Internal/Chore: Update the CI dependency on `actions/setup-java` and `actions/cache`.

## [0.0.16]
### Added
- Add inspection for possibly incorrect architecture field value based on package name.
- Add `DM-Upload-Allowed` as known field and an inspection suggesting removing it as it is obsolete.
- Add inspection for possibly incorrect section field value based on package name.  This inspection
  matches the lintian hint `wrong-section-according-to-package-name`.
- Add `Build-Profiles` as a known field and validation of known build profiles used in that field.
  (Build profiles used in build-dependency fields are still not checked).
- Add inspection for detecting misspelled field names.
- Add inspection for detecting missing `${shlibs:Depends}` dependency.
- Add inspection for detecting non-canonical variants of fields (`depends` -> `Depends` or `XC-Package-Type` -> `Package-Type`).

### Fixed
- Typo of the archive section `electronics`, which caused the plugin to mark the correct spelling as an error and
  promote the spelling error instead.

### Changed
- Internal/Chore: Update the CI dependency on `actions/setup-java`.
- Internal/Chore: Bump lombok dependency to 1.18.24.
- Internal/Chore: Rewrite how problem descriptors are managed internally.
- Internal: Optimize the deb822 lexer by removing some redundant states.

## [0.0.15]
### Added
- Structure view for changelog files to provide an overview of the versions.

### Changed
- Bump the compat range for IDE to support 2022.1
- Internal/Chore: Update build time dependency on `gradle-intellij-plugin`.
- Internal/Chore: Update the CI dependency on `actions/checkout`, `actions/cache`, `actions/setup-java`, and `actions/upload-artifact`.

## [0.0.14]
### Added
- Brace matching support inside the dependency language.
- Add refactor support to rename binary packages.  It will attempt to rename relevant files in debian as
  well (`oldname.foo` -> `newname.foo`).
- Support for auto-completion of packages from `debian/control` inside relation fields.  In "positive" dependency
  fields (`Depends, Recommends, etc.`) for a binary package, then the relation will be versioned by default.

### Changed
- Bump lombok dependency fully to 1.18.22.  Previously 1.18.20 was still used in some cases.
- Field, value and substvars documentation.  It is now done as "hover" navigation rather than "quick nav"
  documentation.  That means it appears when you over the mouse over the element rather than having to
  hold CTRL down while hovering.
- Reformat documentation for fields, values and substvars to fit the conventions for hover documentation.
- Field name suggestions now exclude fields already present in the paragraph.  Note that generally only fields
  present "above" the cursor are considered a part of the paragraph.
- Renaming Source package now also updates the source name in the latest UNRELEASED changelog entry as well.
- Chore: Bump snakeyaml dependency to 1.29 and action/cache CI dependency to 2.1.7.

### Fixed
- Disable "Rename" refactor option for "fake" references (e.g., known fields and values) where it does not
  make sense to use refactoring.
- Provider better quick-nav text for dummy references (i.e., references only present for providing documentation).

## [0.0.13]
### Added
- Proper display names for the file formats, which are used in IDEA's `Override File Type` feature.

### Fixed
- Support multiple dependency profiles for a given dependency, which is permitted by the syntax.
  Previously, this would have tripped a parse error.
- Avoid using `Language` as a Map-key per https://plugins.jetbrains.com/docs/intellij/dynamic-plugins.html#code.
- Internal/Chore: Have Bundle class extend `DynamicBundle` rather than `AbstractBundle` per recommendation of
  JetBrains' documentation.

### Changed
- When dependency profiles are present in a dependency field where it is not permitted, flag the
  profile(s) and not the entire dependency as problematic.
- Bump build time dependencies (org.jetbrains.intellij, org.jetbrains.changelog)
- Internal/Chore: Bump gradle version to 7.3.
- Internal/Chore: Update CI jobs.
- Internal/Chore: Bump build-time dependencies on `org.jetbrains.intellij` and `org.jetbrains.changelog`.
- Bump the compat range for IDE to support 2021.3

## [0.0.12]
### Changed
- Bump snakeyaml dependency to 1.29
- Bump CI dependencies (actions/{cache,checkout,create-release,upload-release-asset, upload-artifact}, gradle/wrapper-validation-action)
- Bump the compat range for the IDE to support 2021.2, no code changes required.

## [0.0.11]
### Changed
- Bump the compat range for the IDE to support 2021.1, no code changes required.
- Bump snakeyaml dependency to 1.28
- Bump lombok dependency to 1.18.20

## [0.0.9]
### Added
- Add an inspection for duplicating the field value of the `Source` paragraph
  in a `Package` paragraph of `debian/control`.
- Add component-prefixed versions of sections to the list of known keywords
  for the `Section` field.
- Add inspection for the value of the `Section` starting with `main/` as
  the main component is the default and is customarily omitted.
- Add code completion of file names in DEP-5 copyright files.
- Add `XB-Important` and `Protected` as known fields in `debian/control`.
- Add inspection for using `XB-Important` without `Protected`.  The latter
  is also supported by dpkg and therefore provides better coverage provided
  dpkg and APT is new enough.

### Changed
- Optimized the number of single character whitespace tokens emitted by the
  lexer for `debian/changelog` in common cases.  This should reduce the
  memory usage slightly for this file type.
- The date parser for `debian/changelog` is now much more forgiving about
  the signoff dates.  It will now accept dates that are incorrectly
  formatted.  Invalid dates are instead detected by an inspection along
  with a quick fix (if the plugin recognises the date format).
- Multiple lines of comments in deb822 files can now be folded and are
  folded by default.
- Field lookups for known fields in `debian/control` now accounts for the
  `X-` prefix (plus variants such as `XB-` and `XC-`).
- Renamed and reordered inspection settings so they are now under `Debian`.
  Previously, they were under `DebianControl`.

### Fixed
- Whitespace only lines in `debian/changelog` could trip parser errors.
  Note that tab characters are still considered syntax errors.
- Fixed an assertion error if `debian/changelog` contained `* dir/:`.
- The "linkification" of closed bugs in `debian/changelog` no longer
  "misses" `Closes` clauses if there are multiple of them in the same entry.
  This was mostly seen with entries containing a sublist with per-item `Closes`.
- The "linkification" of closed bugs in `debian/changelog` no longer
  includes leading whitespace in the link.  Previously, this could happen
  if the `Closes` spanned multiple lines.
- The `debian/control` annotator was neutered due to a regression that is now
  fixed.

## [0.0.8]
### Added
- The `debian/control` support now includes a find usage contributor for files
  and paths.  This enables the safe delete will pick up if you delete a file
  still referenced from `debian/control`.
- The `debian/changelog` code can now identify web-addresses and turn them
  into links.  Along with this, bug numbers mentioned after `Closes:` are now
  linkified as well (provided they follow the Debian or Ubuntu bug patterns).
- URLs in field values in deb822-based files are now turned into links.

### Changed
- The `debian/control` support will now make a reference of each part of the
  path in the `Files` fields.  These work better with the built-in refactoring
  support (but still has rough edges around moving things between directories).
- The `debian/changelog` parser now attempts to understand change entries as
  multiline entries.  This is supposed to be invisible to the user but can be
  an obvious source of regressions.
- Tweak the `debian/changelog` spell check exclusion regular expressions.

### Fixed
- Fixed a bug where renaming a directory via refactoring would remove the
  trailing slash after the directory in the `Files`-field in
  `debian/copyright`.  Now refactoring will correctly preserve the directory
  separators.

## [0.0.7]
### Added
- More packaging terms to the built-in dictionary.
- Custom exclusion rules for the `debian/changelog` Spellchecker.  It will
  now attempt to exclude patterns like `override_dh_VALUE` and other common
  filename patterns (e.g. "*.c").
- A quick fix to the Deb822 copyright format that converts directories into
  a directory + wildcard as directories themselves match nothing.
- Support for IDEA 2020.3.
- Valid paths without wildcards in the `Files` field in `debian/copyright`
  are now turned into references (enabling "jump to" and refactoring).

### Changed
- Parse `[ Name ]:` as an alternative to `[ Name ]` token in `debian/changelog`.

### Fixed
- Fixed compatibility with IDEA 2020.1.4.

## [0.0.6]
### Added
- Recognition of `.buildinfo` and `.changes` file.  To avoid false-positives, debpkg will only auto-detect the format
  when the basename appear to follow the `PACKAGE_VERSION_ARCH.EXTENSION` pattern.
- Auto-detect `debian/copyright` as a deb822 file provided it has a `Format:` field with a value of
  `https://www.debian.org/doc/packaging-manuals/copyright-format/1.0/` (including some variants of that value).
- Reliably detect incorrect (whitespace-only) continuation lines and include quick fixes for them.

### Changed
- The Deb822 parser now recognizes GPG signatures.  By default, the GPG lines will be folded.
- Enable folding support for unknown fields.  Previously only known multi-line fields could be folded.
- Folding works better with "fake" single line fields by (a la `Foo:\n value`) where now it folds away the
  leading whitespace (and newline) but keeps the value visible.
- Use title-case for the plugin name.
- Rewrite how the plugin determines whether a field should be spellchecked.  It now relies on a list of
  known fields that are worth spellchecking.  For formats where the plugin does not recognise the fields, it
  falls back to using the name for guessing whether the field should be spellchecked.  Notably `Comment`,
  `Description` and `Disclaimer` will have spellchecking enabled in generic deb822 files.

## [0.0.5]
### Added
- Automatically recognise `.dsc` files as a generic deb822 file.

### Fixed
- Avoid false error if a field is followed by a new line like `Depends:\n foo,...`.  (Closes: #3)
- In the `d/changelog` format, highlight file references in ` * file:\n ...` cases as well.  (Closes: #4)
- Stop recommending the `Rules-Requires-Root` in generic deb822 file if they happen to have a `Source` field.
- Provide a better hover text for unrecognised deb822 fields instead of just dumping an internal name in front of it.
- Avoid false errors on non-native versions in changelog files.

## [0.0.4]
### Added
- Add check for version operator being supported in the current dependency field.  Provides only supports `=`.
- Add check for use of build profiles restrictions in dependency fields that do not support it.
- Add a spell-check dictionary of known packaging terms to avoid common terms being flagged as spelling mistakes.
- Add suggestion to add Rules-Requires-Root in `debian/control` if it is absent.

### Changed
- Rewrite part of the annotation code to make it future proof at the expense of support for IDEA platforms based on 2019.  If you need 2019, please
  remain on 0.0.3 for now.
- Provide a better hover text for file references in the changelog file.
- Rewrite the "duplicate fields" check as an annotation meaning it can no longer be disabled (nor is it severity configurable).
- Add an {...} marker in the placeholder text for multi-line fields that have been folded.

### Removed
- Remove default extension for the Dependency language format (which is only used inside debian/control).  The extension was only used during debugging.

### Fixed
- Fix unhandled exception in annotation of dependency fields with invalid package names (etc.).
- Fix missing error when a package name contained a trailing colon in a dependency field.  This is invalid, but the annotator silently accepted it as valid.

## [0.0.3]
### Added
- Add check to highlight if fields are placed in the wrong paragraph in `debian/control` files. (Closes: #1)
- Add check to ensure that mandatory `Source` and `Package` fields are present in debian/control.
- Add documentation and field-placement level validation for remaining known fields (e.g. `Depends`).  (Closes: #2)
- Detect the documentation pattern ` * FILENAME: CHANGE` in changelogs and automatically link to the file if
  it can be found (globs not supported).
- Parse dependency fields with a different parser enabling basic format checking of the dependency fields.
- Check for invalid version operators dependency fields.
- Add check for some substvars used unconventionally in dependency fields (e.g. `${misc:Depends}` used as a version).
- Add support for folding long field values with `Description` in `debian/control` being folded by default.

### Changed
- Code completion is now more context-aware inside dependency fields.

### Fixed
- Fix misplaced `Package`/`Source` field warning not coping with moving paragraphs around.

## [0.0.2]
### Added
- Add new icons instead of using the default icon from the template.
- Check for misplaced `Package` and `Source` fields.

## [0.0.1]
### Initial version
- Syntax highlighting of `debian/changelog` and `*.dch` files
- Syntax highlighting of `debian/control` and `*.deb822` files
- Code completion support for `debian/control`
- Hover documentation of most fields, field keywords and standard substitution variables.
- Basic but very incomplete detection of invalid values used in field for `debian/control`.