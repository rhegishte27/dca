import { useTranslation } from 'react-i18next';
import { useValidation } from '../../common/useValidation';

export const useProjectSyncSettingSchema = () => {
    const { required } = useValidation();
    const { i18n } = useTranslation(['project','common']);

    const fields = {
        elementType: {
            label: i18n.t('project:elementType'),
            name: 'typeElement'
        },
        synchronizeProjectElement: {
            label: i18n.t('project:synchronizeProjectElement'),
            name: 'isSynchronizeProjectElement',
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
        location: {
            label: i18n.t('project:location'),
            name: 'locationProjectElement'
        }
    };

    const validateLocation = (value: string, values: any): string => {
        const isSyncProjectElement = values[fields.synchronizeProjectElement.name];

        if (isSyncProjectElement && isSyncProjectElement.toString() === fields.synchronizeProjectElement.enum.ENABLE.value) {
            return required(fields.location.label, value);
        }
        return '';
    };

    const validations = {
        [fields.location.name]: validateLocation
    };

    return { fields, validations };
};