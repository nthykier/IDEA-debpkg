package com.github.nthykier.debpkg.deb822;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Deb822CompletionContributorTest extends LightPlatformCodeInsightFixture4TestCase {
    @Test
    public void completionItems() {
        myFixture.configureByText("test.deb822", "");

        myFixture.type("Sec");
        assertAutocompletedSingleMatch();
        myFixture.configureByText("test.deb822", "Rules-Requires-Root: ");
        myFixture.type("bin");
        assertAutocompletedSingleMatch();


        myFixture.configureByText("test.deb822", "Rules-Requires-Root: ");
        myFixture.type("");
        assertLookupElementContains("no");


        myFixture.configureByText("test.deb822", "");
        myFixture.type("Pa");
        assertLookupElementContains("Package: ");

        /*
        myFixture.configureByText("test.deb822", "Rules-Requires-Root: ");
        myFixture.type("${");
        assertLookupElementContains("${}");

        myFixture.configureByText("test.deb822", "Rules-Requires-Root: ");
        myFixture.type("${Arc");
        assertAutocompletedSingleMatch();*/
    }

    private void printMatches(LookupElement[] items) {
        if (items != null) {
            System.out.println("Match count: " + items.length);
            for (LookupElement element : items) {
                System.out.println("Suggestion: " + element.getLookupString());
            }
        } else {
            System.out.println("Single match that was auto-completed");
        }
    }

    private void assertAutocompletedSingleMatch() {
        LookupElement[] items = myFixture.completeBasic();
        printMatches(items);
        assertNull(items);
    }

    private void assertLookupElementContains(String value) {
        LookupElement[] items = myFixture.completeBasic();

        printMatches(items);
        assertTrue(items != null && items.length > 0);
        Set<String> matches = Arrays.stream(items).map(LookupElement::getLookupString).collect(Collectors.toSet());

        assertContainsElements(matches, value);
    }
}