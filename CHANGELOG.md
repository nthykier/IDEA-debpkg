<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# debpkg Changelog

## [Unreleased]
### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security
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
