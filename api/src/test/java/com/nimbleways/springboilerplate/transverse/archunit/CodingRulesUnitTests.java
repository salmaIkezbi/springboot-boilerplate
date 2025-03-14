package com.nimbleways.springboilerplate.transverse.archunit;

import static com.nimbleways.springboilerplate.Application.BASE_PACKAGE_NAME;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

import com.nimbleways.springboilerplate.common.domain.ports.RandomGeneratorPort;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import java.util.UUID;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@UnitTest
@AnalyzeClasses(packages = BASE_PACKAGE_NAME, importOptions = { ImportOption.DoNotIncludeTests.class })
public class CodingRulesUnitTests {

  @ArchTest
  private final ArchRule no_generic_exceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

  @ArchTest
  private final ArchRule no_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

  @ArchTest
  private final ArchRule no_access_to_standard_streams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

  @ArchTest
  private final ArchRule no_access_to_standard_streams_as_method = noClasses()
      .should(ACCESS_STANDARD_STREAMS);

  @ArchTest
  private final ArchRule no_jodatime = NO_CLASSES_SHOULD_USE_JODATIME;

  @ArchTest
  private final ArchRule loggers_should_be_private_static_final = fields().that().haveRawType(Logger.class)
      .should().bePrivate()
      .andShould().beStatic()
      .andShould().beFinal()
      .because("we agreed on this convention");

  @ArchTest
  private final ArchRule no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

  @ArchTest
  private final ArchRule uuid_randomUUID_should_not_be_called_directly = noClasses()
      .that().doNotImplement(RandomGeneratorPort.class)
      .should()
      .callMethod(UUID.class, "randomUUID")
      .because("RandomGeneratorPort should be used instead");

    @ArchTest
    private final ArchRule configurationProperties_must_be_validated = classes()
      .that().areAnnotatedWith(ConfigurationProperties.class)
      .should().beAnnotatedWith(Validated.class);
}
