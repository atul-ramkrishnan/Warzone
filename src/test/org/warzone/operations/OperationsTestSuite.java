package org.warzone.operations;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * A test suite that includes tests for various operations.
 */
@SelectClasses({BinomialDistributionPickerTest.class, DominationMapIOTest.class, RemovePlayerTest.class, ConquestMapIOTest.class,
                ConquestMapIOAdapter.class})
@Suite
@SuiteDisplayName("Operations Test Suite")
public class OperationsTestSuite {

}
