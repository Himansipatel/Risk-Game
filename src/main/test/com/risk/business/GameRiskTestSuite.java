package com.risk.business;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This is an test suite case for Game Risk Test Cases
 * 
 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a>
 * @author <a href="himansipatel1994@gmail.com">Himansi Patel</a>
 * @version 0.0.1
 */
@RunWith(Suite.class)
@SuiteClasses({ ManageGamePlayTest.class, ManageMapTest.class, ManagePlayerTest.class, ManageDominationTest.class })
public class GameRiskTestSuite {

}
