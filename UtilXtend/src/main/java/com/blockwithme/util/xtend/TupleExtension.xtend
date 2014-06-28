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
 * TODO @Data *uses Reflection* to generate the toString() method?!?
 *
 * @author monster
 */
class TupleExtension extends MathExtension {
	@Data static class TB { boolean p0 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TB($1)")
	def static $(boolean p0) { new TB(p0) }

	@Data static class TD { double p0 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TD($1)")
	def static $(double p0) { new TD(p0) }

	@Data static class TO<E0> { E0 p0 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TO<E0>($1)")
	def static <E0> $(E0 p0) { new TO<E0>(p0) }

	@Data static class TBB { boolean p0; boolean p1 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBB($1,$2)")
	def static $(boolean p0, boolean p1) { new TBB(p0, p1) }

	@Data static class TDB { double p0; boolean p1 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDB($1,$2)")
	def static $(double p0, boolean p1) { new TDB(p0, p1) }

	@Data static class TOB<E0> { E0 p0; boolean p1 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TOB<E0>($1,$2)")
	def static <E0> $(E0 p0, boolean p1) { new TOB<E0>(p0, p1) }

	@Data static class TBD { boolean p0; double p1 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBD($1,$2)")
	def static $(boolean p0, double p1) { new TBD(p0,p1) }

	@Data static class TDD { double p0; double p1 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDD($1,$2)")
	def static $(double p0, double p1) { new TDD(p0,p1) }

	@Data static class TOD<E0> { E0 p0; double p1 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TOD<E0>($1,$2)")
	def static <E0> $(E0 p0, double p1) { new TOD<E0>(p0,p1) }

	@Data static class TBO<E1> { boolean p0; E1 p1 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBO($1,$2)")
	def static <E1> $(boolean p0, E1 p1) { new TBO<E1>(p0,p1) }

	@Data static class TDO<E1> { double p0; E1 p1 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDO($1,$2)")
	def static <E1> $(double p0, E1 p1) { new TDO<E1>(p0,p1) }

	@Data static class TOO<E0,E1> { E0 p0; E1 p1 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TOO<E0,E1>($1,$2)")
	def static <E0,E1> $(E0 p0, E1 p1) { new TOO<E0,E1>(p0,p1) }

	@Data static class TBBB { boolean p0; boolean p1; boolean p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBBB($1,$2,$3)")
	def static $(boolean p0, boolean p1, boolean p2) { new TBBB(p0, p1, p2) }

	@Data static class TDBB { double p0; boolean p1; boolean p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDBB($1,$2,$3)")
	def static $(double p0, boolean p1, boolean p2) { new TDBB(p0, p1, p2) }

	@Data static class TOBB<E0> { E0 p0; boolean p1; boolean p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TOBB<E0>($1,$2,$3)")
	def static <E0> $(E0 p0, boolean p1, boolean p2) { new TOBB<E0>(p0, p1, p2) }

	@Data static class TBDB { boolean p0; double p1; boolean p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBDB($1,$2,$3)")
	def static $(boolean p0, double p1, boolean p2) { new TBDB(p0,p1, p2) }

	@Data static class TDDB { double p0; double p1; boolean p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDDB($1,$2,$3)")
	def static $(double p0, double p1, boolean p2) { new TDDB(p0,p1, p2) }

	@Data static class TODB<E0> { E0 p0; double p1; boolean p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TODB<E0>($1,$2,$3)")
	def static <E0> $(E0 p0, double p1, boolean p2) { new TODB<E0>(p0,p1, p2) }

	@Data static class TBOB<E1> { boolean p0; E1 p1; boolean p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBOB($1,$2,$3)")
	def static <E1> $(boolean p0, E1 p1, boolean p2) { new TBOB<E1>(p0,p1, p2) }

	@Data static class TDOB<E1> { double p0; E1 p1; boolean p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDOB($1,$2,$3)")
	def static <E1> $(double p0, E1 p1, boolean p2) { new TDOB<E1>(p0,p1, p2) }

	@Data static class TOOB<E0,E1> { E0 p0; E1 p1; boolean p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TOOB<E0,E1>($1,$2,$3)")
	def static <E0,E1> $(E0 p0, E1 p1, boolean p2) { new TOOB<E0,E1>(p0,p1, p2) }

