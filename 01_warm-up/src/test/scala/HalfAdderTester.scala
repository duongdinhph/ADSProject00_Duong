// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

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
    test(new HalfAdder).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

          val testCases = Seq(
            (0, 0, 0, 0), // (a, b, expected_s, expected_co)
            (0, 1, 1, 0),
            (1, 1, 0, 1),
            (1, 0, 1, 0)
          )

          for ((a, b, expectedS, expectedCo) <- testCases) {
            dut.io.a.poke(a.U)
            dut.io.b.poke(b.U)
            dut.io.s.expect(expectedS.U)
            dut.io.co.expect(expectedCo.U)           

        }
    } 
  }
}

