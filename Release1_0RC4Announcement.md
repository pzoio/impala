I am pleased to announce the release of Impala 1.0 RC4.

Impala 1.0 RC4 was originally supposed to be the 1.0 final release. However, due to a few more than expected changes in this release, it was decided to have one last release candidate prior to 1.0 final.
The main changes in this release are:
  * simplifications and enhancements in the mechanism for mapping web requests to modules, allowing for more intuitive arrangements of JSPs and resources within modules. See the documentation on [how to slice a multi-module web application](HowToSliceWebApplication.md), and [how to configure within-module JSPs](HowToConfigureJSPs.md).
  * modification of the starter application to use the multi-web module mechanism out of the box, as this is the recommended approach for building Impala web applications.
  * the addition of Javadocs to the build artifacts (this is now required for artifacts deployed to the Maven central repository via Sonatype's infrastructure).

See the full list of [issues covered in this release](http://code.google.com/p/impala/issues/list?can=7&q=label%3AMilestone-Release1.0RC4).

With Impala 1.0 RC4 released artifacts are also available in the Maven central repository. See http://repo1.maven.org/maven2/org/impalaframework/.

If you like Impala and would like to support the project, please take a look at this page: http://code.google.com/p/impala/wiki/GetInvolved.