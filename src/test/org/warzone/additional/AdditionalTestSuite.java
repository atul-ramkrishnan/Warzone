package org.warzone.additional;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
/**
 * This is a method to perform additional tests.
 */
@Suite
@SuiteDisplayName("Additional Test Suite")
@SelectClasses({ValidateMapTest.class})
public class AdditionalTestSuite {

}
