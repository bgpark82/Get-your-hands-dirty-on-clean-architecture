package com.bgpark.app;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class DependencyRuleTest {

    @DisplayName("도메인 레이어에서 어플리케이션 레이어 방향으로 의존성 없음 확")
    @Test
    void domainLayerDosNotDependOnApplicationLayer() {
        noClasses()
                .that()
                .resideInAPackage("com.bgpark.app.account.domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("com.bgpark.app.account.application..")
                .check(new ClassFileImporter().importPackages("com.bgpark.app.account.."));
    }
}
