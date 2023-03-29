package com.github.nthykier.debpkg.deb822.dialects;

import com.github.nthykier.debpkg.deb822.Deb822FileType;
import com.github.nthykier.debpkg.deb822.inspections.DCtrlMissingRulesRequiresRootInspection;
import com.intellij.codeInsight.generation.actions.CommentByLineCommentAction;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DebianControlLightPlatformCodeInsightTestCase extends LightPlatformCodeInsightFixture4TestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/testData/dctrl";
    }

    @Test
    public void testCrashGH107() {
        myFixture.configureByText(Deb822DialectDebianControlFileType.INSTANCE, "Package: foo\n");
        myFixture.enableInspections(DCtrlMissingRulesRequiresRootInspection.class);
        myFixture.checkHighlighting(false, false, true, true);
    }

}
