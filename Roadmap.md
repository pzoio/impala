# Impala Roadmap #

The following gives an outline of some of the features which are envisaged, post Impala 1.0.

### Documentation and Samples ###
  * **_Whistle and bells_ samples**. Impala really shines when dealing with large applications with complex configuration requirements. It would be great to have a more advanced sample application which demonstrates it's capabilities.
  * **More documentation**.

### Features ###
  * **Web administration tool** for visualising and administering modules at runtime.
  * **Module plugins.** Provide a packaging structure which will allow for chunks of reusable functionality to be packaged and deployed within a module, potentially using custom module type.

### Integration ###
  * **Scala support**, with built in support in Impala build for compiling Scala classes.
  * **Groovy support**, with a simple mechanism for picking up Groovy scripts and exposing these as part of module functionality.
  * **Integration with Grails. Either with Impala embedded within Grails, Grails embedded within Impala, or both.**

### IDE ###
  * **IDE integration** (IntelliJ, Netbeans). Currently, Eclipse is explicitly supported, for example, in the documentation, samples and scaffolding. It would be good to provide support for IntelliJ, Netbeans and potentially JDeveloper.
  * **Eclipse plugin for interactive test runner.
  ***Eclipse WST platform**support.**

### Build ###
  * **Better Maven build support**, allowing Maven users to build Impala applications using Maven, and to structure projects according to Maven document structure conventions.
  * **Maven archetypes**, for creating new modules.

### JMX ###
  * **Extensions to JMX administration capability**. Currently, JMX can be used for certain administration functions. The idea would be to extend this capability, and also improve on the range and sophistication of JMX deployment options
  * **Further extensions to the JMX support.**

### Deployment ###
  * **New deployment options, for example via Amazon's EC2 service.**

### Testing ###
  * **JUnit 4.0 support**. Specifically, the interactive test runner should be usable with JUnit 4.0-based tests.

### Web ###
  * **Further web framework integration**. Target additional web frameworks not covered in 1.0 release.

### OSGi ###
  * **Better OSGi support**, including support for web applications, easier OSGi integration testing, and better out the box OSGi deployment.
  * **Extensions to the Service registry API**, potenially allowing for OSGi compatibility when exporting and looking up service registry.