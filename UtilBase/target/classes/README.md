UtilBase
========

This project contains the minimum amount of shared code required to bootstrap
the platform-specific parts of the Util project. It allows another project to
benefit of the portability provided by Util, without making itself dependent
on anything that is not compatibility-related. The compatibility spoken of,
here is between the standard JVM and GWT. If issues related to application
servers, or OSGi, or Android, or iOS, ... later arise, they would also be dealt
with here. Generally speaking, I want to keep this project as small as
possible, with the bulk of the code going into UtilShared.

Currently, we use at least one platform-independent "Facade" class called
SystemUtils, to allow access to platform-specific functionality in a platform
-independent way. This class is abstract, and must be implemented within the
platform-specific sub-project. Since every non-platform-specific class must be
compile with the GWT compiler, this requires a few small compromise here and
there. Using a Facade, which is an abstraction layer, causes a small overhead
in performance, when accessing platform-specific functionality, like the
current time.

To make sure that any shared code, which depends on platform-specific code,
works correctly, the class com.blockwithme.util.base.SystemUtils must be
initialized first with a working implementation using the method:

SystemUtils.setImplementation()

This method must be called in the "main()", so that SystemUtils is ready before
it is used the first time. For most platforms, except GWT/HTML5, an instance of

com.blockwithme.util.server.DefaultSystemUtilsImpl

can be used as-is. For GWT, use instead:

com.blockwithme.util.client.GWTSystemUtilsImpl

All projects are under the Apache License.
