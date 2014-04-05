UtilPrototypes
==============

This project contains some non-GWT-client "prototypes", which were too small
to justify independent projects.

Among other things, it contains an implementation of ...

Base40
======

* Useful for packing a readable small string in a Java long.
* The problem: How can I assign human-readable names/IDs to things, while not
  having to pay the overhead of a String object?
* The solution: Limit the character set of the characters of the name to the
  minimum, and pack the letters in a long. So, the first obvious possibility,
  is to use Base36, which is a well-known encoding. Unfortunately, Base-36
  makes poor use of the full range of a long. It allows for identifiers of
  length 13 (12 character can use the full character set range). So how do we
  make better use of the full range of the 64 bit longs? By growing the
  character set, until we cannot store 12 full-range characters in it anymore.
  That gives us an improved character set of 40 characters. That is a 1/9
  improvement. Since there is no unique set of 4 additional characters that
  will fit all use-cases, those are configurable.
* Base40 is packed in "server", instead of "shared", because JavaScript has got
  no love for "long", and so it makes little sense to use it on the client.

LongObjectCache
===============
JavaScript doesn't support primitive longs, so we may as well use a Map<Long,Object> there ...

Stringnum
=========

Stringnum is an evil performance hack. It allows you to subvert the String
hashcode to store a unique integer ID. It gives you something like dynamically
extensible enums.

Properties
==========

Some generic property-graph API.

All projects are under the Apache License.
