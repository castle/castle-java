package io.castle.client;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This suite runs all tests that can be run without user interaction. {@link io.castle.client.PairingINTTest} contains MFA tests.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ SessionINTTest.class, MonitorINTTest.class, UsersINTTest.class})
public class AutomaticTestSuite {
}
