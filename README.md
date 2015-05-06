# Why Would I Want To Use This Annotation?

I wrote this annotation to help me when working with legacy code. Much of the code I work with does not have associated unit tests, so its behavior is not well documented.
When adding functionality or changing an existing part of the system, I try to write as many unit tests as I can to document the behavior and to help ensure that I don't break existing functionality.

In a perfect world, the legacy code would all be correct and the unit tests would be an accurate description of the system's behavior.
Since I don't live in a perfect world, the reality is that quite often the legacy code contains bugs. By writing tests that document the behavior of the system, I am also writing tests that pass because of these bugs.
I don't want someone else to look at the tests as an accurate description of how the system works, especially if the behavior is dependent on defects.
The answer I went with was to create an annotation that can be added to (preferably test) methods to indicate that the behavior of the method is dependent on a known defect.

Here's an example from the code that prompted me to create this annotation:

    @Test(expected = InvalidRequestException.class)
    public void testValidateNullResponse() {
        ProtocolParser.validateResponse(null);
    }

This test documents the system as it currently exists, but the behavior is not correct. The `ProtocolParser` class should be throwing an `InvalidResponseException`, not an `InvalidRequestException`.
To document this defect, I add the following annotation to the test method:

`@KnownDefect("Should throw InvalidResponseException")`

The correct behavior is now documented, and the test still passes.

## How do I use this?

The simplest way to use these annotations is with Maven. Simply add the following to your POM:

    <dependency>
        <groupId>com.megatome.knowndefects</groupId>
        <artifactId>knowndefects</artifactId>
        <version>1.1</version>
        <scope>test</scope>
    </dependency>
    
If you're using Gradle:

    testCompile "com.megatome.knowndefects:knowndefects:1.1"

Portions of this code were written in front of a live studio audience.