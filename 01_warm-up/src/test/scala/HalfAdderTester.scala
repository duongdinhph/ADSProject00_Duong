// Chisel Introduction
// Chair of Electronic Design Automation, TU Kaiserslautern
// File created on 18/10/2022 by M.Sc. Tobias Jauch (@tojauch)

package adder

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec


/** 
  * Half adder tester
  * Use the truth table from the exercise sheet to test all possible input combinations and the corresponding results exhaustively
  */
class HalfAdderTester extends AnyFlatSpec with ChiselScalatestTester {

  "HalfAdder" should "work" in {
    test(new HalfAdder) { dut =>
          /*dut.io.a.poke(...)
          dut.io.b.poke(...)
          dut.io.c.expect(...)
          dut.io.s.expect(...)

	  ...

	  */

        }
    } 
}

