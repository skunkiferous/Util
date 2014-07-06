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

/**
 * Xtend Extension related to tuples values.
 *
 * The extension methods must be defined in Java, to be able to use @Inline,
 * but the @Data must be declared in Xtend, therefore it is split in two.
 *
 * @author monster
 */
@Data class TB { boolean p0 ; override toString() { "("+p0+")" } }

@Data class TD { double p0 ; override toString() { "("+p0+")" } }

@Data class TO<E0> { E0 p0 ; override toString() { "("+p0+")" } }

@Data class TBB { boolean p0; boolean p1 ; override toString() { "("+p0+","+p1+")" } }

@Data class TDB { double p0; boolean p1 ; override toString() { "("+p0+","+p1+")" } }

@Data class TOB<E0> { E0 p0; boolean p1 ; override toString() { "("+p0+","+p1+")" } }

@Data class TBD { boolean p0; double p1 ; override toString() { "("+p0+","+p1+")" } }

@Data class TDD { double p0; double p1 ; override toString() { "("+p0+","+p1+")" } }

@Data class TOD<E0> { E0 p0; double p1 ; override toString() { "("+p0+","+p1+")" } }

@Data class TBO<E1> { boolean p0; E1 p1 ; override toString() { "("+p0+","+p1+")" } }

@Data class TDO<E1> { double p0; E1 p1 ; override toString() { "("+p0+","+p1+")" } }

@Data class TOO<E0,E1> { E0 p0; E1 p1 ; override toString() { "("+p0+","+p1+")" } }

@Data class TBBB { boolean p0; boolean p1; boolean p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TDBB { double p0; boolean p1; boolean p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TOBB<E0> { E0 p0; boolean p1; boolean p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TBDB { boolean p0; double p1; boolean p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TDDB { double p0; double p1; boolean p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TODB<E0> { E0 p0; double p1; boolean p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TBOB<E1> { boolean p0; E1 p1; boolean p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TDOB<E1> { double p0; E1 p1; boolean p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TOOB<E0,E1> { E0 p0; E1 p1; boolean p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TBBD { boolean p0; boolean p1; double p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TDBD { double p0; boolean p1; double p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TOBD<E0> { E0 p0; boolean p1; double p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TBDD { boolean p0; double p1; double p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TDDD { double p0; double p1; double p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TODD<E0> { E0 p0; double p1; double p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TBOD<E1> { boolean p0; E1 p1; double p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TDOD<E1> { double p0; E1 p1; double p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TOOD<E0,E1> { E0 p0; E1 p1; double p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TBBO<E2> { boolean p0; boolean p1; E2 p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TDBO<E2> { double p0; boolean p1; E2 p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TOBO<E0,E2> { E0 p0; boolean p1; E2 p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TBDO<E2> { boolean p0; double p1; E2 p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TDDO<E2> { double p0; double p1; E2 p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TODO<E0,E2> { E0 p0; double p1; E2 p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TBOO<E1,E2> { boolean p0; E1 p1; E2 p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TDOO<E1,E2> { double p0; E1 p1; E2 p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }

@Data class TOOO<E0,E1,E2> { E0 p0; E1 p1; E2 p2 ; override toString() { "("+p0+","+p1+","+p2+")" } }
