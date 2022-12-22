# Changelog
# 0.0.0
 * Integration of Triangle and Square into Octagon
# 0.0.1
 * Integration of Triangle and Square into Octagon was finished
# 0.0.2
 * Added Reminder support to Person plugin
 * many bug fixes
 * new domain class Reminder
 * added ReminderExecutor
 * octagon was dockerized
 * Added content and # links to whining mail message body
 * Added new column # to list of entities to identify row number
 * Title now contains entity class name and entity name
 * status new now can transits to in progress
 * default task status is now new and not unconfirmed
 * bug fix - after an entity was created or deleted, menu was not viewed (null was shown instead of menu)
 * added deprecated attribute to entity columns and in html web page is added a css style - line through and grey color
# 0.0.3
 * Octagon version is now shown at the bottom of page (footer)
 * Added new domain entity Favorite
 * Some refactoring and package renamings and clean up.
 * Implemented Octagon plugin system
# 0.0.4
 * Fixed bug in plugin system - questionRepository was missing in repositories
 * Migrated to Java 16
# 0.1.0
 * Migrated to Java 17
# 0.2.0
 * Added new feature entity autofill
 * Added new feature batch
 * Plugin api - backwards incompatible change : Plugin initial configuration is of type properties instead of previous json
# SNAPSHOT
 * DACO-321 : Added new entity User


