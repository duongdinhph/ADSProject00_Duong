// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package readserial

import chisel3._
import chisel3.util._
import chisel3.experimental.ChiselEnum

/** controller class */
class Controller extends Module{
  
  val io = IO(new Bundle {
    val reset_n = Input(UInt(1.W))
    val rxd = Input(UInt(1.W))
    val cnt_s = Input(UInt(1.W))
    val cnt_en = Output(UInt(1.W))
    val valid = Output(UInt(1.W))
    })
  object State extends ChiselEnum {
    val sReset, sWait, sStart, sFinish = Value
  }
  // init state
  val state = RegInit(State.sReset)
  //default output
  io.cnt_en :=  0.B 
  io.valid := 0.B
  // state machine transition

  switch(state){
    is(State.sReset){
      when(io.reset_n === 1.B){
        io.valid := 0.B
        io.cnt_en := 0.B
      } .elsewhen(io.rxd === 1.B){
        state := State.sWait
      } .elsewhen(io.rxd === 0.B){
        state := State.sStart
      }
    }

    is(State.sWait){
      when(io.reset_n === 1.B){
        io.valid := 0.B
        io.cnt_en := 0.B
        state := State.sReset
      } .elsewhen(io.rxd === 0.B){
        state := State.sStart
      }
    }

    is(State.sStart){
      when(io.reset_n === 1.B){
        io.valid := 0.B
        io.cnt_en := 0.B
        state := State.sReset
      } .elsewhen(io.cnt_s  === 1.B){
        state := State.sFinish
        io.cnt_en := 1.B
      } .otherwise{
        io.cnt_en := 1.B 
      }
    }

    is(State.sFinish){
      when(io.reset_n === 1.B){
        io.valid := 0.B
        io.cnt_en := 0.B
        state := State.sReset
      } .elsewhen(io.rxd === 0.B){
        io.valid := 1.B 
        io.cnt_en := 0.B 
        state := State.sStart
      } .elsewhen(io.rxd === 1.B){
        io.valid := 1.B
        io.cnt_en := 0.B 
        state := State.sWait
      }
    }
  }

}

/** counter class */
class Counter extends Module{
  
  val io = IO(new Bundle {
    val reset_n = Input(UInt(1.W))
    val cnt_en = Input(UInt(1.W))
    val cnt_s = Output(UInt(1.W))
    })
  object State extends ChiselEnum {
    val sReset, sIdle, sCount = Value
  }
  // set initial state
  val state = RegInit(State.sReset)
  val delay_when_finish = RegInit(0.U(1.W))
  // internal variables
  val cnt = RegInit(0.U(3.W))
  // default values for output
  io.cnt_s := 0.B
  // state machine transition
  switch(state) {
    is(State.sReset){
      cnt := 0.U
      io.cnt_s := 0.B
      when(io.reset_n === 0.B & io.cnt_en === 1.B){
        state := State.sCount
        cnt := cnt + 1.U
      } .elsewhen(io.reset_n === 0.B & io.cnt_en === 0.B){
        state := State.sIdle
      }
    }

    is(State.sIdle){
      when(io.reset_n === 1.B){
        state := State.sReset
        cnt := 0.U
        io.cnt_s := 0.B
      } .otherwise{
        when(io.cnt_en === 1.B){
          state := State.sCount
          cnt := cnt + 1.U
        }
      }
    }

    is(State.sCount){
      when(io.reset_n === 1.B){
        state := State.sReset
        cnt := 0.U
        io.cnt_s := 0.B 
      } .otherwise{
        when(cnt === 7.U){
          cnt := 0.U
          io.cnt_s := 1.B 
          state := State.sIdle
        } .elsewhen(io.cnt_en  === 0.B){
          state := State.sIdle
        } 
        .otherwise{
          cnt := cnt + 1.U 
        }
      }
    }
  }
}


// /** counter class */ complex version 
// class Counter extends Module{
  
