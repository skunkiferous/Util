Util-shared
===========

The common/shared part of the Util project. This is the largest part, as I try
to write portable code. There will be at least one platform-independent
"Facade" class called SystemUtils in there, to allow access to platform
-specific functionality in a platform-independent way. Basically, it starts
where GWT's JRE emulation ends. Every class in here must be compile with the
GWT compiler. This requires a few small compromise here and there.

For several of the classes to be usable, the class com.blockwithme.util.SystemUtils
must be initialized first with a working implementation using the method:

SystemUtils.setImplementation()

This method must be called in the "main()", so that SystemUtils is ready before
it is used the first time. For most platforms, except GWT/HTML5, an instance of

com.blockwithme.util.nongwt.DefaultSystemUtilsImpl

can be used as-is. For GWT, use instead:

com.blockwithme.util.gwt.GWTSystemUtilsImpl

which is abstract; the concrete implementation will be based on libGDX.

All projects are under the Apache License.
