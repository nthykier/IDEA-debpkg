<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# debpkg Changelog

## [Unreleased]
### Added
- Add check to highlight if fields are placed in the wrong paragraph in debian/control files. (Closes: #1)
- Add check to ensure that mandatory "Source" and "Package" fields are present in debian/control.
- Add documentation and field-placement level validation for remaining known fields (e.g. Depends).  (Closes: #2)
- Detect the documentation pattern " * FILENAME: CHANGE" in changelogs and automatically link to the file if
  it can be found (globs not supported).
- Parse dependency fields with a different parser enabling basic format checking of the dependency fields.
- Check for invalid version operators dependency fields.
- Add check for some substvars used unconventionally in dependency fields (e.g. ${misc:Depends} used as a version).
- Add support for folding long field values with "Description" in debian/control being folded by default.

### Changed
- Code completition is now more context-aware inside dependency fields.

### Fixed
- Fix misplaced "Package"/"Source" field warning not coping with moving paragraphs around.

### Security
## [0.0.2]
### Added
- Add new icons instead of using the default icon from the template.
- Check for misplaced "Package" and "Source" fields.


## [0.0.1]
### Initial version

- Syntax highlighting of debian/changelog and *.dch files
- Syntax highlighting of debian/control and *.deb822 files
- Code completion support for debian/control
- Hover documentation of most fields, field keywords and standard substitution variables.
- Basic but very incomplete detection of invalid values used in field for debian/control.
