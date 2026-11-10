package example

import zio.*

import java.io.IOException
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object FiberVsFuture extends ZIOAppDefault {

  def futureDemo(): Unit = {
    given ExecutionContext = ExecutionContext.global

    println("Future 1. defining the Future")
    val future: Future[Int] = Future {
      println("Explode")
      42
    }
    Thread.sleep(500)
    println("Future 2. never awaited, yet the body already ran: defining it was the side effect")

    val first  = Await.result(future, Duration.Inf)
    val second = Await.result(future, Duration.Inf)
    println(s"Future 3. awaited twice, got $first and $second, body did not run again")
  }

  val effect: UIO[Int] = ZIO.succeed {
    println("ZIO >>> body is running")
    42
  }

  val startFiber: UIO[Fiber[Nothing, Int]] = effect.fork

  val zioProgram: ZIO[Any, IOException, Unit] =
    for {
      _      <- Console.printLine("ZIO 2. running the program starts the first fiber")
      fiber  <- startFiber
      first  <- fiber.join
      second <- fiber.join
      _      <- Console.printLine(s"ZIO 3. joined the same fiber twice, got $first and $second, body ran once")
      _      <- Console.printLine("ZIO 4. startFiber is a description, running it again starts a new fiber")
      other  <- startFiber
      third  <- other.join
      _      <- Console.printLine(s"ZIO 5. second fiber got $third, body ran again")
    } yield ()



  def run = {
    futureDemo()
    println()
    zioProgram
  }
}
