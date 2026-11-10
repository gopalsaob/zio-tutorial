# ZIO Tutorial

Live-coding material for a team presentation on ZIO, following the
"ZIO from Scratch" video series (Parts 1-3).

- Scala 3.10.0, sbt 1.13.1, JDK 17+
- The talks build the core of ZIO by hand. The real ZIO library (2.1.27) is used by the comparison examples below.

| Part | Video | Code |
|------|-------|------|
| 1 | https://www.youtube.com/watch?v=wsTIcHxJMeQ | `src/main/scala/example/zioFromScratch.scala` |
| 2 | https://www.youtube.com/watch?v=g8Tuqldu2AE | _not yet added_ |
| 3 | https://www.youtube.com/watch?v=0IU9mGO_9Rw | _not yet added_ |

Run Part 1: `sbt "runMain example.main"`

Run the real ZIO examples:

- A ZIO value is a lazy description: `sbt "runMain example.FunctionalEffectSystem"`
- Fiber vs Future: `sbt "runMain example.FiberVsFuture"`

Notes: [Future vs ZIO table](docs/future-vs-zio.md)