//   val io = IO(new Bundle {
//     val reset_n = Input(UInt(1.W))
//     val cnt_en = Input(UInt(1.W))
//     val cnt_s = Output(UInt(1.W))
//     })
//   object State extends ChiselEnum {
//     val sReset, sIdle, sCount = Value
//   }
//   // set initial state
//   val state = RegInit(State.sReset)
//   val delay_when_finish = RegInit(0.U(1.W))
//   // internal variables
//   val cnt = RegInit(0.U(3.W))
//   // default values for output
//   io.cnt_s := 0.B
//   // state machine transition
//   switch(state) {
//     is(State.sReset){
//       cnt := 0.U
//       io.cnt_s := 0.B
//       when(io.reset_n === 0.B & io.cnt_en === 1.B){
//         state := State.sCount
//         cnt := cnt + 1.U
//       } 
//     }

//     is(State.sIdle){
//       when(io.reset_n === 1.B){
//         state := State.sReset
//         cnt := 0.U
//         io.cnt_s := 0.B
//       } .otherwise{
//         when(io.cnt_en === 1.B){
//           state := State.sCount
//         }
//       }
//     }

//     is(State.sCount){
//       when(io.reset_n === 1.B){
//         state := State.sReset
//         cnt := 0.U
//         io.cnt_s := 0.B 
//       } .otherwise{
//         when(cnt === 7.U){
//           cnt := 0.U
//           io.cnt_s := 1.B 
//           when(io.cnt_en === 0.B){
//             delay_when_finish := 1.B
//           }
//         } 
//         .elsewhen(io.cnt_en === 1.B & delay_when_finish === 0.B){
//           cnt := cnt + 1.U 
//         }
//         .elsewhen(delay_when_finish === 1.B & cnt === 0.U)
//         {
//           delay_when_finish := 0.B
//         }
//       }
//     }
//   }
// }


/** shift register class */
class ShiftRegister extends Module{
  
  val io = IO(new Bundle {
    val rxd = Input(UInt(1.W))
    val data = Output(UInt(8.W))
    })

  val shift_reg = RegInit(0.U(8.W))

  // functionality
  shift_reg := Cat(shift_reg(6,0), io.rxd)
  io.data := shift_reg
}

/** 
  * The last warm-up task deals with a more complex component. Your goal is to design a serial receiver.
  * It scans an input line (“serial bus”) named rxd for serial transmissions of data bytes. A transmission 
  * begins with a start bit ‘0’ followed by 8 data bits. The most significant bit (MSB) is transmitted first. 
  * There is no parity bit and no stop bit. After the last data bit has been transferred a new transmission 
  * (beginning with a start bit, ‘0’) may immediately follow. If there is no new transmission the bus line 
  * goes high (‘1’, this is considered the “idle” bus signal). In this case the receiver waits until the next 
  * transmission begins. The outputs of the design are an 8-bit parallel data signal and a valid signal. 
  * The valid signal goes high (‘1’) for one clock cycle after the last serial bit has been transmitted, 
  * indicating that a new data byte is ready.
  */
class ReadSerial extends Module{
  
  val io = IO(new Bundle {
    val reset_n = Input(UInt(1.W))
    val rxd = Input(UInt(1.W))
    val valid = Output(UInt(1.W))
    val data = Output(UInt(8.W))
    })


  // instanciation of modules
    val ins_controller = Module(new Controller())
    val ins_counter = Module(new Counter())
    val ins_shift_register = Module(new ShiftRegister())

  // connections between modules
    ins_controller.io.reset_n := io.reset_n
    ins_controller.io.rxd := io.rxd 
    ins_controller.io.cnt_s := ins_counter.io.cnt_s

    ins_counter.io.reset_n := io.reset_n
    ins_counter.io.cnt_en := ins_controller.io.cnt_en

    ins_shift_register.io.rxd :=  io.rxd 

    io.valid := ins_controller.io.valid 
    io.data := ins_shift_register.io.data  
  // global I/O 
  /* 
   * TODO: Describe output behaviour based on the input values and the internal signals
   */

}
