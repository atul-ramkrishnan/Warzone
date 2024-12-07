package org.warzone.states.commands;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * A test suite that includes tests for various commands.
 */
@SelectClasses({AirliftTest.class, BlockadeTest.class, BombTest.class, CommandsTestSuite.class, OrderExecutionPhaseTest.class,
                DiplomacyTest.class, OrderTest.class})
@Suite
@SuiteDisplayName("Commands Test Suite")
public class CommandsTestSuite {

}
