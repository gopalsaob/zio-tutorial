package example

import java.io.IOException
import zio.*

/**
 * ZIO is a purely functional library for doing async and concurrent programming
 * Lets you build applications that are testable, modular, maintainable, typesafe, reactive and resilient
 */
// Declarative Encoding, Functional Effect System (Programs as Values), Declarative over Imperative, Dynamite vs Explosion
// ZIO[R, E, A]
// def foo(r: R): E | A

/*
 * Local reasoning -> to understand, verify, and predict the behavior of a specific unit of code (like a single function
 *  or expression) by looking only at that unit—without needing to trace global state, hidden side effects, or all the
 * external places where the function might be called
 */

object FunctionalEffectSystem {

  val eager: Unit = println("*** EXPLOSION ***")

  val greeting: ZIO[Any, IOException, Unit] = Console.printLine("Hello from ZIO!")

  def repeat(times: Int): ZIO[Any, IOException, Unit] = greeting.repeatN(times)

  val neverRun: ZIO[Any, IOException, Unit] = Console.printLine("I am never printed")

  val foo =
    for {
      _ <- Console.printLine("1. greeting is only a description: nothing printed when it was defined")
      _ <- greeting
      _ <- Console.printLine("2. running the same description again prints again")
      _ <- greeting
      _ <- Console.printLine("3. Effects can be composed")
      _ <- repeat(10)
      _ <- Console.printLine("3. neverRun was defined but never executed: no side effect")
    } yield ()

  def main(args: Array[String]): Unit =
    Unsafe.unsafe { implicit unsafe =>
      Runtime.default.unsafe.run(foo).getOrThrowFiberFailure()
    }

}
