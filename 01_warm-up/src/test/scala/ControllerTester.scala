// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package readserial

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import chisel3.experimental.ChiselEnum


/** 
  *read serial tester
  */
class ControllerTester extends AnyFlatSpec with ChiselScalatestTester {

  "Controller" should "work" in {
    test(new Controller).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
        /*** check non-consecutive input ***/
        {
            dut.io.reset_n.poke(1.B)
            dut.io.cnt_s.poke(0.B)
            dut.io.rxd.poke(1.B)
            dut.clock.step(1)
            dut.clock.step(1)
            dut.io.reset_n.poke(0.B)
            dut.clock.step(1)
            dut.clock.step(1)
            dut.io.rxd.poke(0.B)
            dut.clock.step(1)
            dut.clock.step(1)
            dut.clock.step(1)
            dut.clock.step(1)
            dut.clock.step(1)
            dut.clock.step(1)

            dut.io.cnt_s.poke(1.B)
            dut.clock.step(1)
            dut.io.cnt_s.poke(0.B)
            dut.io.rxd.poke(1.B)
            dut.clock.step(1)
            dut.clock.step(1)
          }
        }
        }
}

