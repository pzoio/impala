I am pleased to announce the 1.0 final release of Impala.
The aim of Impala is to extract every last ounce of productivity and flexibility out of your Spring development environment,
while at the same time remaining true to the principles that made Spring popular in the first place - simplicity and testability.

Impala offers instantaneous redeployment of parts of your application, dramatically improving build/deploy/test cycle times.
Impala supports on the fly 'drop-in' of new application functionality - including web modules - without requiring an application restart and without
requiring any changes to existing application code or configuration.

With Impala you can achieve the productivity benefits of a modular
approach to application development. When using Impala, you no longer need to work with application contexts which contain hundreds of bean definitions.
Instead, you break your application down into smaller, manageable chunks.
You no longer need to put up with the ever-increasing complexity and tangled interdependencies which come with
a monolithic approach to application development. Instead, you develop your application in a loosely coupled way, with
a clean distinction between application's interfaces and implementation components.

With Impala it is much easier to mix and match features for particular environments, which is particular useful
for applications which may need to be deployed with different features and configurations on a per-environment basis.
Writing integration tests with Impala is a doddle, because setting these up is simply a matter of selecting the modules you want to
include in your tests.

Impala works with all the technologies you would expect in the Spring world - Hibernate, Quartz, JMX, etc. - without requiring any
special plugins to be written for those technologies. It also supports - in most cases without modification -
web applications using a variety of web frameworks, from Struts to JSF to Tapestry and others. A lot of work has been put into making Impala
elegantly support modular web applications, allowing for modular vertical web application 'slices', rather than just a modular back end.

Impala works straight out of the box using a plain Eclipse installation (no fancy plugins required). It has a built-in build system which you can optionally
use, based on Ant, and also integrates nicely with Maven.

With Impala, you can take your Spring-based application development to a new
level of ease and sophistication, with remarkably few changes to the way you write applications.

See the full list of [issues covered in this release](http://code.google.com/p/impala/issues/list?can=7&q=label%3AMilestone-Release1.0).

With Impala 1.0 released artifacts are also available in the Maven central repository. See http://repo1.maven.org/maven2/org/impalaframework/.

If you like Impala and would like to support the project, please take a look at this page: http://code.google.com/p/impala/wiki/GetInvolved.