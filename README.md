# KSP-IPH-2019-20
1. 
Motivation: Why do you think this problem is worth solving? What are the foreseeable outcomes? (75 words) *

Unified Communication App (UCA) is a same problem solved by GSMA group develop Rich
communication Suite (RCS) specification majorly adopts 3GPP and IETF telecom specifications.
RCS is a much awaited feature over LTE network from operators after succesffull deployment of Voice over LTE (VoLTE). Google Allo is one example of
RCS application.
Working for these great use cases and experiencing such standard bodies specifications is a motive in choosing the problem statement.

Explain how you would build your product within the 36 hours and explain in detail the architecture and tools you are likely to use (50 words) *

UCA/ RCS features needs 3GPP/IETF based IP Multimedia Subsystem (IMS) as core network for registration for services.
Presence, Instant messiaging, VoIP/ media servers are application
servers IMS offers. We are planning to use Kamailio open source servers to get these services up.
Another option is to use XMPP Open source platform to incorporate RCS requirments (specially for multidevice requirements including platform).
Hope our guide can also help us in decision.

Please list possible Input test cases and the Output you expect (50 words) *

1. Login, Logout and Presence features (Online, away, offline)
2. Instant messaging (IM) (Online and offline)
3. File Transfer (IETF RFC MSRP based or similar)
4. Group creations, management (OMA based XDM servers or similar)
4.a) Group messaging
4.b) Converged address book
5. VoIP (AMR-NB/WB, RTP/RTCP protocol, WebRTC ): if time permits
6. Video call (H264, 25FPS, VGA or higher, Wifi based): if time permits

Depends on the open source we choose these scenarios can be demoed using UI based apps or DOS command shell.

. Explain your choice of the dataset for experimentation: Why do you think the data you are planning to use is relevant and sufficient?
Please cite the source if the data is public or argue for a choice of synthetic data generation. (75 words) [Please mention "not required"
if no dataset is needed ] *
not required

Parsimony Sourcing: Are you going to use large heterogeneous virtual computing power (Colab/AWS/G-Cloud etc)? If Yes, why? (30 words) *
To start with we are not using any cloud infra. FCM may be used for to notify features.
As we are choosing IP based servers they can be hosted on any of the load balancing frameworks.
Also we can port own IP Stack to embedd IPSec feature in it. On top, we can adhoc transport layer ports algorithms.


Please list the Laptop/Desktop/HW configuration you're planning to use. (50 words) *
General purpose linux and windows machines to build servers and apps.


Write down the Synopsis of the Problem Statement selected.
This project is about defining a endpoint to connect devices/ people from different types of network (ex: mobile nw, broadband).
Also converging people from different eco systems/platform (iOS, Android) to take part in session or no-session conversation.
With centralized, always ON and private Data centers.


2. List of Tools=> java 1.8 , andriod studio(https://developer.android.com/studio/), Maven(https://maven.apache.org/download.cgi).
    Inteljii idea (https://www.jetbrains.com/idea/)

3.open android studio and open unified client, build gradle,take apk from build-->generate apk and instal in mobile.

4. import open-fire project in inteljii and flow below step for setup

Making changes
==============
The project uses [Maven](https://maven.apache.org/) and as such should import straight in to your favourite Java IDE.
The directory structure is fairly straightforward. The code is contained in two key folders:

* `Openfire/xmppserver` - a Maven module representing the core code for Openfire itself
* `Openfire/plugins` - a number of modules for the various [plugins](https://www.igniterealtime.org/projects/openfire/plugins.jsp) available

Other folders are:  
* `Openfire/build` - various files use to create installers for different platforms
* `Openfire/distribution` - a Maven module used to bring all the parts together
* `Openfire/documentation` - the documentation hosted at [igniterealtime.org](https://www.igniterealtime.org/projects/openfire/documentation.jsp)
* `Openfire/i18n` - files used for internationalisation of the admin interface
* `Openfire/starter` - a small module that allows Openfire to start in a consistent manner on different platforms

To build the complete project including plugins, run the command
```
mvn verify
```  

However much of the time it is only necessary to make changes to the core XMPP server itself in which case the command
```
mvn verify -pl distribution -am 
```  
will compile the core server and any dependencies, and then assemble it in to something that can be run. 

Testing your changes
--------------------

#### IntelliJ IDEA:

1. Run -> Edit Configurations... -> Add Application
2. fill in following values
    1. Name: Openfire
    2. Use classpath of module: starter
    3. Main class: org.jivesoftware.openfire.starter.ServerStarter
    4. VM options (adapt accordingly):
        ````
        -DopenfireHome="-absolute path to your project folder-\distribution\target\distribution-base" 
        -Xverify:none
        -server
        -Dlog4j.configurationFile="-absolute path to your project folder-\distribution\target\distribution-base\lib\log4j2.xml"
        -Dopenfire.lib.dir="-absolute path to your project folder-\distribution\target\distribution-base\lib"
        -Dfile.encoding=UTF-8
       ````
   5. Working directory: -absolute path to your project folder-
3. apply

You need to execute `mvn verify` before you can launch openfire.

#### Other IDE's:

Although your IDE will happily compile the project, unfortunately it's not possible to run Openfire from within the 
IDE - it must be done at the command line. After building the project using Maven, simply run the shell script or 
batch file to start Openfire;
```
./distribution/target/distribution-base/bin/openfire.sh
```
or
```
.\distribution\target\distribution-base\bin\openfire.bat
```

Adding `-debug` as the first parameter to the script will start the server in debug mode, and your IDE should be able
to attach a remote debugger if necessary.

Compiling a plugin
------------------
Compiling the complete project will build all the plugins - however to test changes to a plugin it's often quicker to 
compile an individual plugin by specifing the `pom.xml` for that plugin, for example;
```
mvn verify -f plugins/broadcast/pom.xml



