Util
====

This project contains, as the name implies, "utility" code;
common functionality, shared by most BlockWithMe projects.

It is divided in 4 parts, to make it GWT-compatible.

1) Util_parent: Just a parent POM for the rest.

2) Util_shared: The common/shared part of the code. This is the largest part,
   as I try to write portable code. There will be at least one platform
   -independent "Facade" class called SystemUtils in there, to allow access to
   platform-specific functionality in a platform-independent way. Basically, it
   starts where GWT's JRE emulation ends. Every class in here must be compile
   with the GWT compiler. This requires a few small compromise here and there.

3) Util_server: The non-GWT-client part. It is not in any way "server code",
   but just normal Java code, which is not GWT compatible. It is only called
   "server", because it is the GWT terminology for "non-client-code". In other
   words, it contains mostly a JRE-based implementation of SystemUtils, but
   also some "prototypes", which were too small to justify independent projects.

4) Util_client: The GWT client part. It contains mostly the platform-specific
   implementation of SystemUtils, and possibly some GWT-specific helper code.
   I currently plan to create all my GWT applications on top of libGDX, so this
   part will depend on libGDX directly, as it saves me re-implementing
   functionality that already exists in there (like fake Reflection).

Go in Util_parent, to build everything.

All projects are under the Apache License.
