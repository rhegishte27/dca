import { useTranslation } from 'react-i18next';
import { useValidation } from '../../common/useValidation';

export const useProjectSystemSchema = () => {
    const { required } = useValidation();
    const { i18n } = useTranslation(['project', 'system', 'common']);

    const fields = {
        system: {
            name: 'system',
            label: i18n.t('project:system')
        },
        systemType: {
            name: 'systemType',
            label: i18n.t('system:type')
        },
        location: {
            label: i18n.t('project:location'),
            name: 'location'
        },

        synchronizeSystem: {
            label: i18n.t('project:synchronizeSystem'),
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
        }
    };

    const validateSystem = (value: string): string => {
        return required(fields.system.label, value);
    };

    const validateSystemType = (value: string): string => {
        return required(fields.systemType.label, value);
    };

    const validateSynchronizeSystem = (value: string): string => {
        return required(fields.synchronizeSystem.label, value);
    };

    const validateLocation = (value: string, values: any): string => {
        const isSyncSystem = values[fields.synchronizeSystem.name];

        if (isSyncSystem && isSyncSystem.toString() === fields.synchronizeSystem.enum.ENABLE.value) {
            return required(fields.location.label, value);
        }
        return '';
    };

    const validations = {
        [fields.system.name]: validateSystem,
        [fields.systemType.name]: validateSystemType,
        [fields.synchronizeSystem.name]: validateSynchronizeSystem,
        [fields.location.name]: validateLocation
    };

    return { fields, validations };
};
