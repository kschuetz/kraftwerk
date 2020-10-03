# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [Unreleased]
No changes yet

## [0.10.0] - 2020-10-02
### Added
- Add `String` generators: 
    - `generateAlphaString` 
    - `generateAlphaUpperString` 
    - `generateAlphaLowerString` 
    - `generateAlphanumericString`
- Add `Character` generators:
    - `generateAlphaChar`
    - `generateAlphaUpperChar`
    - `generateAlphaLowerChar`
    - `generateAlphanumericChar`
    - `generateNumericChar`
    - `generatePunctuationChar`
    - `generateAsciiPrintableChar`
    - `generateControlChar`
    
### Changed    
- Rename `buildCollection` to `generateCollection`
- Rename `product` to `generateProduct`
- Rename `tupled` to `generateTuple`
- `generateInfiniteIterable` now yields `ValueSupply`s 

### Removed
- Remove `buildVector` from public API

[Unreleased]: https://github.com/kschuetz/kraftwerk/compare/kraftwerk-0.10.0...HEAD
[0.10.0]: https://github.com/kschuetz/kraftwerk/commits/kraftwerk-0.10.0
