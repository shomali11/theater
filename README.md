# Introduction

In this document, we will be going over the new redesign of the `theater` app to explain the thought process behind my decisions.

**I left the problem description and instructions in the `old_READ.me` file.**

## Dependencies

- org.projectlombok.lombok
  - **New Dependency** 
  - *An annotation-based Java library that allows you to reduce boilerplate code*
- com.google.inject.guice
  - **New Dependency**
  - *A dependency injection framework that allows you to inject dependencies via annotations*
- com.google.code.gson
  - **New Dependency**
  - *A library to serialize Java objects to JSON and vice versa*
- junit.jupiter
  - **Existing Dependency**
  - *Upgraded it to the latest stable version*
- junit.platform
  - **Existing Dependency**
  - *Upgraded it to the latest stable version*

## `theater`

### Assumptions
* The new requirements claim:
  * Any movies showing starting between 11AM ~ 4pm, you'll get 25% discount
  * Any movies showing on 7th, you'll get 1$ discount
* I assumed that:
  * [11AM ~ 4pm) 11 AM is included while 4 PM is excluded
  * That the 7th is the sequence number and not the day of the month.

### Current Features
* Customer can make a reservation for the movie
  * And, system can calculate the ticket discount for customer's reservation
* Theater have a following discount rules
  * 20% discount for the special movie
  * $3 discount for the movie showing 1st of the day
  * $2 discount for the movie showing 2nd of the day
  * $1 discount for the movie showing 7th of the day
  * Any movies showing starting between 11AM - 4pm, you'll get 25% discount
* System can display movie schedule with simple text & json format

### Structure `com.jpmc.theater`
* `dao`
  * The Data Access Object layer that will interact with the "in memory database"
* `models`
  * Simplified Models that represent the various objects without heavy business logic
* `printers`
  * Where the printing/displaying of various formats occurs  
* `providers`
  * Contains the providers
* `services`
  * The Service Layer that encapsulates the `dao` layer. This focuses on the business logic. 
* `Theater`
  * The main entry point of our application 
 
### Thought Process

Ideally, this application would be dockerized and interacts with a dockerized database.
However, for the sake of time and simplicity, I created a `dao` layer 
that contains the "low level" interactions with a database. It just stores data in memory.

The `services` layer encapsulated the `dao` layer, and it serves two main things:
* Contains the business logic
* Could be turned into an API that can be called over the web

If I had the time, I would have turned them into APIs with authentication and authorization as needed.

The `models` contain the simplified representations of our objects such as Movie, Showing, Customer, ... 
You will notice a few things:
- All the business logic was removed from these plain objects into the `services`
- Lombok annotations were used to reduce boiler plate and auto generate the needed methods
- The models use `id`s to be able to represent each "record"

Notice that in the `Showing` object, I introduced a new "id" field. 
I did not want to use the `sequence` as an "id" because that represents the sequence of showings within a single day.

Similarly, I introduced a new "id" in the `Movie` object and opted not to use the `title` as an id.
Moreover, I introduced an enum `MovieCode` to make it easier to interact with `SPECIAL` movies.

In the `printers` section, you will see the logic that prints in JSON and Text formats to standard output.
I kept it simple here, although if I had the time, I would provide support to "Write" to any `OutputStream` desired.

In the `providers` section, you will see the `LocalDateProvider` class. 
The most important change here is converting it to a thread safe singleton.
I kept the lazy initialization intact, but given that this is not an expensive instance, I would have converted it to:

```
private static final LocalDateProvider instance = new LocalDateProvider();
```

and removed the `singleton()` method while keeping the `currentDate()` one.

As for the `Theater` java class. I removed most of the logic there and 
converted it to the main entry point of the application.

All it does is setup the app, the dependency injections, push some data into the services 
which will call the dao classes to store in their databases. 
Finally, it prints/displays the schedule in both text and json formats:

Text:

