package org.warzone.savegame;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * A test suite that includes tests for various operations.
 */
@SelectClasses({GameStateManagerTest.class})
@Suite
@SuiteDisplayName("Save game Test Suite")
public class SaveGameTestSuite {

}
