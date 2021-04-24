import { useTranslation } from 'react-i18next';
import { useValidation } from '../common/useValidation';

export const useProjectSchema = () => {
    const { required, sizeLimit, isEmpty, isGreaterThanZero } = useValidation();
    const { i18n } = useTranslation(['message', 'project', 'common']);

    const fields = {
        id: {
            name: 'id'
        },
        identifier: {
            label: i18n.t('project:identifier'),
            name: 'identifier',
            minSize: 3,
            maxSize: 8,
            regExp: RegExp(/^[a-zA-Z][a-zA-Z0-9-]*$/)
        },
        description: {
            label: i18n.t('project:description'),
            name: 'description',
            minSize: 1,
            maxSize: 255
        },
        generatedCodeLanguage: {
            label: i18n.t('project:generatedCodeLanguage'),
            name: 'generatedCodeLanguage',
        },
        compiler: {
            label: i18n.t('project:compiler'),
            name: 'compiler',
        },
        synchronize: {
            label: i18n.t('project:synchronize'),
            name: 'isSynchronizationEnabled',
            enum: {
                ENABLE: {
                    label: i18n.t('common:enable'),
                    value: 'true'
                },
                DISABLE: {
                    label: i18n.t('common:disable'),
                    value: 'false'
                }
            }
        },
        backup: {
            label: i18n.t('project:backup'),
            name: 'isBackupEnabled',
            enum: {
                ENABLE: {
                    label: i18n.t('common:enable'),
                    value: 'true'
                },
                DISABLE: {
                    label: i18n.t('common:disable'),
                    value: 'false'
                }
            }
        },

        backupWrapper: {
            label: i18n.t('project:backupEvery'),
            backupInterval: {
                label: i18n.t('project:backupInterval'),
                name: 'backupInterval',
            },
            numberBackupInterval: {
                name: 'numberOfBackupIntervals'
            }
        },
        backupKeepWrapper: {
            label: i18n.t('project:keepBackup'),
            backupKeepInterval: {
                label: i18n.t('project:keepBackupInterval'),
                name: 'backupKeepInterval',
            },
            numberBackupKeepInterval: {
                name: 'numberOfBackupKeepIntervals'
            }
        },
        validation: {
            required: 'Required',
            greaterThanZero: 'GreaterThanZero'
        },
        projectSystems: {
            name: 'systems'
        },
        projectSyncSettings: {
            name: 'projectSyncSettings'
        },
        synchronizeProjectElements: {
            label: i18n.t('project:synchronizeProjectElements'),
            name: 'isSynchronizeProjectElementsEnabled',
            enum: {
                ENABLE: {
                    label: i18n.t('common:enable'),
                    value: 'true'
                },
                DISABLE: {
                    label: i18n.t('common:disable'),
                    value: 'false'
                }
            }
        },

        lastBackup: {
            label: i18n.t('project:lastBackup'),
            name: 'lastBackup'
        }
    };

    const validateIdentifier = (value: string): string =>
        required(fields.identifier.label, value) ||
        (fields.identifier.regExp.test(value) ? '' : i18n.t('message:validation:pattern', {what: fields.identifier.label})) ||
        sizeLimit(fields.identifier.label, value, fields.identifier.minSize, fields.identifier.maxSize);

    const validateDescription = (value: string): string =>
        required(fields.description.label, value) ||
        sizeLimit(fields.description.label, value, fields.description.minSize, fields.description.maxSize);

    const validateGeneratedCodeLanguage = (value: string): string =>
        required(fields.generatedCodeLanguage.label, value);

    const validateCompiler = (value: string): string =>
        required(fields.compiler.label, value);

    const isBackup = (values: any): boolean =>
        values[fields.backup.name] === fields.backup.enum.ENABLE.value;

    const validateBackupInterval = (value: string, values: any): string => {
        if (!isBackup(values)) {
            return '';
        }
        return required(fields.backupWrapper.backupInterval.label, value) ? fields.validation.required : '';
    };

    const validateBackupKeepInterval = (value: string, values: any): string =>  {
        if (!isBackup(values)) {
            return '';
        }
        return required(fields.backupKeepWrapper.backupKeepInterval.label, value) ? fields.validation.required : '';
    };

    const validateNumberOfBackupIntervals = (value: string, values: any): string => {
        if (!isBackup(values)) {
            return '';
        }

        if (isEmpty(value)) {
            return fields.validation.required;
        }

        return isGreaterThanZero(value) ? '' : fields.validation.greaterThanZero;
    };

    const validateNumberOfBackupKeepIntervals = (value: string, values: any): string =>{
        if (!isBackup(values)) {
            return '';
        }
        if (isEmpty(value)) {
            return fields.validation.required;
        }

        return isGreaterThanZero(value) ? '' : fields.validation.greaterThanZero;
    };

    const validations = {
        [fields.identifier.name]: validateIdentifier,
        [fields.description.name]: validateDescription,
        [fields.generatedCodeLanguage.name]: validateGeneratedCodeLanguage,
        [fields.compiler.name]: validateCompiler,
        [fields.backupWrapper.backupInterval.name]: validateBackupInterval,
        [fields.backupWrapper.numberBackupInterval.name]: validateNumberOfBackupIntervals,
        [fields.backupKeepWrapper.backupKeepInterval.name]: validateBackupKeepInterval,
        [fields.backupKeepWrapper.numberBackupKeepInterval.name]: validateNumberOfBackupKeepIntervals,
    };

    return { fields, validations };
};
