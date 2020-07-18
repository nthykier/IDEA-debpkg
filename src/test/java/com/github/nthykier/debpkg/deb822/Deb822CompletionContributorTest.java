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
        if (items != null) {
            for (LookupElement element : items) {
                System.out.println("Suggestion: " + element.getLookupString());
            }
        } else {
            System.out.println("No matches");
        }
        /* It matches Package and Homepage; but Package is supposed to come first (and Homepage is irrelevant) */
        assertTrue(items != null && items.length > 0);
        assertEquals("Suggested field is Package", items[0].getLookupString(), "Package: ");
    }
}