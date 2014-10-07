A Mobile Cloud Application Template
====================

This project contains a template for a mobile cloud application that has a Spring Boot server, 
an Android client, and shared client/server code. The template includes appropriate testing for 
each part of the mobile cloud application.

Project Anatomy
---------------

The project template contains 3 sub-projects:

  - _Client_: contains a Gradle-based Android Studio app
  - _Server_: contains a Spring Boot REST-based back-end for the mobile app
  - _Common_: contains a Gradle-based Java project that is shared by both the Client and Server 
              projects and automatically included in both builds

Recommended Development Tools
------------------------------
1. Git (command line)
2. Android Studio
2. Eclipse
3. Genymotion Android Emulator (http://www.genymotion.com/)

Install all of the above software. After installing the software, make sure that you:

1. Open Android Studio, select the "SDK Manager" (right-hand side of the top menu bar) and 
   download the Android 4.3 and 4.4 SDK tools
2. Open Genymotion, click Add, and then select an emulator, such as a Nexus 5 device.
3. Select the emulator in Genymotion and hit play. If you see an error, read this link
   to resolve it (and call me over to get help)   
   http://stackoverflow.com/questions/19922077/genymotion-unable-to-start-the-genymotion-virtual-device

The Client Project
-------------------

###Setup

  1. Download and install the latest version of Android Studio
  2. Clone this repository
  3. In Android Studio, File->Import Project
  4. Select the Client directory and then OK
  5. Click the play button to launch the application in the Android emulator
  
The client contains a basic app for viewing a list of metadata for videos stored on the server. The
client uses the Square Retrofit library (http://square.github.io/retrofit/) to send HTTP requests to
the server to interact with the REST resources. Currently, the client simply displays a list of the
videos that are stored on the server. 

###Code Overview

The client has two activities: LoginScreenActivity and VideoListActivity. The LoginScreenActivity
accepts a username and password (which aren't used in this example) and a server address. When
the user logs in with this information, the LoginScreenActivity attempts to retrieve a list of 
videos from the server over HTTP. If the videos are successfully retrieved, the LoginScreenActivity
opens the VideoListActivity.

The VideoListActivity uses a simple ListView and an ArrayAdapter of Strings to display the names
of the videos that are retrieved from the server. This class shows one of the simplest possible
list-based screens that you can build that fetches data from a server. 

The VideoSvc class is responsible for creating an HTTP client that can interact with the server
to create, read, update, and delete (CRUD) videos. The VideoSvc is built on top of Retrofit.
Retrofit is a library that allows you to create objects that automatically translate method calls
into HTTP requests on a remote server. Retrofit provides a strong type-safe interface to HTTP-based
interactions and vastly simplifies client-server interaction over HTTP. To use Retrofit, you first
create a Java interface describing the server-side services that you want to interact with:

```java

public interface VideoSvcApi {
	
	@GET("/video")
	public Collection<Video> getVideoList();

}

```
This interface is a standard Java interface that has a single method that you expect to be supported by
the server to retrieve the list of videos. The method is annotated with the "@GET" annotation, which 
tells Retrofit that calls to this method should be translated into HTTP GET request to the "/video" path 
on the server. Retrofit dynamically creates objects that implement these types of interfaces and provides
all of the plumbing to translate your method calls to the interface's methods into HTTP requests to the
appropriate endpoints. Retrofit also handles marshalling Objects to/from JSON. In the example above,
Retrofit will attempt to automatically read the JSON that it receives from the server and unmarshall it
into a Collection of Video objects (Retrofit uses the same library that you used in the Git branching
exercise).

In order to use Retrofit, you have to instruct it to dynamically create an object that implements your
interface. You can create these dynamic object as shown below:

```java
VideoSvcApi svc = new RestAdapter.Builder()
				.setEndpoint("http://someserver.com").setLogLevel(LogLevel.FULL).build()
				.create(VideoSvcApi.class);
				
Collection<Video> videos = svc.getVideoList();
```
The RestAdapter.Builder() creates the dynamic object implementation of the VideoSvcApi and returns it.
Once you have one of these objects, you can call methods on it like any other Java object, as shown above
in the call to svc.getVideoList().

LoginScreenActivity and VideoListActivity both use the VideoSvc to construct a VideoSvcApi implementation 
using Retrofit and retrieve videos from the server. One important consideration on mobile devices is that
you do not want to do networking interactions, such as HTTP requests, on the thread that is rendering the
UI of an application. Since the calls to the methods on VideoSvcApi all result in HTTP requests being sent
over the network to a server, all of these method calls need to take place in background threads. 

The CallableTask class provides a simple interface for executing arbitrary work in a background thread and
then notifying a callback of the success or failure of the background work in the UI thread. The reason that
the CallableTask notifies the callback in the UI thread is that only the UI thread can make changes to the
user interface elements. Using the CallableTask, networking operations can be run in the background and the
results of those operations can be communicated to the user via changes to the GUI made by the callback
object.

###Testing

The Client template project is configured to use the Android JUnit-based testing framework. All of the test
code is contained within the androidTest folder. Each class in the client application has an associated
Android test. There are two different types of tests in the project:

1. _Standard TestCase_ classes are used to test code that does not depend on an Activity or other Android
   UI classes (these classes extend TestCase).

2. _ActivityInstrumentationTestCase2_ classes are used to test Activities.

Take a look at each type of test to understand how they work. All of these tests interact with the real server
and require that it is running before they are launched. Since these tests do not isolate small pieces of
functionality, they are actually _integration tests_. Normally, it is best to write small and fast unit tests
that isolate functionality and are simple to run. However, Android tests run so incredibly slowly, you might
as well do integration testing with the actual server as it adds very little extra time.

_How do I run a test?_ Android requires an emulator or real device in order to run tests. Start an emulator or
connect an Android device via USB. When you want to run one of the tests, right-click on it->Run XYZTest. Android
Studio will load the test into your emulator or device, execute it, and then display the results in the pane
at the bottom of Android Studio.

_How do I add a new test?_ First, determine which of the two types of tests listed above seems most appropriate.
After you decide on the right type of test, copy/paste one of the existing tests into same package (or another
one in the androidTest folder) and rename the test appropriately by right-clicking on it, Refactor->Rename.
You can then add / delete / modify the test as needed. Since Android does not use the latest version of JUnit,
you must name each of your test methods testXYZ. There are no @Test annotations. 

The Common Project
-------------------
###Setup

  1. Download and install the latest version of Eclipse or Intellij
  2. Import the Common project as a Gradle project

###Code Overview

The Common project contains classes that are shared by both the client and server. Rather than duplicating this 
shared code, the Common project is included in the builds for both of the other projects. Any code that you put
into the Common project will be available to the client and server. 

The Common project contains the "domain objects" or "model objects" that are used by the application. In this case,
a Video object is used to represent video metadata. The Video class has simple member variables for different types
of video metadata, such as the name and duration of the Video. The Video class also has special annotations that
are used by the Server and discussed below.

The VideoSvcApi is defined in the Common project. The interface contains the methods and Retrofit annotations that
are used to build the client object that communicates with the server. Finally, the TestUtils class provides some
helper methods for creating dummy Videos.

###Testing

There is little to test in this project, however, there are still tests included for each object. The project
uses JUnit and the OpenPojo framework for testing. The VideoTest checks that the custom code in the equals()
and hashcode() methods of the Video object works properly. 

A common question is whether or not it makes sense to test getters/setters on objects. Manually creating tests
of every getter/setter is time consuming. The PojoTest uses the OpenPojo framework to automatically discover every
Plain Old Java Object (POJO) class in your application and test it. A POJO is just an object that is essentially
all member variables and getters/setters (e.g., model objects). The PojoTest finds every one of these objects,
tests that their getters/setters work properly, and also runs a number of other code structure tests against them.
This tool saves you a vast amount of test implementation work while increasing your test coverage.

_How do I run a test?_ All you need to do is right-click on the test, Run As->JUnit Test.

_How do I add a test?_ Select the package that you want to add the test to (it must be beneath src/test) and
then File->New->JUnit Test Case. Add one or more methods annotated with @Test.




The gitingore file
-------------------
This repo is setup with a .gitignore file. This file automatically ignores all IDE settings, build artifacts, etc.
that shouldn't be committed to the source repository. See the .gitignore file in the root of the repository for
a list of the types of files and directories that are ignored.
