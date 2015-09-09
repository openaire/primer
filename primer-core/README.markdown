icm-iis-primer
==============

The aim of this project is to provide a reusability method for Oozie workflows, 
scripts and other file types by utilising import links.

The Primer traverses a specified Java package, looking for import links. When 
found, it resolves them by creating new elements in the containing directory,
in accordance with the import link. The primed package is stored in a new 
directory.

## Import links ##
Import links are defined as files following the specified format. They are 
always named `import.txt`.

### Exemplary import link

    ## This is a classpath-based import file (this header is required)
    from_jar classpath pl/edu/icm/coansys
    workflow coansys pl.edu.icm.coansys.citations:inner-workflow:1.2-SNAPSHOT

The file starts with a compulsory header. Then, every line consists of the 
following fields separated by whitespaces:

 - destination - the name that will be given to the imported element
 - import type - the type of imported element
 - imported element description - additional information indentifying the 
   imported element (import type-specific)

The import file presented above will create a directory `from_jar` and 
copy all the contents of `pl/edu/icm/coansys` into it. Additionally, it will 
unpack `inner-workflow` into `workflow` directory. 

### Supported import types ###

#### Classpath element ####
The element will be imported from the classpath (it can be either a file or 
directory).

 - **Import type:** `classpath`
 - **Element description:** fully qualified resource name, using forward slashes 
   as separators.

#### CoAnSys Oozie Workflow Package ####
A CoAnSys Oozie Workflow Package will be imported and unpacked.

 - **Import type:** `coansys`
 - **Element description:** Maven-style package description, i.e. 
   `groupId:artifactId:version`

## Execution ##
Primer is executed from a Maven plugin implemented in 
`icm-iis-primer-maven-plugin` project. Plugin usage is presented in 
`icm-iis-primer-example` project.

## For developer ##
The main element of Primer is `Loader` class. It is a close relative of 
Java `ClassLoader`. It should be provided with `ClassProviders`, which tell 
where it should look for resources. By default, `JarClassProvider` and 
`FileSystemClassProvider` are supplied. `Loader.prime` method executes the 
priming process.

Another important class is `Resolver`. It is used when resolving import links.
It groups various `ResolvingServices`. Each import type should be assigned a
`ResolvingService`. Currently implemented `ResolvingServices` are 
`ClasspathResolvingService` and `CoansysResolvingService`. 
