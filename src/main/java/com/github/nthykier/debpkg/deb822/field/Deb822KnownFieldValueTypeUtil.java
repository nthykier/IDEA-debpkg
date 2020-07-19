package com.github.nthykier.debpkg.deb822.field;

import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;

import java.util.function.Predicate;

class Deb822KnownFieldValueTypeUtil {

    final static Predicate<ASTNode> IS_WHITESPACE = new ASTNodeToTokenSetPredicate(TokenSet.WHITE_SPACE);
    final static Predicate<ASTNode> IS_COMMA = new ASTNodeToTokenSetPredicate(TokenSet.create(Deb822Types.COMMA));
    final static Predicate<ASTNode> IS_COMMENT = new ASTNodeToTokenSetPredicate(TokenSet.create(Deb822Types.COMMENT));

    private static class ASTNodeToTokenSetPredicate implements Predicate<ASTNode> {
        private final TokenSet set;

        ASTNodeToTokenSetPredicate(TokenSet set) {
            this.set = set;
        }
        public boolean test(ASTNode node) {
            return set.contains(node.getElementType());
        }
    }
}
