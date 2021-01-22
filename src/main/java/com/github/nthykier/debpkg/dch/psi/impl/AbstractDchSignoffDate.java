// This is a generated file. Not intended for manual editing.
package com.github.nthykier.debpkg.dch.psi.impl;

import com.github.nthykier.debpkg.dch.psi.DchSignoffDate;
import com.github.nthykier.debpkg.dch.psi.DchTypes;
import com.github.nthykier.debpkg.deb822.psi.impl.Deb822PsiImplUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class AbstractDchSignoffDate extends ASTWrapperPsiElement implements DchSignoffDate {

  private static final TokenSet SIGNOFF_DATE_TOKEN_SET = TokenSet.create(DchTypes.SIGNOFF_DATE_TOKEN);

  private String cachedText;
  private ZonedDateTime parsedDate = null;

  public AbstractDchSignoffDate(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void subtreeChanged() {
    super.subtreeChanged();
    this.cachedText = null;
    this.parsedDate = null;
  }


  @Override
  public String getText() {
    /* Guide lookup and cache the text; we know there is exactly one leaf beneath this node containing the
     * text we want.  The default class structure does not and have to copy the text around, which is expensive.
     */
    if (cachedText == null) {
      cachedText = Deb822PsiImplUtil.getTextFromCompositeWrappingAToken(this, SIGNOFF_DATE_TOKEN_SET);
    }
    return cachedText;
  }

  @Override
  public ZonedDateTime getSignoffDate() {
    if (parsedDate == null) {
      for (DateTimeFormatter format : VALID_DATE_FORMATS) {
        try {
          parsedDate = ZonedDateTime.parse(getText(), format);
          break;
        } catch (DateTimeParseException ignored) {
        }
      }
    }
    return this.parsedDate;
  }
}
