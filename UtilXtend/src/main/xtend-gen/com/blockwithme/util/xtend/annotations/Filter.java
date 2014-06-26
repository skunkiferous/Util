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
package com.blockwithme.util.xtend.annotations;

import com.blockwithme.util.xtend.annotations.ProcessorUtil;
import java.util.Map;
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration;

/**
 * Filter used to allow composition of "requirements" in the accept() method of a Processor
 */
@SuppressWarnings("all")
public interface Filter {
  public abstract boolean apply(final Map<String, Object> processingContext, final ProcessorUtil processorUtil, final TypeDeclaration typeDeclaration);
}
