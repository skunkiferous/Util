/**
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
package com.blockwithme.util.xtend;

import com.google.common.base.Strings;

@SuppressWarnings("all")
public class IndentationAwareStringBuilder {
  private final StringBuilder builder = new StringBuilder();
  
  private int indentation = 0;
  
  private final static String indentationString = "  ";
  
  private final static String newLineString = "\n";
  
  public IndentationAwareStringBuilder increaseIndent() {
    this.indentation = (this.indentation + 1);
    return this;
  }
  
  public IndentationAwareStringBuilder decreaseIndent() {
    this.indentation = (this.indentation - 1);
    return this;
  }
  
  public IndentationAwareStringBuilder append(final CharSequence string) {
    if ((this.indentation > 0)) {
      String _repeat = Strings.repeat(IndentationAwareStringBuilder.indentationString, this.indentation);
      final String replacement = (IndentationAwareStringBuilder.newLineString + _repeat);
      String _string = string.toString();
      final String indented = _string.replace(IndentationAwareStringBuilder.newLineString, replacement);
      this.builder.append(indented);
    } else {
      this.builder.append(string);
    }
    return this;
  }
  
  public IndentationAwareStringBuilder newLine() {
    StringBuilder _append = this.builder.append(IndentationAwareStringBuilder.newLineString);
    String _repeat = Strings.repeat(IndentationAwareStringBuilder.indentationString, this.indentation);
    _append.append(_repeat);
    return this;
  }
  
  public String toString() {
    return this.builder.toString();
  }
}
