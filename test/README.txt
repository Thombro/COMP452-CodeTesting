Put your JUnit test classes and test doubles in this folder.


storing my notes in here
for game over panel, create a subclass of CSVWriter that stores the String[2] and returns it through some method
then create 2 versions of the function: one that takes in a CSVWriter and one that doesn't, with the one that doesn't sending in the same CSVWriter as would normally have been generated inside
oops: "UI components and classes (i.e., the “view”) should not contain non-trivial logic in their
       methods. They should simply send and receive data and events from the UI, then rely on
       other classes to do non-trivial processing and calculations based on the data and events."
       I failed this? need to move generateGuessText to other class?