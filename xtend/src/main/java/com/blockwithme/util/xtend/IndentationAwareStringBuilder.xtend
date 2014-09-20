/*
 * Copyright (C) 2014 Sebastien Diot.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blockwithme.util.xtend

import static extension com.google.common.base.Strings.*

class IndentationAwareStringBuilder {
		val builder = new StringBuilder()
		var indentation = 0
		static val indentationString = "  "
		static val newLineString = "\n"

		def increaseIndent() {
			indentation = indentation + 1
			return this
		}
		def decreaseIndent() {
			indentation = indentation - 1
			return this
		}
		def append(CharSequence string) {
			if (indentation>0) {
				val replacement = newLineString + indentationString.repeat(indentation)
				val indented = string.toString().replace(newLineString, replacement)
				builder.append(indented);
			} else {
				builder.append(string)
			}
			return this
		}
		def newLine() {
			builder.append(newLineString).append(indentationString.repeat(indentation))
			return this
		}
		override toString() {
			return builder.toString()
		}
	}
