I am pleased to announce the 1.0.2 release of Impala.
This is primarily a bug fix release, but also contains some minor as well as more significant feature enhancements.

## Optional modules ##

It is now possible to express dependencies on optional modules. Module B may declare an optional dependency on module A, which means that if A is present, it's classes, resources and beans will be visible to module B. However, if A is not loaded, the application will not fail.

This feature makes it possible to allow optional functionality to be included in an application.

## Module-specific library jars ##

This feature, described an earlier blog entry, allows a module to use third party library classes which are specific to that module. In other words, it allows a module to load it's own version of a particular third party library class, potentially one which is different from or conflicts with the class visible to other modules in the application.

In addition, this release covers a number of other issues.
See the full list of [issues covered in this release](http://code.google.com/p/impala/issues/list?can=7&q=label%3AMilestone-Release1.0.2).

With Impala 1.0.2 released artifacts are also available in the Maven central repository. See http://repo1.maven.org/maven2/org/impalaframework/.

If you like Impala and would like to support the project, please take a look at this page: http://code.google.com/p/impala/wiki/GetInvolved.