package com.github.nthykier.debpkg.dch;

import com.intellij.testFramework.ParsingTestCase;

public class DchParsingTest extends ParsingTestCase {

    public DchParsingTest() {
        super("", "dch", new DchParserDefinition());
    }

    public void testParsingTestData() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData/dch";
    }

    @Override
    protected boolean skipSpaces() {
        return false;
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }
}
