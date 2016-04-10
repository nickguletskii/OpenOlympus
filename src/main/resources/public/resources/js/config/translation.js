import moment from "moment";
import module from "app";
import { union as _union } from "lodash";

export function installTranslation() {
	module
		.factory("missingTranslationHandler", () =>
			(translationId) => {
				const orig = angular.fromJson(
					localStorage.getItem("missingTranslations") || "[]");
				const unified = _union(orig, [translationId]);
				localStorage.setItem("missingTranslations",
					angular.toJson(unified));
			});
	module
		.config(/* @ngInject*/ ($translateProvider) => {
			$translateProvider.useSanitizeValueStrategy("escape");
			$translateProvider.useUrlLoader("/translation");
			$translateProvider.preferredLanguage("ru");
			// $translateProvider.translationNotFoundIndicator("$$");
			$translateProvider.useMissingTranslationHandler(
				"missingTranslationHandler");
		});
	module.run(/* @ngInject*/ ($rootScope, $translate, FormForConfiguration) => {
		// Make sure that locales are synchronised across libraries
		$rootScope.$on("$translateChangeSuccess",
			(event, language) => {
				$rootScope.currentLanguage = language.language;
				moment.locale(language.language);
			});

		$rootScope.availableLanguages = ["en", "ru"]; // TODO: Make this dynamic

		// Expose $translate.use to the language change menu
		$rootScope.changeLanguage = $translate.use;

		$translate(["validation.failed.custom", "validation.failed.email",
				"validation.failed.integer",
				"validation.failed.maxCollectionSize", "validation.failed.maximum",
				"validation.failed.maxLength",
				"validation.failed.minimum", "validation.failed.minCollectionSize",
				"validation.failed.minLength",
				"validation.failed.negative", "validation.failed.nonNegative",
				"validation.failed.numeric",
				"validation.failed.pattern", "validation.failed.positive",
				"validation.failed.empty"
			])
			.then((translations) => {
				FormForConfiguration.setValidationFailedForCustomMessage(translations[
					"validation.failed.custom"]);
				FormForConfiguration.setValidationFailedForEmailTypeMessage(translations[
					"validation.failed.email"]);
				FormForConfiguration.setValidationFailedForIntegerTypeMessage(
					translations["validation.failed.integer"]);
				FormForConfiguration.setValidationFailedForMaxCollectionSizeMessage(
					translations["validation.failed.maxCollectionSize"]);
				FormForConfiguration.setValidationFailedForMaximumMessage(translations[
					"validation.failed.maximum"]);
				FormForConfiguration.setValidationFailedForMaxLengthMessage(translations[
					"validation.failed.maxLength"]);
				FormForConfiguration.setValidationFailedForMinimumMessage(translations[
					"validation.failed.minimum"]);
				FormForConfiguration.setValidationFailedForMinCollectionSizeMessage(
					translations["validation.failed.minCollectionSize"]);
				FormForConfiguration.setValidationFailedForMinLengthMessage(translations[
					"validation.failed.minLength"]);
				FormForConfiguration.setValidationFailedForNegativeTypeMessage(
					translations["validation.failed.negative"]);
				FormForConfiguration.setValidationFailedForNonNegativeTypeMessage(
					translations["validation.failed.nonNegative"]);
				FormForConfiguration.setValidationFailedForNumericTypeMessage(
					translations["validation.failed.numeric"]);
				FormForConfiguration.setValidationFailedForPatternMessage(translations[
					"validation.failed.pattern"]);
				FormForConfiguration.setValidationFailedForPositiveTypeMessage(
					translations["validation.failed.positive"]);
				FormForConfiguration.setValidationFailedForRequiredMessage(translations[
					"validation.failed.empty"]);
				FormForConfiguration.disableAutoTrimValues();
			});
	});
}
