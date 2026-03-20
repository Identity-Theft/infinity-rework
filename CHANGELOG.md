# 1.3.0
### Added:
- When using Scale Per Level the max level of Infinity can be configured

### Changed:
- Allow Mending and Allow All Arrows are now off by default
- Scale Per Level is now on by default
- Switched to YetAnotherConfigLib for configs
- Replace dead homepage link with a link to Modrinth

### Fixed:
- If using a mod that removes max enchantment levels and Scale Per Level is off, levels above 3 would always use arrows
- **[1.20.1]** Use `#arrow` tag instead of comparing item itself
- **[1.20.1]** Arrow count sometimes desyncing