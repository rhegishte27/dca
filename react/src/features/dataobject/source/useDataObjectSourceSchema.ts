import { useTranslation } from 'react-i18next';

export const useDataObjectSourceSchema = () => {

    const { i18n } = useTranslation(['dataObject']);

    const fields = {
        identifier: {
            label: i18n.t('dataObject:identifier'),
            name: 'identifier'
        },
        system: {
            label: i18n.t('dataObject:system'),
            name: 'system'
        },
        source: {
            label: i18n.t('dataObject:source'),
            name: 'source'
        }
    };

    const validations = {};

    return { fields, validations };
};