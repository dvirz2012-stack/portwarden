## netstat essentials

netstat:
    a built-in command line tool that displays active network connections, routing tables and ports
    currently in use.

    -a:
        displays all active connections and listening ports.
    -n:
        displays addresses and port numbers in numerical form instead of attempting
        to resolve hostnames.
    -o:
        displays the owning process ID associated with each connection.
    -p tcp:
        limits the results to exclusively TCP traffic.

## Kotlin syntax essentials

Variables:
    val is read-only, just like final
    var is mutable, meaning it is changable throughout the code

Null-safety:
    Kotlin's compiler system designed to prevent NullPointerException crashes:

        Type?:
            Marks a variable as nullable (such as String? can hold a string or null).

        toIntOrNull() / toLongOrNull():
            attempts to convert a string to a number, returning null safely instead of crashing
            if the string contains letters.

        ?: (elvis operator):
            provides a fallback default value if an expression evaluates to null (for example val x = port ?: 8080)

data class:
    a specialized Kotlin class designed solely to hold data. the compiler automatically generates boilerplate
    methods behind the scenes, including equals(), hashCode(), toString() and copy().

@Serializable:
    an annotation that tells the compiler:
        Generate the code needed to turn this class into a string and back (its like making a class json).

## Kotlin string manipulation

split():
    split() removes whatever characters you passed as a parameter in it, and breaks the entire string into chunks wherever that character appears returning a list of all pieces.

    for example:
```kotlin

        val s: String = "hello,world,or,universe"
        val arr: List<String> = s.split(",")
        
        // arr = [hello, world, or, universe]
        for (item in arr){
            print(s)
        }

        // hello
        // world
        // or
        // universe

```    

trim():
    trim() removes whitespaces from both sides of the string, for example:
```kotlin

    val s: String = "    hello world or universe    "
    print(s.trim())
    // "hello word or universe"

```

Basic regex:
    regex (regular expression) is a sequence of characters that forms a search pattern. when you search for data in a text, you can use this search 
    pattern to describe what you are searching for. 

    regex patterns:
        [abc] => Find one character from the options between the brackets
        [^abc] => Find one character not between the brackets
        [0-9] => Find one character from the range 0 to 9

    Metacharacters:
        | => Find a match for any one of the patterns separated by | as in: application|programming|interface
        . => Find just one occurrence of any characters
        ^ => Finds a match at the beginning of a string as in: ^kotlin
        $ => Finds a match at the end of a string as in: kotlin$
        \d => Find a digit
        \s => Find a whitespace character
        \b => Find a match at the beginning of a word like this: \bKOTLIN, or at the end of a word like this: KOTLIN\b
        \uxxxx => Find the unicode character specified by the hexadecimal number xxxx

    Quanitifiers:
        s+ => Matches any string that contains at least one s
        s* => Matches any string that contains zero or more occurrences of s
        s? => Matches any string that contains zero or one occurrences of s
        s{x} => Matches any string that contains a sequence of X s's
        s{x,y} => Matches any string that contains a sequence of X to Y s's 
        s{x,} => Matches any string that contains a sequence of at least X s's

    toRegex():
        whenever we want the text pattern to be metacharacters and have actual functionality, we use toRegex(). it turns the string of metacharacters
        into a pattern:
```kotlin

            val text = "port    8080"
            val parts = text.split("\\s+".toRegex())
            // kotlin knows \s+ is a rule meaning "one or more spaces"
            // ["port", "8080"]
            
```
    
## Kotlin libraries

ProcessBuilder:
    A Java/Kotlin standard library class used to create, configure, and execute operating system commands
    like launching cmd.exe or terminal scripts directly from inside your code.

ProcessHandle:
    A modern API introduced in Java 9 to interact with running operating system processes. it allows you
    to query process details and invoke methods like .destroyForcibly() to terminate frozen or stubborn
    processes

coding area

```kotlin

    package com.dvir.portwarden.model

    import kotlinx.serialization.Serializable

    @Serializable
    data class PortInfo (

        val port: Int,
        val pid: Long

    )

```

we made a data class holding the values port and pid (process Id) and made it to string.


```kotlin

    fun main(){
        val process = ProcessBuilder("cmd.exe", "/c", "netstat -ano -p tcp").start()

        process.inputStream.bufferedReader().useLines { lines ->

            lines.forEach { line ->
                println(line)

            }
        }
    }

```
