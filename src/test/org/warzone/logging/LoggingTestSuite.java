package org.warzone.logging;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * A test suite that includes tests for logging-related functionality.
 */
@SelectClasses({FileLoggerTest.class, LogEntryTest.class, LogEntryBufferTest.class})

@Suite
@SuiteDisplayName("Logging Test Suite")
public class LoggingTestSuite {
}
