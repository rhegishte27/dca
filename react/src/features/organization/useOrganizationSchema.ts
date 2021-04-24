import { useTranslation } from 'react-i18next';
import { useValidation } from '../common/useValidation';

export const useOrganizationSchema = () => {
    const { required, sizeLimit } = useValidation();
    const { i18n } = useTranslation(['organization']);

    const fields = {
        name: {
            label: i18n.t('organization:name'),
            name: 'name',
            minSize: 1,
            maxSize: 50
        },
        description: {
            label: i18n.t('organization:description'),
            name: 'description',
            minSize: 1,
            maxSize: 255
        }
    };

    const validateName = (value: string): string =>
        required(fields.name.label, value) ||
        sizeLimit(fields.name.label, value, fields.name.minSize, fields.name.maxSize);

    const validateDescription = (value: string): string =>
        required(fields.description.label, value) ||
        sizeLimit(fields.description.label, value, fields.description.minSize, fields.description.maxSize);

    const validations = {
        [fields.name.name]: validateName,
        [fields.description.name]: validateDescription
    };

    return { fields, validations };
};
