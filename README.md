# Statistical Analyser
This Android application performs statistical measures - currently mean and median - on data sets

## Building
This project uses Gradle, and in principle should be IDE agnostic, although currently only tested using Android Studio

There is a KTLint phase pre-build which will do some very basic auto-formatting and syntax checking to encourage consistent coding standards.

## Dagger
The project  uses Dagger 2 to provide dependency injection. An interface/implementation pattern is used.

## Tests
This project follows the default convention - source in main/java, unit tests in test/java and Instrumentation/UI tests in autotest/java. This of course is despite the majority of the project being written in Kotlin

## Package Structure
The project aims to follow default conventions as fast as possible. However, components will be grouped by feature rather than type (e.g. .feature.welcome.Activity rather than .activities.WelcomeActivity)