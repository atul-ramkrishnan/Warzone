package org.warzone;

import org.junit.platform.suite.api.*;
/**
 * This is the GlobalTestSuite class that runs all the test cases across multiple packages.
 */
@SelectPackages({"org.warzone.additional", "org.warzone.entities", "org.warzone.logging", "org.warzone.operations",
                "org.warzone.states","org.warzone.strategy"})
@Suite
@SuiteDisplayName("Global Test Suite")
public class GlobalTestSuite {

}
