package org.warzone.states;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * A test suite that includes tests for the states in the warzone game.
 */
@SelectPackages({"org.warzone.states.commands"})
@Suite
@SuiteDisplayName("States Test Suite")
public class StatesTestSuite {

}
