package com.github.nthykier.debpkg.deb822;

import com.github.nthykier.debpkg.deb822.dialects.Deb822DialectDebianControlFileType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Deb822CompletionContributorTest extends LightPlatformCodeInsightFixture4TestCase {
    @Test
    public void completionItems() {
        myFixture.configureByText(Deb822DialectDebianControlFileType.INSTANCE, "");

        myFixture.type("Sec");
        assertAutocompletedSingleMatch();
        myFixture.configureByText(Deb822DialectDebianControlFileType.INSTANCE, "Rules-Requires-Root: ");
        myFixture.type("bin");
        assertAutocompletedSingleMatch();


        myFixture.configureByText(Deb822DialectDebianControlFileType.INSTANCE, "Rules-Requires-Root: ");
        myFixture.type("");
        assertLookupElementContains("no");


        myFixture.configureByText(Deb822DialectDebianControlFileType.INSTANCE, "");
        myFixture.type("Pa");
        assertLookupElementContains("Package: ");

        myFixture.configureByText(Deb822DialectDebianControlFileType.INSTANCE, "Rules-Requires-Root: ");
        myFixture.type("${");
        assertLookupElementContains("}");

        myFixture.configureByText(Deb822DialectDebianControlFileType.INSTANCE, "Foo: ");
        myFixture.type("${shlib");
        assertLookupElementContains("shlibs:Depends}");

        myFixture.configureByText(Deb822DialectDebianControlFileType.INSTANCE, "Foo: ");
        myFixture.type("${shlibs:Pre-Dep");
        assertAutocompletedSingleMatch();


        /* Dependency fields (tests DependencyLanguage code completion) */
        myFixture.configureByText(Deb822DialectDebianControlFileType.INSTANCE, "Depends: ");
        myFixture.type("${shlib");
        assertLookupElementContains("${shlibs:Depends}");

        myFixture.configureByText(Deb822DialectDebianControlFileType.INSTANCE, "Pre-Depends: ");
        myFixture.type("${shlibs:Pre-Dep");
        assertAutocompletedSingleMatch();

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