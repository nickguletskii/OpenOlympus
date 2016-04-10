/*
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
