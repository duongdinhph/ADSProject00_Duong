// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package readserial

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec


/** 
  *read serial tester
  */
class ShiftRegisterTester extends AnyFlatSpec with ChiselScalatestTester {

  "ShiftRegister" should "work" in {
    test(new ShiftRegister).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
        /*** check non-consecutive input ***/
        {
          val number = 100
          for(i <- (0 to 7).reverse){
            val bit = (number >> i) & 1
            dut.io.rxd.poke(bit.B)
            dut.clock.step(1)
          }
        }
        
        }
    } 
}

