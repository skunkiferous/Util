Util_client
===========

The GWT client part of Util. It contains mostly the platform-specific
implementation of SystemUtils, and possibly some GWT-specific helper code. I
currently plan to create all my GWT applications on top of libGDX, so this part
is left abstract, so it can depend on libGDX in the application itself, as it
saves me re-implementing functionality that already exists in there (like the
Reflection emulation).

All projects are under the Apache License.
