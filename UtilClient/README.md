UtilClient
===========

The GWT client part of Util. It contains mostly the platform-specific
implementation of SystemUtils, and possibly some GWT-specific helper code. I
currently plan to create all my GWT applications on top of libGDX, so this part
depends on libGDX.

This project contains JRE emulation code for GWT pertaining to the following (ASL) projects:

GWTX: http://code.google.com/p/gwtx/
reactive4java: https://code.google.com/p/reactive4java/
totsp-emu: http://gc.codehum.com/p/totsp-emu/
libGDX: https://github.com/libgdx/libgdx/
Apache Harmony: http://harmony.apache.org/

And then some original code too.

All projects are under the Apache License.
