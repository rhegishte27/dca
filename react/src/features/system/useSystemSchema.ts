import { useTranslation } from 'react-i18next';
import { useValidation } from '../common/useValidation';

export const useSystemSchema = () => {
    const { required, sizeLimit } = useValidation();
    const { i18n } = useTranslation(['message', 'system']);

    const fields = {
        id: {
            name: 'id'
        },
        identifier: {
            label: i18n.t('system:identifier'),
            name: 'identifier',
            minSize: 3,
            maxSize: 8,
            regExp: RegExp(/^[a-zA-Z][a-zA-Z0-9-]*$/)
        },
        description: {
            label: i18n.t('system:description'),
            name: 'description',
            minSize: 1,
            maxSize: 255
        }
    };

    const validateIdentifier = (value: string): string =>
        required(fields.identifier.label, value) ||
        (fields.identifier.regExp.test(value) ? '' : i18n.t('message:validation:pattern', {what: fields.identifier.label})) ||
        sizeLimit(fields.identifier.label, value, fields.identifier.minSize, fields.identifier.maxSize);

    const validateDescription = (value: string): string =>
        required(fields.description.label, value) ||
        sizeLimit(fields.description.label, value, fields.description.minSize, fields.description.maxSize);

    const validations = {
        [fields.identifier.name]: validateIdentifier,
        [fields.description.name]: validateDescription
    };

    return { fields, validations };
};
