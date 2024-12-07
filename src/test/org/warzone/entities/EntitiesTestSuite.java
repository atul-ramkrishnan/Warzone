package org.warzone.entities;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * A test suite that includes tests for various entities.
 */
@Suite
@SuiteDisplayName("Entities Test Suite")
@SelectClasses({PlayerTest.class, CardsTest.class})
public class EntitiesTestSuite {

}