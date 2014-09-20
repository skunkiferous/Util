Util
====

This project contains, as the name implies, "utility" code;
common functionality, shared by most BlockWithMe projects.

Apart from serving as container for my own utility code, this project mainly
serves as an extended compatibility layer for non-JVM application (currently,
only GWT).

It is divided in several parts, to make it GWT-compatible.

1) The parent POM for the rest.

2) base: The base/bootstrap part of the code. Just enough to allow binding
   the platform-specific part, without making the shared code dependent on them.

3) shared: The common/shared part of the code. This is the largest part,
   as I try to write portable code. There will be at least one platform
   -independent "Facade" class called SystemUtils in there, to allow access to
   platform-specific functionality in a platform-independent way. Basically, it
   starts where GWT's JRE emulation ends. Every class in here must be compile
   with the GWT compiler. This requires a few small compromise here and there.

4) server: The non-GWT-client part. It is not in any way "server code",
   but just normal Java code, which is not GWT compatible. It is only called
   "server", because it is the GWT terminology for "non-client-code". In other
   words, it contains mostly a JRE-based implementation of UtilBase.

5) prototypes: Some "prototypes", which were too small to justify
   independent projects. Currently depends on server. If anything here is
   eventually put to use, it will be moved to shared.

6) client: The GWT client part. It contains mostly the platform-specific
   implementation of base, and possibly some GWT-specific helper code.
   I currently plan to create all my GWT applications on top of libGDX, so this
   part will depend on libGDX directly, as it saves me re-implementing
   functionality that already exists in there (like fake Reflection). This
   project is also large, because it contains many JRE elmulation classes, which
   are missing from GWT itself. Extending the JRE emulation as much as possible
   is required, to allow most of the Util code to be in shared.

7) wwtest: A test project to see that client and it's dependencies compile and
   can be used in a GWT application, since GWT only actually compiles code when
   it is used, and so you never know if your Java code is GWT compatible, unless
   you actually use it in a GWT application.

8) xtend: As I aim to use Xtend going forward for all my Java devellopments,
   I have put all the "util" Xtend code in this project.

9) jactor2-coreSt: I am also maintaining a single-threaded, GWT-compatible,
   implementation of the JActor2 project. SInce it depends on Util, it made
   most sense to keep it here.

To build everything, run "mvn install" (I'm paranoid about reproducibility,
so I personally always run "mvn clean install").

All projects are under the Apache License.
