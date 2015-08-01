The following is the set of release steps which need to be followed as part of release.

## In Eclipse ##

### Get samples up to date ###

Run `ant tohome`, then `ant fetch` from relevant projects. This results in the latest Impala snapshot being populated. Check-in the updated Jar files.

### Run Eclipse tests ###

Run `AllTests` for Impala, Example and Petclinic applications, as JUnit test.
Run `EntryDAOTest` as JUnit test.
Run `InProjectEntryDAOTest` as JUnit test.

### Run Interactive tests in Eclipse ###

Run `EntryDAOTest` as main class:

```
test (should print OK)
reload (all application classes should reload, all modules should reload)
reload dao (module dao classes should reload, module dao should reload)
sc (should be given selection. Select number corresponding with `EntryServiceTest`)
test (should get output)
reload (as above with `EntryDAOTest`, but should include example-service module)
module dao(as above with `EntryDAOTest`, but should reload example-service module)
```

Run `InProjectEntryDAOTest` as main class:

```
test (should print OK)
reload (all application classes should reload, all modules should reload)
reload dao (module dao classes should reload, module dao should reload)
```

Run `ExampleServiceTest` as main class:

```
test (should print OK)
reload (as above)
reload service (as above) 
cd (which will bring up a prompt, then select:)
example
sc (which will prompt for a class to select)
test (which should bring up a few, including `AlternativeEntryServiceTest` - select this one)
test (should print OK)
reload service
reload
test
```

### Run up web apps in Eclipse ###

Run `StartServer` from _example-web/src_. Verify using the following urls:
  * http://localhost:8080/example-web/

Go through all the links on this page. Reload one of the modules and do the same again.


## In ANT ##

### Basic build and test ###

Run `ant fixcrlf` to fix any carriage return or line feed issues. Then check in these changes.

Run `ant` for Impala

```
cd /localDisk/workspaces/impala/impala
ant 
ant test
```

Copy release files to Impala home. Note that this target is assuming that the `IMPALA_HOME` environment variable has been set.

```
ant tohome
```

**Warning**: do not run this command if your IMPALA\_HOME is set to the impala directory within the Impala source code workspace, as this could result in this directory being deleted and any outgoing changes being lost. Instead, set IMPALA\_HOME to a staging directory where the Impala snapshot distribution can be build to. For example:

  * c:\impala-1.0-SNAPSHOT on Windows
  * /home/username/impala-1.0-SNAPSHOT on MacOSX or Linux

### Deploy and sanity check Petclinic ###

Run `ant fetch clean dist test` for Petclinic. Assuming location of ../petclinic relative to _impala_.

```
cd ../petclinic-build
ant fetch clean dist test
ant war tomcat
```

Note that the _tomcat.home_ property`tomcat` target will need to be set correctly. Run tomcat

Assumes the TOMCAT\_HOME environment variable is set:

```
cd $TOMCAT_HOME
./catalina.sh run
```

Check application from
  * http://localhost:8080/petclinic-host/

### Deploy and sanity check Example sample ###

Run `ant fetch clean dist test` for Example.

```
cd ../example-sample/example-build
ant fetch clean dist test
ant war tomcat
```

Run Tomcat as in Petclinic

Check the following urls:
  * http://localhost:8080/example-host/test.do
  * http://localhost:8080/example-host/servlet1/test.do1
  * http://localhost:8080/example-host/servlet2/test.to

## Scaffold steps ##

Run through steps of scaffold build as [the getting started tutorial, part one](GettingStartedPart1.md).

## Publish release ##

Any errors encountered in the previous step should be recorded. If serious, the next step should not proceed.

  * Check _next.release.version_ is correct in _release-build.properties_.
  * Run from from the main _impala_ project:

```
ant release
```

This will tag the SVN repository with the release name, and create a distribution file.
Upload _dist/impala-%RELEASE\_VERSION%.zip_ to [Google Code downloads](http://code.google.com/p/impala/downloads/list).

## Sanity Check ##

Download the release. Check that the SHA1 checksum matches that of the pre-upload distribution file, and that published on the download metadata page.

For example, run the command

```
openssl sha1 impala-%RELEASE_VERSION%.zip
```

See http://www.bresink.de/osx/sha1.html for an example on how to do this.

Go through scaffold steps as defined in FirstSteps.

## Maven steps ##

From Impala home, run

```
ant -Dmaven.release=true -Dproject.version=%RELEASE_VERSION% mvnsigndeploy
```

This will upload the release files to the maven staging repository from the local maven repository, as per _mvn-build.properties_:

```
maven.repo.root=../maven/repo
maven.snapshot.repository=http://oss.sonatype.org/content/repositories/impalaframework-snapshots
maven.release.repository=http://oss.sonatype.org/service/local/staging/deploy/maven2
```

Note that releases are deployed to Sonatype's staging directory.
They need to promoted as described here: http://nexus.sonatype.org/oss-repository-hosting.html and
http://www.jroller.com/holy/entry/releasing_a_project_to_maven.

The Nexus interface is here: http://oss.sonatype.org/index.html

## Documentation Updates ##

Now is a good time to make sure that documentation is up to date for the new release.
Prepare any necessary release documentation.

## Announcements ##

Make new release the 'Featured' download, so that it appears on the project home page.
Remove the 'Featured' tag from the previous release.

TODO: Add list of sites/mailing lists to add announcement.