package com.github.nthykier.debpkg.deb822;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase;
import org.junit.Test;

public class Deb822CompletionContributorTest extends LightPlatformCodeInsightFixture4TestCase {
    @Test
    public void completionItems() {
        myFixture.configureByText("test.deb822", "");

        myFixture.type("Pa");
        LookupElement[] items = myFixture.completeBasic();
        for (LookupElement element : items) {
            System.out.println("Suggestion: " + element.getLookupString());
        }
        // fixme: test the completion items
    }
}