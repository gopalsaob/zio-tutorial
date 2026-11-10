# Future vs ZIO

Comparison table from slide 7 of
[Error Management: Future vs ZIO](https://www.slideshare.net/slideshow/error-management-future-vs-zio/145070081)
by John De Goes (Dublin Scala Meetup).

| Criterion                     | Future        | ZIO           |
|-------------------------------|---------------|---------------|
| Performance                   | > 100x slower | > 100x faster |
| Cancellation & Timeouts       | ✗             | ✔             |
| Effect Combinators            | ✗             | ✔             |
| Resource Safety               | ✗             | ✔             |
| Fiber Concurrency             | ✗             | ✔             |
| Equational & Type Reasoning   | ✗             | ✔             |
| Testability                   | ✗             | ✔             |
| Error Management & Debugging  | ✗             | ✔             |


