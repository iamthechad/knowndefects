Why Would I Want To Use This Annotation?

I wrote this annotation to help me when working with legacy code. Much of the code I work with does not have associated unit tests, so its behavior is not well documented.
When adding functionality or changing an existing part of the system, I try to write as many unit tests as I can to document the behavior and to help ensure that I don't break existing functionality.

In a perfect world, the legacy code would all be correct and the unit tests would be an accurate description of the system's behavior.
Since I don't live in a perfect world, the reality is that quite often the legacy code contains bugs. By writing tests that document the behavior of the system, I am also writing tests that pass because of these bugs.
I don't want someone else to look at the tests as an accurate description of how the system works, especially if the behavior is dependent on defects.
The answer I went with was to create an annotation that can be added to (preferably test) methods to indicate that the behavior of the method is dependent on a known defect.

Here's an example from the code that prompted me to create this annotation:

public void testValidateNullResponse() {
   try {
      ProtocolParser.validateResponse(null);
      fail();
   } catch (InvalidRequestException expected) {
      // Caught expected exception
   }
}

This test documents the system as it currently exists, but the behavior is obviously not correct. The ProtocolParser class should be throwing an InvalidResponseException, not an InvalidRequestException.
To document this defect, I add the following annotation to the test method:

@KnownDefect("Should throw InvalidResponseException")

The correct behavior is now documented, and the test still passes.

Portions of this code were written in front of a live studio audience.

/*
 * Copyright 2012 Megatome Technologies
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
  