import { useTranslation } from 'react-i18next';
import { useValidation } from '../common/useValidation';

export const useSettingSchema = () => {
    const {required, sizeLimit, numberRange} = useValidation();
    const {i18n} = useTranslation(['setting']);

    const fields = {
        language: {
            label: i18n.t('setting:language'),
            name: 'language'
        },
        tokenDuration: {
            label: i18n.t('setting:tokenDuration'),
            name: 'tokenDuration',
            min: 20,
            max: 120
        },
        commonFolder: {
            label: i18n.t('setting:commonFolder'),
            name: 'commonFolder'
        },
        defaultImportFolder: {
            label: i18n.t('setting:defaultImportFolder'),
            name: 'defaultImportFolder'

        },
        defaultExportFolder: {
            label: i18n.t('setting:defaultExportFolder'),
            name: 'defaultExportFolder'
        },
        defaultDownloadFolder: {
            label: i18n.t('setting:defaultDownloadFolder'),
            name: 'defaultDownloadFolder'
        },
        path: {
            minSize: 1,
            maxSize: 256,
            backSlash: '/',
            forwardSlash: '\\',
            regExp: RegExp('^(?!.*[\\\\\\/]\\s+)(?!(?:.*\\s|.*\\.|\\W+)$)(?:[a-zA-Z]:)?(?:(?:[^<>:"\\|\\?\\*\\n])+(?:\\/\\/|\\/|\\\\\\\\|\\\\)?)+$')
        }
    };

    const validateLanguage = (value: string): string =>
        required(fields.language.label, value);

    const validateCommonFolder = (value: string): string =>
        (fields.path.regExp.test(value)) ? '' : i18n.t('setting:message:pathFormat') ||
            required(fields.commonFolder.label, value) ||
            sizeLimit(fields.commonFolder.label, value, fields.path.minSize, fields.path.maxSize);

    const validateDefaultImportFolder = (value: string, values: any): string =>
        validateDefaultFolder(value, values) ||
        required(fields.defaultImportFolder.label, value) ||
        sizeLimit(fields.defaultImportFolder.label, value, fields.path.minSize, fields.path.maxSize);

    const validateDefaultExportFolder = (value: string, values: any): string =>
        validateDefaultFolder(value, values) ||
        required(fields.defaultExportFolder.label, value) ||
        sizeLimit(fields.defaultExportFolder.label, value, fields.path.minSize, fields.path.maxSize);

    const validateDefaultDownloadFolder = (value: string, values: any): string =>
        validateDefaultFolder(value, values) ||
        required(fields.defaultDownloadFolder.label, value) ||
        sizeLimit(fields.defaultDownloadFolder.label, value, fields.path.minSize, fields.path.maxSize);

    const validateDefaultFolder = (value: any, values: any): string => {
        let commonFolder: string = values[fields.commonFolder.name];

        if (!commonFolder) {
            commonFolder = '';
        }

        const pathValid = validatePath(value);
        if (pathValid) {
            return pathValid;
        }

        if (commonFolder.endsWith(fields.path.backSlash) || commonFolder.endsWith(fields.path.forwardSlash)) {
            return isNotEqualsAndStartsWith(value, commonFolder) ? '' : i18n.t('setting:message:pathSubFolder');
        }

        return (isNotEqualsAndStartsWith(value, commonFolder + fields.path.backSlash)
            || isNotEqualsAndStartsWith(value, commonFolder + fields.path.forwardSlash))
            ? '' : i18n.t('setting:message:pathSubFolder');
    };

    const isNotEqualsAndStartsWith = (s: string, commonFolder: string): boolean => {
        return s !== null
            && commonFolder !== null
            && s.toString() !== commonFolder.toString()
            && s.startsWith(commonFolder);
    };

    const validatePath = (value: string): string =>
        (fields.path.regExp.test(value)) ? '' : i18n.t('setting:message:pathFormat');

    const validateTokenDuration = (value: string): string => {
        const fieldName = fields.tokenDuration.label;

        return required(fieldName, value) || numberRange(fieldName, value, fields.tokenDuration.min, fields.tokenDuration.max);
    };


    const validations = {
        [fields.language.name]: validateLanguage,
        [fields.tokenDuration.name]: validateTokenDuration,
        [fields.commonFolder.name]: validateCommonFolder,
        [fields.defaultImportFolder.name]: validateDefaultImportFolder,
        [fields.defaultExportFolder.name]: validateDefaultExportFolder,
        [fields.defaultDownloadFolder.name]: validateDefaultDownloadFolder
    };

    return {fields, validations};
};