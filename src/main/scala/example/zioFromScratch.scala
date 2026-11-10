package example


import example.MyZIO.{Test, ZIO}
import example.Suite.assert

import scala.concurrent.Future
import scala.util.{Failure, Success}

object MyZIO {

//  sealed trait ZIO[+A] {
  sealed trait ZIO[+A] { self =>

//    def run(a: A): A

    // continuation
    def run(callback: A => Unit): Unit

    def zip[B](right: ZIO[B]): ZIO[(A, B)] = ZIO.Zip(self, right)

    def map[B](f: A => B): ZIO[B] = ZIO.ZMap(self, f)

    def flatMap[B](f: A => ZIO[B]): ZIO[B] = ZIO.FlatMap(self, f)

//    def fork: ZIO[ZIO.Fiber[A]] = ZIO.Fork(self)
  }

  object ZIO {

    case class Succeed[A](a: A) extends ZIO[A] {

//      def run(a: A): A = a
      override def run(callback: A => Unit): Unit = callback(a)
    }
    case class Zip[A, B](left: ZIO[A], right: ZIO[B]) extends ZIO[(A, B)] {
      override def run(callback: ((A, B)) => Unit): Unit =
        left.run { a =>
          right.run { b =>
            callback((a, b))
          }
        }
    }

    case class ZMap[A, B](zio: ZIO[A], f: A => B) extends ZIO[B] {
      def run(callback: B => Unit): Unit = zio.run { a =>
        callback(f(a))
      }
    }

    case class Effect[A](a: () => A) extends ZIO[A] {
      override def run(callback: A => Unit): Unit =
        callback(a())
    }

    case class FlatMap[A, B](za: ZIO[A], f: A => ZIO[B]) extends ZIO[B] {
      override def run(callback: B => Unit): Unit =
        za.run { a =>
          f(a).run { b =>
            callback(b)
          }
        }
    }

    case class Fork[A](z: ZIO[A]) extends ZIO[Fiber[A]] {
      val f: Fiber[A] = Fiber(z)
      f.start()

      override def run(callback: Fiber[A] => Unit): Unit = callback(f)
    }

    case class Fiber[A](z: ZIO[A]) {
      var result: Option[A] = None
      var callbacks: List[A => Unit] = List()

      def start(): Unit = scala.concurrent.ExecutionContext.global.execute { () =>
        z.run { a =>
          result = Some(a)
          callbacks.foreach(cb => cb(a))
        }
      }

      def join: ZIO[A] = ZIO.async { cb =>
        result match {
          case Some(value) => cb(value)
          case None        => callbacks = cb :: callbacks
        }
      }

    }

    def succeed[A](a: A): ZIO[A] = ZIO.Succeed(a)

    def effect[A](a: => A): ZIO[A] = ZIO.Effect(() => a)

    def async[A](register: (A => Unit) => Unit): ZIO[A] = ZIO.Async(register)

    case class Async[A](register: (A => Unit) => Unit) extends ZIO[A] {
      override def run(callback: A => Unit): Unit = register(callback)
    }

  }

  trait ZIOApp {

    def run(args: Array[String]): ZIO[Unit]

    final def main(args: Array[String]): Unit = run(args).run(_ => ())
  }

  trait Test {
    def assert[A](description: String, expectation: A)(zio: ZIO[A]): Unit = {
      zio.run { a =>
        if (a == expectation)
          println(s"$description - OK")
        else
          println(s"$description - KO | expected: $expectation, got: $a")
      }
    }
  }
}

object Suite extends Test {

  @main def main(): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

//    val zio = ZIO.succeed(5)
//    println(zio.run)

    assert("succeeds returns the given value", 5)(ZIO.succeed(5))

//    assert("zip makes a pair of left and right", (true, "this is great"))(ZIO.succeed(true).zip(ZIO.succeed("this is great")))
//
//    assert("map transforms output", "HELLO WORLD")(ZIO.succeed("hello world").map(_.toUpperCase))
//
//    assert("effect actually prints, but only once", ())({
//      ZIO.effect(println("I am printing!!!")); ZIO.effect(println("I am printing!!!"))
//    })
//
//    assert("flatmap runs effects in sequence", 50)(for {
//      x <- ZIO.succeed(5)
//      _ <- ZIO.effect(println(s"this is x: $x"))
//      y <- ZIO.succeed(10)
//      _ <- ZIO.effect(println(s"this is y: $y"))
//    } yield x * y)
//
//    assert("asynnc retunrs the value of the async action", 77)(ZIO.async(cb =>
//      Future {
//        println("STARTING...")
//        Thread.sleep(1000)
//        77
//      }.onComplete {
//        case Success(x) => cb(x)
//        case Failure(e) => println(s"Failure, ${e.getMessage}")
//      }
//    ))

    //  assert("runs 2 long computations in parallel", 7999992) {
    //    def expensiveComputation = ZIO.effect{
    //      println("Long computation - Starting")
    //      val result = List.fill(999999)(4).sum
    //      println("Long computation - Done")
    //      result
    //    }
    //
    //    for {
    //      fiber1 <- expensiveComputation.fork
    //      fiber2 <- expensiveComputation.fork
    //      _      <- ZIO.effect(println("forked both..."))
    //      r1     <- fiber1.join
    //      r2     <- fiber2.join
    //    } yield r1 + r2
    //
    //  }

    //  Thread.sleep(3000)
  }

}