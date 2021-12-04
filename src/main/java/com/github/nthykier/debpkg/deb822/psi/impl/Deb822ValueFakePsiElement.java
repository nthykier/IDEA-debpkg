package com.github.nthykier.debpkg.deb822.psi.impl;

import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldKeyword;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Deb822ValueFakePsiElement extends Deb822FakePsiElementBase<PsiElement> {

    private final Deb822KnownFieldKeyword keyword;

    public Deb822ValueFakePsiElement(PsiElement element, @NotNull Deb822KnownFieldKeyword keyword) {
        super(element);
        this.keyword = keyword;
    }

    @Override
    public String getName() {
        return keyword.getValueName();
    }

    @Override
    public @Nullable String getDocumentation() {
        if (keyword != null) {
            StringBuilder doc = new StringBuilder(DocumentationMarkup.DEFINITION_START + "<b>" + keyword.getValueName() + "</b>" + DocumentationMarkup.DEFINITION_END + DocumentationMarkup.CONTENT_START);
            String docs = keyword.getValueDescription();
            if (docs != null) {
                doc.append("<p>").append(docs).append("</p>");
            } else {
                doc.append("<p>[Standard value; no documentation available]</p>");
            }
            if (keyword.isExclusive()) {
                doc.append("<br><p><b>Exclusive value</b>: <em>The value only makes sense when it is used alone (even if the field allows multiple values).</em></p>");
            }
            return doc.append(DocumentationMarkup.CONTENT_END).toString();
        }
        return null;
    }
}
