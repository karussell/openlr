This repository aims to be a 1:1 clone of the downloadable sources from http://www.openlr.org/download.html

The license is accordingly: Apache License 2.0

With this repository the classes are better explorable

Javadocs are [here](http://www.openlr.org/maven/apidocs/)

Related project is [openlr.js](https://github.com/tomtom-international/openlr-js)

Include the library via Maven:

```xml
 <dependency>
    <groupId>openlr</groupId>
    <artifactId>xml</artifactId>
    <version>1.4.2</version>
 </dependency>
```

But you need a repository:

```xml
 <repository>
    <id>openlr</id>
    <url>http://www.openlr.org/nexus/content/repositories/releases</url>
 </repository>
```

The Java usage is unclear as the following does not really make sense but
other methods are not available:

```java
 // how can we fetch all the entries?
 OpenLRXmlReader reader = new OpenLRXmlReader();
 OpenLR olr = reader.readOpenLRXML(new File("somefile.xml", true));
 olr.getBinaryLocationReferences()
```
