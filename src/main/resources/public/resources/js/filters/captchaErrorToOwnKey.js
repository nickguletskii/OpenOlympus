import { filters } from "app";

filters.filter("captchaErrorToOwnKey", () => (input) => `recaptcha.${input}`);