```
2022-10-28
===================================================
1: 2022-10-28T09:00 Turning Red (1 hour 25 minutes) $11.0
2: 2022-10-28T11:00 Spider-Man: No Way Home (1 hour 30 minutes) $12.5
3: 2022-10-28T12:50 The Batman (1 hour 35 minutes) $9.0
4: 2022-10-28T14:30 Turning Red (1 hour 25 minutes) $11.0
5: 2022-10-28T16:10 Spider-Man: No Way Home (1 hour 30 minutes) $12.5
6: 2022-10-28T17:50 The Batman (1 hour 35 minutes) $9.0
7: 2022-10-28T19:30 Turning Red (1 hour 25 minutes) $11.0
8: 2022-10-28T21:10 Spider-Man: No Way Home (1 hour 30 minutes) $12.5
9: 2022-10-28T23:00 The Batman (1 hour 35 minutes) $9.0
===================================================
```

JSON:

```
===================================================
{
  "date": "2022-10-28",
  "movieShowingList": [
    {
      "sequenceOfTheDay": 1,
      "showStartTime": "2022-10-28T09:00",
      "title": "Turning Red",
      "runningTime": "1 hour 25 minutes",
      "ticketPrice": 11.0
    },
    {
      "sequenceOfTheDay": 2,
      "showStartTime": "2022-10-28T11:00",
      "title": "Spider-Man: No Way Home",
      "runningTime": "1 hour 30 minutes",
      "ticketPrice": 12.5
    },
    {
      "sequenceOfTheDay": 3,
      "showStartTime": "2022-10-28T12:50",
      "title": "The Batman",
      "runningTime": "1 hour 35 minutes",
      "ticketPrice": 9.0
    },
    {
      "sequenceOfTheDay": 4,
      "showStartTime": "2022-10-28T14:30",
      "title": "Turning Red",
      "runningTime": "1 hour 25 minutes",
      "ticketPrice": 11.0
    },
    {
      "sequenceOfTheDay": 5,
      "showStartTime": "2022-10-28T16:10",
      "title": "Spider-Man: No Way Home",
      "runningTime": "1 hour 30 minutes",
      "ticketPrice": 12.5
    },
    {
      "sequenceOfTheDay": 6,
      "showStartTime": "2022-10-28T17:50",
      "title": "The Batman",
      "runningTime": "1 hour 35 minutes",
      "ticketPrice": 9.0
    },
    {
      "sequenceOfTheDay": 7,
      "showStartTime": "2022-10-28T19:30",
      "title": "Turning Red",
      "runningTime": "1 hour 25 minutes",
      "ticketPrice": 11.0
    },
    {
      "sequenceOfTheDay": 8,
      "showStartTime": "2022-10-28T21:10",
      "title": "Spider-Man: No Way Home",
      "runningTime": "1 hour 30 minutes",
      "ticketPrice": 12.5
    },
    {
      "sequenceOfTheDay": 9,
      "showStartTime": "2022-10-28T23:00",
      "title": "The Batman",
      "runningTime": "1 hour 35 minutes",
      "ticketPrice": 9.0
    }
  ]
}
===================================================
```

*I could have left the values in the JSON response in its raw values 
but I opted to reuse the same date and time string formatting for readability purposes*

Finally the `test`, is where all the tests exist. Given the rewrite of the entire app, 
the tests were rewritten completely too. 
The tests are consistent, with predictable inputs and outputs along with a high code coverage 
*(100% for `dao` and `services` )*

You are welcome to check out the various tests, but the `ReservationServiceTests` 
is most likely the most interesting one. It contains logic that test all various combinations 
of valid, invalid and discount options

**You will notice that instead of passing a sequence I am passing the `Showing` id. 
This is due to my attempt to working with the various objects with their IDs and 
treating them as database records**

Another good one is the `ScheduleServiceTests`, as it shows how the scheduling is created.

**You will notice that I am requiring the `date` to be passed as we could be generating 
schedules for different days. This is as opposed to only generating the "current" day's schedule**

### Set Up

* To clean

```
mvn clean
```

* To compile

```
mvn compile
```

* To test

```
mvn test
```