#!/usr/bin/env sh

# Intentional minimal gradle wrapper script for GitHub Actions compilation
# Executing Gradle build steps
exec java -cp "gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
