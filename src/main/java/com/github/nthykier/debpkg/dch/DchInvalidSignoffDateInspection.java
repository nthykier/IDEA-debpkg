package com.github.nthykier.debpkg.dch;

import com.github.nthykier.debpkg.Deb822Bundle;
import com.github.nthykier.debpkg.dch.psi.DchFile;
import com.github.nthykier.debpkg.dch.psi.DchSignoffDate;
import com.github.nthykier.debpkg.dch.psi.DchVisitor;
import com.github.nthykier.debpkg.util.AnnotatorUtil;
import com.intellij.codeInspection.*;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import static com.github.nthykier.debpkg.dch.psi.DchSignoffDateSupport.PREFERRED_DATE_FORMAT;

public class DchInvalidSignoffDateInspection extends LocalInspectionTool {

    private static final Pattern STRIP_DAY_NAME = Pattern.compile("^\\S+,\\s*");
    private static final DateTimeFormatter[] LENIENT_DATE_FORMAT_PATTERNS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("d MMM uuuu H:m:s X"),
            DateTimeFormatter.ofPattern("d MMM uuuu H:m:s z"),
            DateTimeFormatter.ofPattern("d MMM uuuu H:m:s VV"),
            // These are not even remotely valid, but as long as we know what was meant.
            DateTimeFormatter.ofPattern("uuuu-M-d H:m:s[ ]X"),
            DateTimeFormatter.ofPattern("uuuu-M-d H:m:s[ ]z"),
            DateTimeFormatter.ofPattern("uuuu-M-d H:m:s[ ]VV"),
            DateTimeFormatter.ofPattern("uuuu-M-d'T'H:m:s[ ]X"),
            DateTimeFormatter.ofPattern("uuuu-M-d'T'H:m:s[ ]z"),
            DateTimeFormatter.ofPattern("uuuu-M-d'T'H:m:s[ ]VV"),
    };


    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
        /* This only makes sense for debian/changelog files */
        if (holder.getFile().getLanguage() != DchLanguage.INSTANCE) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }
        if (!(holder.getFile() instanceof DchFile)) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }
        return new DchVisitor() {
            @Override
            public void visitSignoffDate(@NotNull DchSignoffDate o) {
                ProgressIndicatorProvider.checkCanceled();
                checkSignoffDate(holder, o, isOnTheFly);
            }
        };
    }

    private void checkSignoffDate(@NotNull final ProblemsHolder holder, @NotNull DchSignoffDate dchSignoffDate, boolean isOnTheFly) {
        LocalQuickFix quickFix = null;
        String reducedDate;
        ZonedDateTime parsedDate = null;
        ZonedDateTime actualDate = dchSignoffDate.getSignoffDate();
        if (actualDate != null) {
            return;
        }
        String text = dchSignoffDate.getText();
        // We ignore the day name as people often get it wrong.
        reducedDate = STRIP_DAY_NAME.matcher(text).replaceFirst("").trim();
        for (DateTimeFormatter parseFormat : LENIENT_DATE_FORMAT_PATTERNS) {
            try {
                parsedDate = ZonedDateTime.parse(reducedDate, parseFormat);
                break;
            } catch (DateTimeParseException ignored) {
            }
        }
        if (parsedDate != null) {
            String correctlyFormatted = parsedDate.format(PREFERRED_DATE_FORMAT);
            quickFix = AnnotatorUtil.replacementQuickFix(
                    Deb822Bundle.message("deb822.files.quickfix.fields.invalid-signoff-date-format.name"),
                    (project, pd) -> DchElementFactory.createSignoffDate(project, correctlyFormatted)
            );
        }

        InspectionManager inspectionManager = InspectionManager.getInstance(dchSignoffDate.getProject());
        ProblemDescriptor descriptor = inspectionManager.createProblemDescriptor(
                dchSignoffDate,
                Deb822Bundle.message("dch.files.inspection.invalid-signoff-date-format"),
                quickFix,
                ProblemHighlightType.ERROR,
                isOnTheFly
        );
        holder.registerProblem(descriptor);
    }
}