	@Data static class TBBD { boolean p0; boolean p1; double p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBBD($1,$2,$3)")
	def static $(boolean p0, boolean p1, double p2) { new TBBD(p0, p1, p2) }

	@Data static class TDBD { double p0; boolean p1; double p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDBD($1,$2,$3)")
	def static $(double p0, boolean p1, double p2) { new TDBD(p0, p1, p2) }

	@Data static class TOBD<E0> { E0 p0; boolean p1; double p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TOBD<E0>($1,$2,$3)")
	def static <E0> $(E0 p0, boolean p1, double p2) { new TOBD<E0>(p0, p1, p2) }

	@Data static class TBDD { boolean p0; double p1; double p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBDD($1,$2,$3)")
	def static $(boolean p0, double p1, double p2) { new TBDD(p0,p1, p2) }

	@Data static class TDDD { double p0; double p1; double p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDDD($1,$2,$3)")
	def static $(double p0, double p1, double p2) { new TDDD(p0,p1, p2) }

	@Data static class TODD<E0> { E0 p0; double p1; double p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TODD<E0>($1,$2,$3)")
	def static <E0> $(E0 p0, double p1, double p2) { new TODD<E0>(p0,p1, p2) }

	@Data static class TBOD<E1> { boolean p0; E1 p1; double p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBOD($1,$2,$3)")
	def static <E1> $(boolean p0, E1 p1, double p2) { new TBOD<E1>(p0,p1, p2) }

	@Data static class TDOD<E1> { double p0; E1 p1; double p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDOD($1,$2,$3)")
	def static <E1> $(double p0, E1 p1, double p2) { new TDOD<E1>(p0,p1, p2) }

	@Data static class TOOD<E0,E1> { E0 p0; E1 p1; double p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TOOD<E0,E1>($1,$2,$3)")
	def static <E0,E1> $(E0 p0, E1 p1, double p2) { new TOOD<E0,E1>(p0,p1, p2) }

	@Data static class TBBO<E2> { boolean p0; boolean p1; E2 p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBBO<E2>($1,$2,$3)")
	def static <E2> $(boolean p0, boolean p1, E2 p2) { new TBBO<E2>(p0, p1, p2) }

	@Data static class TDBO<E2> { double p0; boolean p1; E2 p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDBO<E2>($1,$2,$3)")
	def static <E2> $(double p0, boolean p1, E2 p2) { new TDBO<E2>(p0, p1, p2) }

	@Data static class TOBO<E0,E2> { E0 p0; boolean p1; E2 p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TOBO<E0,E2>($1,$2,$3)")
	def static <E0,E2> $(E0 p0, boolean p1, E2 p2) { new TOBO<E0,E2>(p0, p1, p2) }

	@Data static class TBDO<E2> { boolean p0; double p1; E2 p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBDO<E2>($1,$2,$3)")
	def static <E2> $(boolean p0, double p1, E2 p2) { new TBDO<E2>(p0,p1, p2) }

	@Data static class TDDO<E2> { double p0; double p1; E2 p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDDO<E2>($1,$2,$3)")
	def static <E2> $(double p0, double p1, E2 p2) { new TDDO<E2>(p0,p1, p2) }

	@Data static class TODO<E0,E2> { E0 p0; double p1; E2 p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TODO<E0,E2>($1,$2,$3)")
	def static <E0,E2> $(E0 p0, double p1, E2 p2) { new TODO<E0,E2>(p0,p1, p2) }

	@Data static class TBOO<E1,E2> { boolean p0; E1 p1; E2 p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TBOO<E1,E2>($1,$2,$3)")
	def static <E1,E2> $(boolean p0, E1 p1, E2 p2) { new TBOO<E1,E2>(p0,p1, p2) }

	@Data static class TDOO<E1,E2> { double p0; E1 p1; E2 p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TDOO<E1,E2>($1,$2,$3)")
	def static <E1,E2> $(double p0, E1 p1, E2 p2) { new TDOO<E1,E2>(p0,p1, p2) }

	@Data static class TOOO<E0,E1,E2> { E0 p0; E1 p1; E2 p2 }

	@Pure @Inline("new com.blockwithme.util.xtend.TupleExtension.TOOO<E0,E1,E2>($1,$2,$3)")
	def static <E0,E1,E2> $(E0 p0, E1 p1, E2 p2) { new TOOO<E0,E1,E2>(p0,p1, p2) }
}